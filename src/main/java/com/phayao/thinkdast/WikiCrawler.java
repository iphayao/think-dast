package com.phayao.thinkdast;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class WikiCrawler {
    // keeps track of where we started
    private final String source;

    // the index where the result go
    private JedisIndex index;

    // queue of URLs to be indexed
    private Queue<String> queue = new LinkedList<String>();

    // fetcher used to get pages from WikiPedia
    final static WikiFetcher wf = new WikiFetcher();

    /**
     * Constructor
     *
     * @param source
     * @param index
     */
    public WikiCrawler(String source, JedisIndex index) {
        this.source = source;
        this.index = index;
        queue.offer(source);
    }

    /**
     * Return the number of URLs in the queue.
     * @return
     */
    public int queueSize() {
        return queue.size();
    }

    /**
     * Get a URL fro the queue and indexes it.
     * @param testing
     * @return
     * @throws IOException
     */
    public String crawl(boolean testing) throws IOException {
        // TODO:
        if(queue.isEmpty()) {
            return null;
        }
        String url = queue.poll();
        System.out.println("Crawling " + url);

        if(testing == false && index.isIndexOf(url)) {
            System.out.println("Already Indexed.");
            return null;
        }

        Elements paragraphs;
        if(testing) {
            paragraphs = wf.readWikipedia(url);
        }
        else {
            paragraphs = wf.fetchWikipedia(url);
        }
        index.indexPage(url, paragraphs);
        queueInternalLinks(paragraphs);
        return url;
    }

    public void queueInternalLinks(Elements paragraphs) {
        // TODO:
        for(Element paragraph: paragraphs) {
            queueInternalLinks(paragraph);
        }
    }

    private void queueInternalLinks(Element paragraph) {
        Elements elements = paragraph.select("a[href]");
        for(Element element: elements) {
            String relURL = element.attr("href");

            if(relURL.startsWith("/wiki/")) {
                String absURL =  "https://en.wikipedia.org" + relURL;
                queue.offer(absURL);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        // make a WikiClawler
        Jedis jedis = JedisMaker.make();
        JedisIndex index = new JedisIndex(jedis);
        String source = "https://en.wikipedia.org/wiki/Java_(programming_language)";
        WikiCrawler wc = new WikiCrawler(source, index);

        // for testing purposes, load up the queue
        Elements paragraphs = wf.fetchWikipedia(source);
        wc.queueInternalLinks(paragraphs);

        // loop until we index a new page
        String res;
        do {
            res = wc.crawl(false);
            //break;
        } while (res == null);

        Map<String, Integer> map = index.getCounts("the");
        for(Map.Entry<String, Integer> entry: map.entrySet()) {
            System.out.println(entry);
        }

    }
}
