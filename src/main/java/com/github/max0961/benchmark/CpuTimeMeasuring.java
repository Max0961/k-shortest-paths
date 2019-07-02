package com.github.max0961.benchmark;

import java.lang.management.ManagementFactory;

public final class CpuTimeMeasuring {
    private CpuTimeMeasuring() {
    }

    private static long start;
    private static long end;

    public static void start() {
        start = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
    }

    public static void stop() {
        end = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
    }

    public static double getElapsedTime() {
        return (end - start) / 1e9;
    }
}
