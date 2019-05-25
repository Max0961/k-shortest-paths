package com.github.max0961;

import com.github.max0961.model.ksp.KSP;
import com.github.max0961.model.ksp.YenKSP;
import com.github.max0961.model.Graph;
import com.github.max0961.model.ksp.util.TimeMeasuring;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class Test {
    private static final String path = "d:\\test_result.txt";

    public static void main(String[] args) {
        KSP ksp = new YenKSP();
        saveResult(path, testTimes(ksp, 1, 1, 10, 1000, 100, true, false, false));
    }

    private static void warmUp(KSP ksp) {
        for (int j = 1; j <= 10; ++j) {
            Graph graph = new Graph();
            graph.generateRandomGraph(true, true, 5, 4);
            TimeMeasuring.start();
            ksp.findKsp(2, graph, "1", "s");
            TimeMeasuring.stop();
        }
    }

    private static double[][] testTimes(KSP ksp, int ol, int il, int e, int v, int k, boolean ei, boolean vi, boolean ki) {
        System.out.println("Тестрирование времени выполнения...");
        double[][] values = new double[il][4];
        warmUp(ksp);

        for (int i = 0; i < ol; ++i) {
            for (int j = 1; j <= il; ++j) {
                Graph graph = new Graph();

                int K = k * (vi ? j : 1);
                int vertexNumber = v * (vi ? j : 1);
                float density = (float) (ei ? j : 1) / e;
                values[j - 1][0] = K;
                values[j - 1][1] = vertexNumber;
                values[j - 1][2] = density;

                graph.generateRandomGraph(true, true, vertexNumber, density);
                TimeMeasuring.start();
                ksp.findKsp(K, graph, "1", "2");
                TimeMeasuring.stop();
                System.out.println(ksp);
                values[j - 1][3] += TimeMeasuring.getElapsedTime();
            }
        }
        for (int j = 1; j <= il; ++j) {
            values[j - 1][3] /= ol;
        }
        System.out.println("Тестирование завершено.");
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
