package com.phayao.thinkdast;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.*;

public class MyTreeMapTest {
    private MyTreeMap<String, Integer> map;

    @Before
    public void setUp() throws Exception {
        map = new MyTreeMap<String, Integer>();
        map.put("08", 8);
        map.put("03", 3);
        map.put("10", 10);
        map.put("01", 1);
        map.put("06", 6);
        map.put("14", 14);
        map.put("04", 4);
        map.put("07", 7);
        map.put("13", 13);
    }

    /**
     * Test method for {@link MyTreeMap#size()}.
     */
    @Test
    public void testSize() {
        assertThat(map.size(), is(9));
    }

    /**
     * Test method for {@link MyTreeMap#isEmpty()}.
     */
    @Test
    public void testIsEmpty() {
        assertThat(map.isEmpty(), is(false));
        map.clear();
        assertThat(map.isEmpty(), is(true));
    }

    /**
     * Test method for {@link MyTreeMap#containsKey(Object)}.
     */
    @Test
    public void testContainsKey() {
        assertThat(map.containsKey("03"), is(true));
        assertThat(map.containsKey("05"), is(false));
    }

    /**
     * Test method for {@link MyTreeMap#containsValue(Object)}.
     */
    @Test
    public void testContainsValue() {
        assertThat(map.containsValue(3), is(true));
        assertThat(map.containsValue(5), is(false));
    }

    /**
     * Test method for {@link MyTreeMap#get(Object)}.
     */
    @Test
    public void testGet() {
        assertThat(map.get("01"), is(1));
        assertThat(map.get("03"), is(3));
        assertThat(map.get("04"), is(4));
        assertThat(map.get("06"), is(6));
        assertThat(map.get("07"), is(7));
        assertThat(map.get("08"), is(8));
        assertThat(map.get("10"), is(10));
        assertThat(map.get("13"), is(13));
        assertThat(map.get("14"), is(14));

        assertThat(map.get("02"), nullValue());
        assertThat(map.get("05"), nullValue());
    }

    /**
     * Test method for {@link MyTreeMap#put(Object, Object)}.
     */
    @Test
    public void testPut() {
        map.put("06", 66);
        assertThat(map.size(), is(9));
        assertThat(map.get("06"), is(66));

        map.put("05", 5);
        assertThat(map.size(), is(10));
        assertThat(map.get("05"), is(5));
    }

    /**
     * Test method for {@link MyTreeMap#values()}.
     */
    @Test
    public void testValues() {
        Collection<Integer> keySet = map.values();
        assertThat(keySet.size(), is(9));
        assertThat(keySet.contains(3), is(true));
        assertThat(keySet.contains(5), is(false));
    }

    /**
     * Test method for {@link MyTreeMap#putAll(Map)}.
     */
    @Test
    public void testPutAll() {
        Map<String, Integer> m = new HashMap<String, Integer>();
        m.put("02", 2);
        m.put("05", 5);
        m.put("12", 12);
        map.putAll(m);
        assertThat(map.size(), is(12));
    }

    /**
     * Test method for {@link MyTreeMap#remove(Object)}.
     */
    @Test
    public void testRemove() {
    }

    /**
     * Test method for {@link MyTreeMap#clear()}.
     */
    @Test
    public void testClear() {
        map.clear();
        assertThat(map.size(), is(0));
    }

    /**
     * Test method for {@link MyTreeMap#keySet()}.
     */
    @Test
    public void testKeySet() {
        Set<String> keySet = map.keySet();
        assertThat(keySet.size(), is(9));
        assertThat(keySet.contains("03"), is(true));
        assertThat(keySet.contains("05"), is(false));
    }

    /**
     * Test method for {@link MyTreeMap#entrySet()}.
     */
    @Test
    public void testEntrySet() {
    }
}