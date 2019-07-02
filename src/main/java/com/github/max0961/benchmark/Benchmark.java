package com.github.max0961.benchmark;

import com.github.max0961.model.Path;
import com.github.max0961.model.ksp.GeneralizedDijkstra;
import com.github.max0961.model.ksp.KSP;
import com.github.max0961.model.Graph;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Date;

public class Benchmark {
    public static void main(String[] args) {
        KSP ksp;
        String dir;

//        ksp = new YenKSP();
//        dir = "d:\\results\\" + ksp.getClass().getSimpleName();
//        arrange(ksp, dir);
//
//        ksp = new SimpleEppsteinKSP();
//        dir = "d:\\results\\" + ksp.getClass().getSimpleName();
//        arrange(ksp, dir);

        ksp = new GeneralizedDijkstra();
        dir = "d:\\results\\" + ksp.getClass().getSimpleName();
        arrange(ksp, dir);
    }

    private static void arrange(KSP ksp, String dir) {
        int ol = 10;
        int il = 10;
        //warmUp(ksp);

//        // Меняется K со 100 до 1000
//        saveResult(dir + "\\k1.txt",
//                testTimes(ksp, ol, il, 0.2f, 1000, 100, false, false, true, true));
//        saveResult(dir + "\\k2.txt",
//                testTimes(ksp, ol, il, 0.2f, 1500, 100, false, false, true, true));
//        saveResult(dir + "\\k3.txt",
//                testTimesKSP(ksp, ol, il, 0.2f, 2000, 100, false, false, true, true));

//        // Меняется |V|
//        saveResult(dir + "\\v1.txt",
//                testTimes(ksp, ol, il, 0.2f, 100, 500, false, true, false, true));
//        saveResult(dir + "\\v2.txt",
//                testTimes(ksp, ol, il, 0.2f, 150, 500, false, true, false, true));
//        saveResult(dir + "\\v3.txt",
//                testTimes(ksp, ol, il, 0.2f, 200, 500, false, true, false, true));
//
//        // Меняется |V|
//        saveResult(dir + "\\v1nd.txt",
//                testTimes(ksp, ol, il, 0.2f, 100, 500, false, true, false, false));
//
//        // Меняется плотность с 10% до 100%
//        saveResult(dir + "\\d1.txt",
//                testTimes(ksp, ol, il, 0.1f, 1000, 500, true, false, false, true));
        saveResult(dir + "\\dijkstra1.txt",
                testTimesDijkstra(ol, il, 0.2f, 1000, 100, false, false, true, true));
        saveResult(dir + "\\dijkstra2.txt",
                testTimesDijkstra(ol, il, 0.2f, 1500, 100, false, false, true, true));
        saveResult(dir + "\\dijkstra3.txt",
                testTimesDijkstra(ol, il, 0.2f, 2000, 100, false, false, true, true));
    }

    private static void warmUp(KSP ksp) {
        Graph graph = new Graph();
        for (int j = 1; j <= 1500; ++j) {
            graph.generateRandomGraph(false, true, 5, 4);
            CpuTimeMeasuring.start();
            ksp.findKsp(2, graph, "1", "5");
            CpuTimeMeasuring.stop();
        }
    }

    private static double[][] testTimesDijkstra(int ol, int il, float d, int v, int k,
                                                boolean ei, boolean vi, boolean ki, boolean directed) {
        System.out.println("Тестрирование времени выполнения...");
        double[][] values = new double[il][4];
        Graph graph = new Graph();
        for (int j = 1; j <= 1500; ++j) {
            graph.generateRandomGraph(false, true, 5, 4);
            CpuTimeMeasuring.start();
            new Path(graph, "1", "5");
            CpuTimeMeasuring.stop();
        }

        for (int i = 0; i < ol; ++i) {
            for (int j = 1; j <= il; ++j) {
                int K = k * (ki ? j : 1);
                int vertexNumber = v * (vi ? j : 1);
                float density = (ei ? (float) j / il * 10 : 1) * d;
                values[j - 1][0] = K;
                values[j - 1][1] = vertexNumber;
                values[j - 1][2] = density;

                graph.generateRandomGraph(directed, true, vertexNumber, density);
                CpuTimeMeasuring.start();
                for (int w = 0; w < K; ++w) {
                    new Path(graph, "1", Integer.toString(vertexNumber));
                }
                CpuTimeMeasuring.stop();
                values[j - 1][3] += CpuTimeMeasuring.getElapsedTime();
            }
        }
        for (int j = 1; j <= il; ++j) {
            values[j - 1][3] /= ol;
        }
        System.out.println("Тестирование завершено. " + new Date().toString());
        return values;
    }

    private static double[][] testTimesKSP(KSP ksp, int ol, int il, float d, int v, int k,
                                           boolean ei, boolean vi, boolean ki, boolean directed) {
        System.out.println("Тестрирование времени выполнения...");
        double[][] values = new double[il][4];

        Graph graph = new Graph();
        for (int i = 0; i < ol; ++i) {
            for (int j = 1; j <= il; ++j) {
                int K = k * (ki ? j : 1);
                int vertexNumber = v * (vi ? j : 1);
                float density = (ei ? (float) j / il * 10 : 1) * d;
                values[j - 1][0] = K;
                values[j - 1][1] = vertexNumber;
                values[j - 1][2] = density;

                graph.generateRandomGraph(directed, true, vertexNumber, density);
                CpuTimeMeasuring.start();
                ksp.findKsp(K, graph, "1", Integer.toString(vertexNumber));
                CpuTimeMeasuring.stop();
                values[j - 1][3] += CpuTimeMeasuring.getElapsedTime();
            }
        }
        for (int j = 1; j <= il; ++j) {
            values[j - 1][3] /= ol;
        }
        System.out.println("Тестирование завершено. " + new Date().toString());
        return values;
    }

    private static void saveResult(String fileName, double[][] values) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            for (double[] value : values) {
                StringBuilder string = new StringBuilder();
                string.append((int) value[0]).append("\t");
                string.append((int) value[1]).append("\t");
                string.append(String.format("%f", value[2])).append("\t");
                string.append(String.format("%f", value[3])).append("\n");
                writer.write(string.toString());
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
