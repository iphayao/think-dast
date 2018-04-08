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

public class JedisIndexTest {
    private static String url1, url2;
    private Jedis jedis;
    private JedisIndex index;

    @Before
    public void setUp() throws Exception {
        jedis = JedisMaker.make();
        index = new JedisIndex(jedis);

        loadIndex(index);
    }

    /**
     * Loads the index with two pages read from files.
     *
     * @param index
     * @throws IOException
     */
    private static void loadIndex(JedisIndex index) throws IOException {
        WikiFetcher wf = new WikiFetcher();

        url1 = "https://en.wikipedia.org/wiki/Java_(programming_language)";
        Elements paragraphs = wf.readWikipedia(url1);
        index.indexPage(url1, paragraphs);

        url2 = "https://en.wikipedia.org/wiki/Programming_language";
        paragraphs = wf.readWikipedia(url2);
        index.indexPage(url2, paragraphs);
    }

    @After
    public void tearDown() throws Exception {
        // Delete all keys in Redis
        index.deleteAllKeys();
        jedis.close();
    }

    /**
     * Test method for {@link JedisIndex#getCount(String, String)}.
     */
    @Test
    public void testGetCounts() {
        Map<String, Integer> map = index.getCounts("the");
        assertThat(map.get(url1), is(339));
        assertThat(map.get(url2), is(264));
    }

}