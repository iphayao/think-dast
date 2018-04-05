package com.phayao.thinkdast;


import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class MyTreeMap<K, V> implements Map<K, V> {
    private int size = 0;
    private Node root = null;

    protected class Node {
        public K key;
        public V value;
        public Node left = null;
        public Node right = null;

        /**
         *
         * @param key
         * @param value
         */
        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public Node create(K key, V value) {
            return new Node(key, value);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return findNode(key) != null;
    }

    /**
     * Return the entry that contains the target key, or null if there is none.
     * @param key
     * @return
     */
    private Node findNode(Object key) {
        // some implementations can handle null as a key, but not this one.
        if(key == null) {
            throw new IllegalArgumentException();
        }

        // something to make the compiler happy
        Comparable<? super K> k = (Comparable<? super K>) key;

        // the actual search
        Node node = root;
        while (node != null) {
            int cmp = k.compareTo(node.key);
            if(cmp < 0) {
                node = node.left;
            }
            else if(cmp > 0) {
                node = node.right;
            }
            else {
                return node;
            }
        }

        return null;
    }

    private boolean equals(Object objA, Object objB) {
        if(objA == null) {
            return objB == null;
        }
        return objA.equals(objB);
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueHelper(root, value);
    }

    private boolean containsValueHelper(Node node, Object value) {
        if(node == null) {
            return false;
        }
        if(equals(node.value, value)) {
            return true;
        }
        if(containsValueHelper(node.left, value)) {
            return true;
        }
        if(containsValueHelper(node.right, value)) {
            return true;
        }
        return false;
    }


    @Override
    public V get(Object key) {
        Node node = findNode(key);
        if(node == null) {
            return null;
        }
        return node.value;
    }

    @Override
    public V put(K key, V value) {
        if(key == null) {
            throw new NullPointerException();
        }
        if(root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }
        return putHelper(root, key, value);
    }

    private V putHelper(Node node, K key, V value) {
        Comparable<? super K> k = (Comparable<? super K>) key;
        int cmp = k.compareTo(node.key);

        if(cmp < 0) {
            if(node.left == null) {
                node.left = new Node(key, value);
                size++;
                return null;
            }
            else {
                return putHelper(node.left, key, value);
            }
        }
        if(cmp > 0) {
            if(node.right == null) {
                node.right = new Node(key, value);
                size++;
                return null;
            }
            else {
                return putHelper(node.right, key, value);
            }
        }
        V old = node.value;
        node.value = value;
        return old;
    }

    @Override
    public Collection<V> values() {
        Set<V> set = new HashSet<V>();
        Deque<Node> stack = new LinkedList<Node>();
        stack.push(root);
        while (!stack.isEmpty()) {
            Node node = stack.pop();
            if(node == null) continue;
            set.add(node.value);
            stack.push(node.left);
            stack.push(node.right);
        }
        return set;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for(Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public V remove(Object key) {
        // OPTIONAL TODO:
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    @Override
    public Set<K> keySet() {
        Set<K> set = new LinkedHashSet<K>();
        addInOrder(root, set);
        return set;
    }

    /**
     * Walks the tree and add the keys to 'set'
     * @param node
     * @param set
     */
    private void addInOrder(Node node, Set<K> set) {
        if(node == null) {
            return;
        }
        addInOrder(node.left, set);
        set.add(node.key);
        addInOrder(node.right, set);
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        Map<String, Integer> map = new MyTreeMap<String, Integer>();
        map.put("Word1", 1);
        map.put("Word2", 2);
        Integer value = map.get("Word1");
        System.out.println(value);

        for(String key: map.keySet()) {
            System.out.println(key + ", " + map.get(key));
        }
    }
}
