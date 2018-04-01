package com.phayao.thinkdast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class WikiFetcher {
    private long lastRequestTime = -1;
    private long minInterval = 1000;

    /**
     * Fetches and parses a URL string, returning a list of paragraph elements.
     * @param url
     * @return
     * @throws IOException
     */
    public Elements fetchWikipedia(String url) throws IOException {
        sloopIfNeed();

        // download and parse the docuenment
        Connection connection = Jsoup.connect(url);
        Document document = connection.get();

        // select the content text and pull out the paragraph
        Element content = document.getElementById("mw-content-text");

        // TODO: avoid selecting paragraphs from sidebars and boxouts
        Elements paragraphs = content.select("p");
        return paragraphs;
    }

    public Elements readWikipedia(String url) throws IOException {
        URL readURL = new URL(url);

        // assemble the first name
        String slash = File.separator;
        //String filename = "resources" + slash + readURL.getHost() + readURL.getPath();
        String filename = readURL.getHost() + readURL.getPath();

        // read the file
        InputStream stream = WikiFetcher.class.getClassLoader().getResourceAsStream(filename);
        Document document = Jsoup.parse(stream, "UTF-8", filename);

        // parse the contents of the file
        Element content = document.getElementById("mw-content-text");
        Elements paragraphs = content.select("p");
        return paragraphs;
    }

    /**
     * Rate limits by waiting at least the minimum interval between requests.
     */
    private void sloopIfNeed() {
        if(lastRequestTime != -1) {
            long currentTime = System.currentTimeMillis();
            long nextRequestTime = lastRequestTime + minInterval;
            if(currentTime < nextRequestTime) {
                try {
                    Thread.sleep(nextRequestTime - currentTime);
                }
                catch (InterruptedException e) {
                    System.err.println("Warning: sleep interrupted in fetchWikipedia");
                }
            }
        }
        lastRequestTime = System.currentTimeMillis();
    }

    public static void main(String[] args) throws IOException {
        WikiFetcher wf = new WikiFetcher();
        String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
        Elements paragraphs = wf.readWikipedia(url);

        for(Element paragraph: paragraphs) {
            System.out.println(paragraph);
        }
    }
}
