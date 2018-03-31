package com.phayao.thinkdast;

import java.util.*;

public class MyArrayList<T> implements List<T> {
    int size;           // keeps track of the number of elements
    private T[] array;  // stores the elements

    @SuppressWarnings("unchecked")
    public MyArrayList() {
        // you can't instantiate an array of T[] but you can instantiate an
        // array of Object and then typecast it.
        // detail at http://www.ibm.com/developerworks/java/library/j-jtp01255/index.html
        array = (T[]) new Object[10];
        size = 0;
    }

    public static void main(String[] args) {
        MyArrayList<Integer> mal = new MyArrayList<Integer>();
        mal.add(1);
        mal.add(2);
        mal.add(3);
        System.out.println(Arrays.toString(mal.toArray()) + " size = " + mal.size);

        mal.remove(new Integer(2));
        System.out.println(Arrays.toString(mal.toArray()) + " size = " + mal.size);
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
    public Iterator<T> iterator() {
        // make a copy of the array
        T[] copy = Arrays.copyOf(array, size);
        // make a list and return an iterator
        return Arrays.asList(copy).iterator();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(array, size);
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean add(T t) {
        if(size >= array.length) {
            T[] newArray = (T[]) new Object[array.length * 2];
            for(int i = 0; i < size; i++) {
                newArray[i] = array[i];
            }
            array = newArray;
        }
        array[size] = t;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if(index == -1)
            return false;
        remove(index);
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for(Object e : c) {
            if(!contains(e)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean flag = true;
        for(T e : c) {
            flag &= add(e);
        }
        return flag;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
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
        size = 0;
    }

    @Override
    public T get(int index) {
        if(index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return array[index];
    }

    @Override
    public T set(int index, T element) {
        if(index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        T previous = array[index];
        array[index] = element;
        return previous;
    }

    @Override
    public void add(int index, T element) {
        if(index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        // add the element to get the resizing
        add(element);

        // shift the elements
        for(int i = size; i > index; i--) {
            array[i] = array[i - 1];
        }
        // put the new one in the right place
        array[index] = element;
    }

    @Override
    public T remove(int index) {
        if(index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        T previous = array[index];
        for(int i = index; i < size; i++) {
            array[i] = array[i + 1];
        }
        //array[size] = null;
        size--;
        return previous;
    }

    @Override
    public int indexOf(Object o) {
        for(int i = 0; i < size; i++) {
            if(equals(o, array[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for(int i = size - 1; i >= 0; i--) {
            if(equals(o, array[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public ListIterator<T> listIterator() {
        // make copy of array then return list iterator
        T[] copy = Arrays.copyOf(array, size);
        return Arrays.asList(copy).listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        // make copy of array then return index of list iterator
        T[] copy = Arrays.copyOf(array, size);
        return Arrays.asList(copy).listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        if(fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }
        T[] copy = Arrays.copyOfRange(array, fromIndex, toIndex);
        return Arrays.asList(copy);
    }

    private boolean equals(Object target, T element) {
        if(target == null) {
            return element == null;
        }
        else {
            return target.equals(element);
        }
    }
}
