package com.github.max0961;

import com.github.max0961.ksp.YenKSP;
import com.github.max0961.model.Graph;
import com.github.max0961.model.Path;
import com.github.max0961.util.TimeMeasuring;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Objects;

public class Application {
    static final int VERTEX_NUMBER_PER_STEP = 2000;
    static final int EDGE_NUMBER_PER_STEP = 0;
    static final int STEP_NUMBER = 10;
    static final int OUTER_LOOP = 5;

    static final String SOURCE = "1";
    static final String TARGET = "100";
    static final int K = 10;

    static final String GRAPH_FILE_NAME = "d:/test.txt";
    static final String RESULT_FILE_NAME = "d:/result.txt";

    public static void main(String[] args) {
        System.out.print(sample(GRAPH_FILE_NAME));
        Process processObject = new Process(SOURCE, TARGET, K);
        //saveResult(RESULT_FILE_NAME, process(processObject, OUTER_LOOP, STEP_NUMBER, VERTEX_NUMBER_PER_STEP));
    }

    private static String sample(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        Graph graph = new Graph(fileName);
//        Graph graph = new Graph(50000);
//        graph.generateRandomDirectedGraph(500000);
//        graph.saveToFile(fileName);
        stringBuilder.append(graph);
        TimeMeasuring.start();
        stringBuilder.append(new YenKSP(graph, "1", "100", 100));
        //stringBuilder.append(new Path(graph, "1", "100"));
        TimeMeasuring.stop();
        stringBuilder.append(String.format("Выполнение %f", TimeMeasuring.getElapsedTime()));
        return stringBuilder.toString();
    }

    public static double[][] process(Process processObject, int outerLoop, int stepNumber, int vertexNumberPerStep) {
        System.out.println(processObject.getName());
        System.out.println("Тестрирование времени выполнения. ");
        double[][] values = new double[stepNumber][3];
        int counter = 0;
        for (int i = 0; i < outerLoop; ++i) {
            for (int j = 1; j <= stepNumber; ++j) {
                counter++;
                System.out.println((String.format("Выполнение %d/%d", counter, outerLoop * stepNumber)));
                Graph graph = new Graph(j * vertexNumberPerStep);
                graph.generateRandomDirectedGraph((int) Math.round(Math.pow(j * vertexNumberPerStep, 1.25)));

                TimeMeasuring.start();
                processObject.run(graph);
                TimeMeasuring.stop();
                graph = null;
                System.gc();
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
