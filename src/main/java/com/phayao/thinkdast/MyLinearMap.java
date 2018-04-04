package com.phayao.thinkdast;

import java.util.*;

public class MyLinearMap<K, V> implements Map<K, V> {
    private List<Entry> entries = new ArrayList<Entry>();

    public class Entry implements Map.Entry<K, V> {
        private K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            this.value = value;
            return this.value;
        }
    }

    @Override
    public int size() {
        return entries.size();
    }

    @Override
    public boolean isEmpty() {
        return entries.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return findEntry(key) != null;
    }

    /**
     * Return the entry that contains the target key, or null there is none.
     *
     * @param key
     * @return
     */
    private Entry findEntry(Object key) {
        for(Entry entry: entries) {
            if(equals(key, entry.getKey())) {
                return  entry;
            }
        }
        return null;
    }

    @Override
    public boolean containsValue(Object value) {
        for(Map.Entry<K, V> entry: entries) {
            if(equals(value, entry.getValue())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Compares two keys or two values, handing null correctly
     *
     * @param object
     * @param value
     * @return
     */
    private boolean equals(Object object, Object value) {
        if(object == null) {
            return value == null;
        }
        return object.equals(value);
    }

    @Override
    public V get(Object key) {
        Entry entry = findEntry(key);
        if(entry == null) {
            return null;
        }
        return entry.getValue();
    }

    @Override
    public V put(K key, V value) {
        Entry entry = findEntry(key);
        if(entry == null) {
            entries.add(new Entry(key, value));
            return null;
        }
        V ov = entry.getValue();
        entry.setValue(value);
        return ov;
    }

    @Override
    public V remove(Object key) {
        Entry entry = findEntry(key);
        if(entry == null) {
            return  null;
        }
        V ov = entry.getValue();
        entries.remove(entry);
        return ov;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for(Map.Entry<? extends K, ? extends V> entry: m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        entries.clear();
    }

    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<K>();
        for(Entry entry: entries) {
            set.add(entry.getKey());
        }
        return set;
    }

    @Override
    public Collection<V> values() {
        Set<V> set = new HashSet<V>();
        for(Entry entry: entries) {
            set.add(entry.getValue());
        }
        return set;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        Map<String, Integer> map = new MyLinearMap<String, Integer>();
        map.put("World1", 1);
        map.put("World2", 2);
        Integer value = map.get("World1");
        System.out.println(value);

        for(String key: map.keySet()) {
            System.out.println(key + ", " + map.get(key));
        }
    }
}
