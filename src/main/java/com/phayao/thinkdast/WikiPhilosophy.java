package com.phayao.thinkdast;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WikiPhilosophy {
    final static List<String> visited = new ArrayList<String>();
    final static WikiFetcher wf = new WikiFetcher();

    /**
     * Tests a conjecture about Wikipedia and Philosophy.
     *
     * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
     *
     * 1. Clicking on the first non-parenthesized, non-italicized link
     * 2. Ignoring external links, links to the current page, or red links
     * 3. Stopping when reaching "Philosophy", a page with no links or a page
     *    the does not exist, or when a loop occurs
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String destination = "https://en.wikipedia.org/wiki/Philosopy";
        String source = "https://en.wikipedia.org/wiki/Java_(programming_language)";

        testConjecture(destination, source, 10);
    }

    /**
     * Starts from given URL and follows first link until it finds the destinations or exceeds the limit.
     *
     * @param destination
     * @param source
     * @param limit
     * @throws IOException
     */
    private static void testConjecture(String destination, String source, int limit) throws IOException {
        // TODO: Implement this
        String url = source;
        for(int i = 0; i < limit; i++) {
            if(visited.contains(url)) {
                System.err.println("We're in a loop, exiting");
                return;
            }
            else {
                visited.add(url);
            }

            Element element = getFirstValidLink(url);
            if(element == null) {
                System.err.println("Got to a page with no valid links");
            }
            System.out.println("**" + element.text() + "**");
            url = element.attr("abs:href");
            if(url.equals(destination)) {
                System.out.println("Eureka!");
                break;
            }
        }
    }

    private static Element getFirstValidLink(String url) throws IOException {
        System.out.println(String.format("Fetching %s", url));
        Elements paragraphs = wf.fetchWikipedia(url);
        WikiParser wikiParser = new WikiParser(paragraphs);
        Element element = wikiParser.findFirstLink();
        return element;
    }
}
