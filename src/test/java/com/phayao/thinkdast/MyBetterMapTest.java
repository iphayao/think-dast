package com.phayao.thinkdast;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class MyBetterMapTest {
    protected Map<String, Integer> map;

    @Before
    public void setUp() throws Exception {
        map = new MyBetterMap<String, Integer>();
        map.put("One", 1);
        map.put("Two", 2);
        map.put("Three", 3);
        map.put(null, 0);
    }

    /**
     * Test method for {@link MyBetterMap#size()}.
     */
    @Test
    public void testSize() {
        assertThat(map.size(), is(4));
    }

    /**
     * Test method for {@link MyBetterMap#isEmpty()}.
     */
    @Test
    public void testIsEmpty() {
        assertThat(map.isEmpty(), is(false));
        map.clear();
        assertThat(map.isEmpty(), is(true));
    }

    /**
     * Test method for {@link MyBetterMap#containsKey(Object)},
     */
    @Test
    public void testContainsKey() {
        assertThat(map.containsKey("Three"), is(true));
        assertThat(map.containsKey(null), is(true));
        assertThat(map.containsKey("Four"), is(false));
    }

    /**
     * Test method for {@link MyBetterMap#containsValue(Object)}.
     */
    @Test
    public void testContainsValue() {
        assertThat(map.containsValue(3), is(true));
        assertThat(map.containsValue(0),is(true));
        assertThat(map.containsValue(4), is(false));
    }

    /**
     * Test method for {@link MyBetterMap#get(Object)}.
     */
    @Test
    public void testGet() {
        assertThat(map.get("Three"), is(3));
        assertThat(map.get(null), is(0));
        assertThat(map.get("Four"), nullValue());
    }

    /**
     * Test method for {@link MyBetterMap#put(Object, Object)}.
     */
    @Test
    public void testPut() {
        map.put("One", 11);
        assertThat(map.size(), is(4));
        assertThat(map.get("One"), is(11));

        map.put("Five", 5);
        assertThat(map.size(), is(5));
        assertThat(map.get("Five"), is(5));

    }

    /**
     * Test method for {@link MyBetterMap#remove(Object)}.
     */
    @Test
    public void testRemove() {
        map.remove("One");
        assertThat(map.size(), is(3));
        assertThat(map.get("One"), nullValue());
    }

    /**
     * Test method for {@link MyBetterMap#putAll(Map)}.
     */
    @Test
    public void testPutAll() {
        Map<String, Integer> m = new HashMap<String, Integer>();
        m.put("Six", 6);
        m.put("Seven", 7);
        m.put("Eight", 8);
        map.putAll(m);
        assertThat(map.size(), is(7));
    }

    /**
     * Test method for {@link MyBetterMap#clear()}.
     */
    @Test
    public void testClear() {
        map.clear();
        assertThat(map.size(), is(0));
    }

    /**
     * Test method for {@link MyBetterMap#keySet()}.
     */
    @Test
    public void testKeySet() {
        Set<String> keySet = map.keySet();
        assertThat(keySet.size(), is(4));
        assertThat(keySet.contains("Three"), is(true));
        assertThat(keySet.contains(null), is(true));
        assertThat(keySet.contains("Four"), is(false));
    }
}