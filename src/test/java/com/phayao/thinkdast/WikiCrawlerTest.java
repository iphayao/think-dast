package com.phayao.thinkdast;

import org.jsoup.select.Elements;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class WikiCrawlerTest {
    private Jedis jedis;
    private WikiCrawler crawler;
    private JedisIndex index;

    @Before
    public void setUp() throws Exception {
        // make a WikiCrawler
        jedis = JedisMaker.make();
        index = new JedisIndex(jedis);
        String source = "https://en.wikipedia.org/wiki/Java_(programming_language)";
        crawler = new WikiCrawler(source, index);

        // for testing purpose, load up the queue
        WikiFetcher fetcher = new WikiFetcher();
        Elements paragraphs = fetcher.readWikipedia(source);
        crawler.queueInternalLinks(paragraphs);
    }

    @After
    public void tearDown() throws Exception {
        index.deleteAllKeys();
        jedis.close();
    }

    @Test
    public void testCrawl() throws IOException {
        String res = null;
        int count = 0;

        String url1 = "https://en.wikipedia.org/wiki/Java_(programming_language)";
        String url2 = "https://en.wikipedia.org/wiki/Programming_language";
        String url3 = "https://en.wikipedia.org/wiki/Concurrent_computing";

        res = crawler.crawl(true);
        assertThat(url1.equals(res), is(true));
        assertThat(crawler.queueSize(), is(396));

        res = crawler.crawl(true);
        assertThat(url2.equals(res), is(true));
        assertThat(crawler.queueSize(), is(653));

        res = crawler.crawl(true);
        assertThat(url3.equals(res), is(true));
        assertThat(crawler.queueSize(), is(704));

        Map<String, Integer> map = index.getCounters("the");

        count = map.get(url1);
        assertThat(count, is(339));

        count = map.get(url2);
        assertThat(count, is(264));

        count = map.get(url3);
        assertThat(count, is(53));
    }
}