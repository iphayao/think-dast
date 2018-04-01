package com.phayao.thinkdast;

import java.lang.reflect.Array;
import java.util.*;

public class MyLinkedList<E> implements List<E> {

    private class Node {
        public E data;
        public Node next;

        public Node(E data) {
            this.data = data;
            this.next = null;
        }

        public Node(E data, Node next) {
            this.data = data;
            this.next = next;
        }

        @Override
        public String toString() {
            return "Node(" + data.toString() + ")";
        }
    }

    private int size;
    private Node head;

    public MyLinkedList() {
        head = null;
        size = 0;
    }

    public static void main(String[] args) {
        // run a few simple tests
        List<Integer> mll = new MyLinkedList<Integer>();
        mll.add(1);
        mll.add(2);
        mll.add(3);
        System.out.println(Arrays.toString(mll.toArray()) + " size= " + mll.size());

        mll.remove(new Integer(2));
        System.out.println(Arrays.toString(mll.toArray()) + " size= " + mll.size());

    }

    @Override
    public boolean add(E e) {
        if(head == null) {
            head = new Node(e);
        }
        else {
            Node node;
            // loop until the last node
            for(node = head; node.next != null; node = node.next) {}
            node.next = new Node(e);
        }
        size++;
        return true;
    }

    @Override
    public void add(int index, E element) {
        if(index == 0) {
            head = new Node(element, head);
        }
        else {
            Node node = getNode(index - 1);
            node.next = new Node(element, node.next);
        }
        size++;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean flag = true;
        for(E e : c) {
            flag &= add(e);
        }
        return flag;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
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
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for(Object o : c) {
            if(!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<E> iterator() {
        E[] a = (E[]) toArray();
        return Arrays.asList(a).iterator();
    }

    @Override
    public Object[] toArray() {
        Object[] a = new Object[size];
        int i = 0;
        for(Node node = head; node != null; node = node.next) {
            a[i] = node.data;
            i++;
        }
        return a;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if(index == -1) {
            return false;
        }
        remove(index);
        return true;
    }

    @Override
    public E remove(int index) {
        E old = get(index);
        if(index == 0) {
            head = head.next;
        }
        else {
            Node node = getNode(index - 1);
            node.next = node.next.next;
        }
        size--;
        return old;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean flag = true;
        for(Object o : c) {
            flag &= remove(o);
        }
        return flag;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        head = null;
        size = 0;
    }

    @Override
    public E get(int index) {
        Node node = getNode(index);
        return node.data;
    }

    private Node getNode(int index) {
        if(index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node node = head;
        for(int i = 0; i < index; i++) {
            node = node.next;
        }
        return node;
    }

    @Override
    public E set(int index, E element) {
        Node node = getNode(index);
        E old = node.data;
        node.data = element;
        return old;
    }

    @Override
    public int indexOf(Object o) {
        Node node = head;
        int index = -1;
        for(int i = 0; i < size; i++) {
            if(equals(o, node.data)) {
                return i;
            }
            node = node.next;
        }
        return index;
    }

    @Override
    public int lastIndexOf(Object o) {
        Node node = head;
        int index = -1;
        for(int i = 0; i < size; i++) {
            if(equals(o, node.data)) {
                index = i;
            }
            node = node.next;
        }
        return index;
    }

    private boolean equals(Object o, E data) {
        if(o == null) {
            return data == null;
        }
        return o.equals(data);
    }

    @Override
    public ListIterator<E> listIterator() {
        E[] array = (E[])toArray();
        return Arrays.asList(array).listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        E[] array = (E[])toArray();
        return Arrays.asList(array).listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if(fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }

        int i = 0;
        MyLinkedList<E> list = new MyLinkedList<E>();
        for(Node node = head; node != null; node = node.next) {
            if(i >= fromIndex && i <= toIndex) {
                list.add(node.data);
            }
            i++;
        }
        return list;
    }

}
