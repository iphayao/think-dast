package com.phayao.thinkdast;

import com.phayao.thinkdast.Profiler.Timeable;
import org.jfree.data.xy.XYSeries;

import java.util.HashMap;
import java.util.Map;

public class ProfileMapPut {

    public static void main(String[] args) {
        //profileHashMapPut();
        profileMyHashMapPut();
    }

    public static void profileHashMapPut() {
        Timeable timeable = new Timeable() {
            Map<String, Integer> map;

            @Override
            public void setup(int n) {
                map = new HashMap<String, Integer>();
            }

            @Override
            public void timeMe(int n) {
                for(int i = 0; i < n; i++) {
                    map.put(String.format("%01d", i), i);
                }
            }
        };

        int startN = 8000;
        int endMilis = 1000;
        runProfiler("HashMap out", timeable, startN, endMilis);
    }

    public static void profileMyHashMapPut() {
        Timeable timeable = new Timeable() {
            Map<String, Integer> map;

            @Override
            public void setup(int n) {
                map = new MyHashMap<String, Integer>();
            }

            @Override
            public void timeMe(int n) {
                for(int i = 0; i < n; i++) {
                    map.put(String.format("%01d", i), i);
                }
            }
        };

        int startN = 1000;
        int endMilis = 5000;
        runProfiler("MyHashMap out", timeable, startN, endMilis);
    }

    private static void runProfiler(String title, Timeable timeable, int startN, int endMilis) {
        Profiler profiler = new Profiler(title, timeable);
        XYSeries series = profiler.timingLoop(startN, endMilis);
        profiler.plotResults(series);
    }
}
