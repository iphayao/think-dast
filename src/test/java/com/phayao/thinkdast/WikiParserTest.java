package com.phayao.thinkdast;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class WikiParserTest {
    final static WikiFetcher wf = new WikiFetcher();

    @Test
    public void testFindFirstLink1() throws IOException {
        String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
        String href = findFirstLink(url);
        assertThat(href, is("/wiki/Programming_language"));
    }

    @Test
    public void testFindFirstLink2() throws IOException {
        String url = "https://en.wikipedia.org/wiki/Mathematics";
        String href = findFirstLink(url);
        assertThat(href, is("/wiki/Quantity"));
    }

    private String findFirstLink(String url) throws IOException {
        Elements paragraphs = wf.readWikipedia(url);
        WikiParser wp = new WikiParser(paragraphs);
        Element element = wp.findFirstLink();
        String href = element.attr("href");
        return href;
    }

}