package com.github.max0961.benchmark;

public final class TimeMeasuring {
    private TimeMeasuring() {
    }

    private static long start;
    private static long end;

    public static void start() {
        start = System.nanoTime();
    }

    public static void stop() {
        end = System.nanoTime();
    }

    public static double getElapsedTime() {
        return (end - start) / (double) 1000000000;
    }
}
