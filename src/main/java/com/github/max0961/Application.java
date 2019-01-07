package com.github.max0961;

import com.github.max0961.ksp.YenKSP;
import com.github.max0961.model.Graph;
import com.github.max0961.model.Path;
import com.github.max0961.util.TimeMeasuring;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Objects;

public class Application {
    static final int VERTEX_NUMBER_PER_STEP = 5000;
    static final int EDGE_NUMBER_PER_STEP = 0;
    static final int STEP_NUMBER = 10;
    static final int OUTER_LOOP = 10;

    static final String SOURCE = "1";
    static final String TARGET = "100";
    static final int K = 100;

    static final String GRAPH_FILE_NAME = "d:/test.txt";
    static final String RESULT_FILE_NAME = "d:/result.txt";

    public static void main(String[] args) {
        //.out.println(sample(GRAPH_FILE_NAME));
        saveResult(RESULT_FILE_NAME, processWithKIncreasing(OUTER_LOOP, STEP_NUMBER, VERTEX_NUMBER_PER_STEP));
    }

    private static String sample(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
//        Graph graph = new Graph(fileName);
        Graph graph = new Graph(20);
        graph.generateRandomDirectedGraph(60);
        graph.saveToFile(fileName);
        stringBuilder.append(graph);
        TimeMeasuring.start();
        stringBuilder.append(new YenKSP(graph, "1", "2", 5));
        //stringBuilder.append(new Path(graph, "1", "100"));
        TimeMeasuring.stop();
//        graph = null;
//        System.gc();
        stringBuilder.append(String.format("Выполнение за %fc", TimeMeasuring.getElapsedTime()));
        return stringBuilder.toString();
    }

    public static double[][] processWithGraphIncreasing(int outerLoop, int stepNumber, int vertexNumberPerStep) {
        System.out.println("Тестрирование времени выполнения.");
        double[][] values = new double[stepNumber][3];
        int counter = 0;
        for (int i = 0; i < outerLoop; ++i) {
            for (int j = 1; j <= stepNumber; ++j) {
                counter++;
                System.out.println((String.format("Выполнение %d/%d", counter, outerLoop * stepNumber)));
                Graph graph = new Graph(j * vertexNumberPerStep);
                graph.generateRandomDirectedGraph((int) Math.round(Math.pow(j * vertexNumberPerStep, 1.25)));

                TimeMeasuring.start();
                //YenKSP yenKSP = new YenKSP(graph, SOURCE, TARGET, K);
                Path path = new Path(graph, SOURCE, TARGET);
                TimeMeasuring.stop();

                values[j - 1][2] += TimeMeasuring.getElapsedTime();
            }
        }
        for (int j = 1; j <= stepNumber; ++j) {
            values[j - 1][0] = j * vertexNumberPerStep;
            values[j - 1][1] = (int) Math.round(Math.pow(j * vertexNumberPerStep, 1.25));
            values[j - 1][2] /= outerLoop;
        }
        System.out.println("Тестирование времени завершено.");
        return values;
    }

    public static double[][] processWithKIncreasing(int outerLoop, int stepNumber, int vertexNumberPerStep) {
        System.out.println("Тестрирование времени выполнения.");
        double[][] values = new double[stepNumber][3];
        int counter = 0;
        for (int i = 0; i < outerLoop; ++i) {
            for (int j = 1; j <= stepNumber; ++j) {
                counter++;
                System.out.println((String.format("Выполнение %d/%d", counter, outerLoop * stepNumber)));
                Graph graph = new Graph(1 * vertexNumberPerStep);
                graph.generateRandomDirectedGraph((int) Math.round(Math.pow(1 * vertexNumberPerStep, 1.25)));

                TimeMeasuring.start();
                YenKSP yenKSP = new YenKSP(graph, SOURCE, TARGET, j * K);
                TimeMeasuring.stop();

                values[j - 1][2] += TimeMeasuring.getElapsedTime();
            }
        }
        for (int j = 1; j <= stepNumber; ++j) {
            values[j - 1][0] = j * K;
            values[j - 1][2] /= outerLoop;
        }
        System.out.println("Тестирование времени завершено.");
        return values;
    }

    public static void saveResult(String fileName, double[][] values) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
            for (double[] value : values) {
                StringBuilder string = new StringBuilder();
                string.append((int) value[0]).append("\t");
                string.append((int) value[1]).append("\t");
                string.append(String.format("%f", value[2])).append("\n");
                writer.write(string.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                Objects.requireNonNull(writer).close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
