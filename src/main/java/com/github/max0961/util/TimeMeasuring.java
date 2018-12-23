package com.github.max0961.util;

public final class TimeMeasuring {
    private TimeMeasuring() {
    }

    private static long start;
    private static long end;

    public static void start() {
        start = System.nanoTime();
    }

    public static void end() {
        end = System.nanoTime();
    }

    public static double getElapsedTime() {
        return (end - start) / (double) 1000000000;
    }
}
