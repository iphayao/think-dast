package com.phayao.thinkdast;

import java.util.*;

public class MyBetterMap<K, V> implements Map<K, V> {
    // MyBetterMap use collection of MyLinearMap
    protected List<MyLinearMap<K, V>> maps;

    /**
     * Initialize the map with 2 sub-maps.
     */
    public MyBetterMap() {
        makeMaps(2);
    }

    /**
     * Makes a collection of 'k' MyLinearMap
     * @param k
     */
    private void makeMaps(int k) {
        maps = new ArrayList<MyLinearMap<K, V>>(k);
        for(int i = 0; i < k; i++) {
            maps.add(new MyLinearMap<K, V>());
        }
    }

    private MyLinearMap<K, V> chooseMap(Object key) {
        int index = ((key == null) ? 0 : Math.abs(key.hashCode()) % maps.size());
        return maps.get(index);
    }


    @Override
    public int size() {
        // add up the size of the sub-map
        int total = 0;
        for(MyLinearMap<K, V> map: maps) {
            total += map.size();
        }
        return total;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        MyLinearMap<K, V> map = chooseMap(key);
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        // TODO:
        for(MyLinearMap<K, V> map: maps) {
            if(map.containsValue(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        MyLinearMap<K, V> map = chooseMap(key);
        return map.get(key);
    }

    @Override
    public V put(K key, V value) {
        MyLinearMap<K, V> map = chooseMap(key);
        return map.put(key, value);
    }

    @Override
    public V remove(Object key) {
        MyLinearMap<K, V> map = chooseMap(key);
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for(Map.Entry<? extends K, ? extends V> entry: m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        // clear the sub-maps
        for(int i = 0; i < maps.size(); i++) {
            maps.get(i).clear();
        }
    }

    @Override
    public Set<K> keySet() {
        // add up the keySets from the sub-maps
        Set<K> set = new HashSet<K>();
        for(MyLinearMap<K, V> map: maps) {
            set.addAll(map.keySet());
        }
        return set;
    }

    @Override
    public Collection<V> values() {
        // add up the valueSets from teh sub-maps
        Set<V> set = new HashSet<V>();
        for(MyLinearMap<K, V> map: maps) {
            set.addAll(map.values());
        }
        return set;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        Map<String, Integer> map = new MyBetterMap<String, Integer>();
        map.put("Word1", 1);
        map.put("Word2", 2);
        Integer value = map.get("Word1");
        System.out.println(value);

        for(String key: map.keySet()) {
            System.out.println(key + ", " + map.get(key));
        }
    }
}
