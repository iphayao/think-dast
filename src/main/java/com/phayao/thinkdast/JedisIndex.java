package com.phayao.thinkdast;

import org.jsoup.select.Elements;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;


import java.io.IOException;
import java.util.*;

/**
 * Represent a Redis-backed web search index
 */
public class JedisIndex {
    private Jedis jedis;

    /**
     * Constructor.
     * @param jedis
     */
    public JedisIndex(Jedis jedis) {
        this.jedis = jedis;
    }

    /**
     * Returns the Redis key for a given search term.
     * @param term
     * @return
     */
    private String urlSetKey(String term) {
        return "URLSet:" + term;
    }

    /**
     * Returns the Redis key for a URL's TermCounter
     * @param url
     * @return
     */
    private String termCounterKey(String url) {
        return "TermCounter:" + url;
    }

    /**
     * Checks whether we have a TeamCounter for a given URL.
     * @param url
     * @return
     */
    public boolean isIndexOf(String url) {
        String redisKey = termCounterKey(url);
        return jedis.exists(redisKey);
    }

    /**
     * Adds a URL to the set associated with 'term'
     * @param term
     * @param tc
     */
    public void add(String term, TermCounter tc) {
        jedis.sadd(urlSetKey(term), tc.getLabel());
    }

    /**
     * Looks up a search term and returns a set of URLs.
     * @param term
     * @return
     */
    public Set<String> getURLs(String term) {
        Set<String> set = jedis.smembers(urlSetKey(term));
        return set;
    }

    /**
     * Looks up a term and returns a map from URL to count.
     * @param term
     * @return
     */
    public Map<String, Integer> getCounts(String term) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        Set<String> urls = getURLs(term);
        for(String url: urls) {
            Integer count = getCount(url, term);
            map.put(url, count);
        }
        return map;
    }

    /**
     * Looks up a term and returns a map from URL to count.
     * @param term
     * @return
     */
    public Map<String, Integer> getCounterFaster(String term) {
        // convert the set of strings to a list so we get the same traversal order every time
        List<String> urls = new ArrayList<String>();
        urls.addAll(getURLs(term));

        // construct a transaction to perform all lookups
        Transaction t = jedis.multi();
        for(String url: urls) {
            String redisKey = termCounterKey(url);
            t.hget(redisKey, term);
        }
        List<Object> res = t.exec();

        // iterate the results and make the map
        Map<String, Integer> map = new HashMap<String, Integer>();
        int i = 0;
        for(String url: urls) {
            System.out.println(url);
            Integer count = new Integer((String)res.get(i++));
            map.put(url, count);
        }
        return map;
    }

    /**
     * Returns the number of times the given term appears at the given URL.
     * @param url
     * @param term
     * @return
     */
    private Integer getCount(String url, String term) {
        String redisKey = termCounterKey(url);
        String count = jedis.hget(redisKey, term);
        return new Integer(count);
    }

    /**
     * Add a page to the index
     * @param url
     * @param paragraphs
     */
    public void indexPage(String url, Elements paragraphs) {
        System.out.println("Indexing " + url);

        // make a TermCounter and count the terms in teh paragraphs
        TermCounter tc = new TermCounter(url);
        tc.processElements(paragraphs);

        // push the counters of the TermCounter to Redis
        pushTermCounterToRedis(tc);
    }

    /**
     * Pushes the contents of the TermCounters to Redis.
     * @param tc
     */
    private List<Object> pushTermCounterToRedis(TermCounter tc) {
        Transaction t = jedis.multi();

        String url = tc.getLabel();
        String hashName = termCounterKey(url);

        // if this page has already been indexed; deleted the old hash
        t.del(hashName);

        // for each term, add a entry in the TermCounter and a new member of the index
        for(String term: tc.keySet()) {
            Integer count = tc.get(term);
            t.hset(hashName, term, count.toString());
            t.sadd(urlSetKey(term), url);
        }
        List<Object> res = t.exec();
        return res;
    }

    /**
     * Print the contents of the index.
     * Should be used for development and testing, not production.
     */
    public void printIndex() {
        // loop through the search terms.
        for(String term: termSet()) {
            System.out.println(term);
            // for each term, print the pages where it appears
            Set<String> urls = getURLs(term);
            for(String url: urls) {
                Integer count = getCount(url, term);
                System.out.println("   " + url + " " + count);
            }
        }
    }

    /**
     * Return the set of terms that have been indexed.
     * Should be use for development and testing, not production.
     * @return
     */
    private Set<String> termSet() {
        Set<String> keys = urlSetKeys();
        Set<String> terms = new HashSet<String>();
        for(String key: keys) {
            String[] array = key.split(":");
            if(array.length < 2) {
                terms.add("");
            }
            else {
                terms.add(array[1]);
            }
        }
        return null;
    }

    /**
     * Returns URLSet key for the terms that have been indexed
     * Should be use for development and testing, not production.
     * @return
     */
    private Set<String> urlSetKeys() {
        return jedis.keys("URLSet:*");
    }

    /**
     * Returns TermCounter keys for the URLs that have been indexed.
     * Should be use for development and testing, not production.
     * @return
     */
    public Set<String> termCounterKeys() {
        return jedis.keys("TermCounter:*");
    }

    /**
     * Deletes all URLSet objects for database
     * Should be use for development and testing, not production.
     */
    public void deleteURLSets() {
        Set<String> keys = urlSetKeys();
        Transaction t = jedis.multi();
        for(String key: keys) {
            t.del(key);
        }
        t.exec();
    }

    /**
     * Deletes all URLSet object from the database.
     * Should be use for development and testing, not production.
     */
    public void deleteTermCounters() {
        Set<String> keys = termCounterKeys();
        Transaction t = jedis.multi();
        for(String key: keys) {
            t.del(key);
        }
        t.exec();
    }

    /**
     * Deletes all keys object from the database.
     * Should be use for development and testing, not production.
     */
    public void deleteAllKeys() {
        Set<String> keys = jedis.keys("*");
        Transaction t = jedis.multi();
        for(String key: keys) {
            t.del(key);
        }
        t.exec();
    }

    /**
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Jedis jedis = JedisMaker.make();
        JedisIndex index = new JedisIndex(jedis);

        loadIndex(index);

        Map<String, Integer> map = index.getCounterFaster("the");
        for(Map.Entry<String, Integer> entry: map.entrySet()) {
            System.out.println(entry);
        }

        deleteIndex(index);
        jedis.close();
    }

    /**
     * Store two pages in the index for testing purpose.
     *
     * @param index
     * @throws IOException
     */
    private static void loadIndex(JedisIndex index) throws IOException {
        WikiFetcher wf = new WikiFetcher();

        String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
        Elements paragraphs = wf.readWikipedia(url);
        index.indexPage(url, paragraphs);

        url = "https://en.wikipedia.org/wiki/Programming_language";
        paragraphs = wf.readWikipedia(url);
        index.indexPage(url, paragraphs);
    }

    /**
     * Delete all Keys, termCounters, URLSets
     *
     * @param index
     */
    private static void deleteIndex(JedisIndex index) {
        index.deleteAllKeys();
        //index.deleteTermCounters();
        //index.deleteURLSets();
    }

}
