package com.phayao.thinkdast;

import java.util.UUID;

public class MyTreeMapExample {
    public static void main(String[] args) {
        int n = 16384;
        System.out.println("\nTesting MyTreeMap with random string");
        runRandomString(n);
        System.out.println("\nTesting MyTreeMap with timestramps");
        putTimestamps(n);

    }

    /**
     *
     * @param n
     */
    private static void runRandomString(int n) {
        MyTreeMap<String, Integer> map = new MyTreeMap<String, Integer>();
        final long startTime = System.currentTimeMillis();
        for(int i = 0; i < n; i++) {
            String uuid = UUID.randomUUID().toString();
            map.put(uuid, 0);
        }

        final long elapsed = System.currentTimeMillis() - startTime;
        printResult(map, elapsed, - 1);

    }

    /**
     *
     * @param n
     */
    private static void putTimestamps(int n) {
        MyTreeMap<String, Integer> map = new MyTreeMap<String, Integer>();
        final long startTime = System.currentTimeMillis();
        for(int i = 0; i < n; i++) {
            String timestamp = Long.toString(System.nanoTime());
            map.put(timestamp, 0);
        }

        final long elapsed = System.currentTimeMillis() - startTime;
        printResult(map, elapsed, - 1);
    }

    private static void printResult(MyTreeMap<String, Integer> map, long elapsed, int height) {
        System.out.println("   Time in milliseconds = " + (elapsed));
        System.out.println("   Final size of MyTreeMap = " + map.size());
        System.out.println("   log base 2 of size of MyTreeMap = " + Math.log(map.size())/ Math.log(2));
        System.out.println("   Final height of MyTreeMap = " + height);
    }

}
