package com.phayao.thinkdast;

import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.*;

public class WikiSearch {
    // map from URLs that contain the term(s) to relevance score
    private Map<String, Integer> map;

    /**
     * Constructor
     * @param map
     */
    public WikiSearch(Map<String, Integer> map) {
        this.map = map;
    }

    /**
     * Look up the relevance of given URL.
     * @param url
     * @return
     */
    public Integer getRelevance(String url) {
        Integer relevance = map.get(url);
        return relevance == null ? 0 : relevance;
    }

    /**
     * Prints the contents in order of term frequency
     */
    private void print() {
        List<Map.Entry<String, Integer>> entries = sort();
        for(Map.Entry<String, Integer> entry: entries) {
            System.out.println(entry);
        }
    }

    /**
     * Computes the union of two search results
     *
     * @param that
     * @return
     */
    public WikiSearch or(WikiSearch that) {
        Map<String, Integer> union = new HashMap<String, Integer>(map);
        for(String term: that.map.keySet()) {
            int relevance = totalRelevance(this.getRelevance(term), that.getRelevance(term));
            union.put(term, relevance);
        }
        return new WikiSearch(union);
    }

    /**
     * Computes that intersection of two search results
     *
     * @param that
     * @return
     */
    public WikiSearch and(WikiSearch that) {
        Map<String, Integer> intersection = new HashMap<String, Integer>();
        for(String term: map.keySet()) {
            if(that.map.containsKey(term)) {
                int relevance = totalRelevance(this.map.get(term), that.map.get(term));
                intersection.put(term, relevance);
            }
        }
        return new WikiSearch(intersection);
    }

    /**
     * Computes that difference of two search results
     *
     * @param that
     * @return
     */
    public WikiSearch minus(WikiSearch that) {
        Map<String, Integer> difference = new HashMap<String, Integer>(map);
        for(String term: that.map.keySet()) {
            difference.remove(term);
        }
        return new WikiSearch(difference);
    }

    /**
     * Computes the relevance of a search with multiple terms
     *
     * @param rel1
     * @param rel2
     * @return
     */
    protected int totalRelevance(Integer rel1, Integer rel2) {
        // simple starting place: relevance is the sum of the term frequencies.
        return rel1 + rel2;
    }

    /**
     * Sort the results by relevance
     *
     * @return
     */
    public List<Map.Entry<String, Integer>> sort() {
        // NOTE: this can be done more concisely in Java 8. see
        // http://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values-java

        // make a list of entries
        List<Map.Entry<String, Integer>> entries = new LinkedList<Map.Entry<String, Integer>>(map.entrySet());

        // make a Comparator object for sorting
        Comparator<Map.Entry<String, Integer>> comparator = new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        };

        Collections.sort(entries, comparator);
        return entries;
    }

    /**
     * Performs a search and makes a WikiSearch object.
     *
     * @param term
     * @param index
     * @return
     */
    public static WikiSearch search(String term, JedisIndex index) {
        Map<String, Integer> map = index.getCounts(term);
        return new WikiSearch(map);
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {
        // make a JadisIndex
        Jedis jedis = JedisMaker.make();
        JedisIndex index = new JedisIndex(jedis);

        // search for the first term
        String term1 = "java";
        System.out.println("Query: " + term1);
        WikiSearch search1 = search(term1, index);
        search1.print();

        // search for the second term
        String term2 = "programming";
        System.out.println("Query: " + term2);
        WikiSearch search2 = search(term2, index);
        search2.print();
    }
}
