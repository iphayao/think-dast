package com.phayao.thinkdast;

import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TermCounter {
    private Map<String, Integer> map;
    private String label;

    public TermCounter(String label) {
        this.label = label;
        this.map = new HashMap<String, Integer>();
    }

    public String getLabel() {
        return label;
    }

    /**
     * Return the total of all counts.
     *
     * @return
     */
    public int size() {
        int total = 0;
        for (Integer value: map.values()) {
            total += value;
        }
        return total;
    }

    /**
     * Take collection of Elements and counts their words.
     *
     * @param paragraphs
     */
    public void processElements(Elements paragraphs) {
        for(Node node: paragraphs) {
            processTree(node);
        }
    }

    /**
     * Finds TextNodes in a DOM tree and counts their words.
     *
     * @param root
     */
    private void processTree(Node root) {
        // we already have a free iterator, let's use it.
        for(Node node: new WikiNodeIterable(root)) {
            if(node instanceof TextNode) {
                processText(((TextNode)node).text());
            }
        }
    }

    /**
     * Split `text` into words and counts them.
     *
     * @param text
     */
    private void processText(String text) {
        // replace punctuation with spaces, convert to lower case, and split on whitespace.
        String[] array = text.replace("\\p{P}", " ").toLowerCase().split("\\s+");

        for(int i = 0; i < array.length; i++) {
            String term = array[i];
            incrementTermCount(term);
        }

    }

    /**
     * Increments the counter associated with `term`.
     *
     * @param term
     */
    private void incrementTermCount(String term) {
        put(term, get(term) + 1);

    }

    /**
     * Adds a term to the map with a given count.
     *
     * @param term
     * @param count
     */
    public void put(String term, int count) {
        map.put(term, count);
    }

    /**
     * Returns the count associated with this term, or 0 if it is unseen.
     *
     * @param term
     * @return
     */
    public Integer get(String term) {
        Integer count = map.get(term);
        return count == null ? 0 : count;
    }

    /**
     * Returns the set of terms that have been counted.
     *
     * @return
     */
    public Set<String> keySet() {
        return map.keySet();
    }

    /**
     * Print the terms and their count in arbitrary order.
     */
    public void printCounts() {
        for(String key: keySet()) {
            Integer count = get(key);
            System.out.println(key + ", " + count);
        }
        System.out.println("Total of all counts = " + size());
    }

    public static void main(String[] args) throws IOException {
        String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";

        WikiFetcher wf = new WikiFetcher();
        Elements paragraphs = wf.fetchWikipedia(url);

        TermCounter counter = new TermCounter(url.toString());
        counter.processElements(paragraphs);
        counter.printCounts();
    }

}
