package com.github.max0961;

import com.github.max0961.model.Graph;
import com.github.max0961.model.Path;
import com.github.max0961.util.TimeMeasuring;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Objects;

public class Application {
    static final int VERTEX_NUMBER_PER_STEP = 10000;
    static final int EDGES_NUMBER_PER_STEP = 20000;
    static final int STEP_NUMBER = 1;
    static final int OUTER_LOOP = 1;

    static final String SOURCE = "0";
    static final String TARGET = "1";
    static final int K = 5;

    static final String FILE_NAME = "d:/test.txt";

    public static void main(String[] args) {
        TimeMeasuring.start();
        //Graph graph = new Graph(VERTEX_NUMBER_PER_STEP);
        //graph.generateRandomDirectedGraph(EDGES_NUMBER_PER_STEP);
        //graph.saveToFile(FILE_NAME);
        Graph graph = new Graph(FILE_NAME);
        TimeMeasuring.stop();
        System.out.println(String.format("Время создания графа с |V| = %d и |E| = %d – %fс",
                graph.verticesNumber(), graph.edgeNumber(), TimeMeasuring.getElapsedTime()));

        TimeMeasuring.start();
        Path path = new Path(graph, SOURCE, TARGET);
        TimeMeasuring.stop();
        System.out.println(path);
        System.out.print(TimeMeasuring.getElapsedTime());

        //saveResult(FILE_NAME, process());
    }

    private static double[][] processYenKsp() {
        System.out.println("Тестрирование времени работы алгоритма Йена.");
        double[][] values = new double[STEP_NUMBER][3];

        for (int i = 0; i < OUTER_LOOP; ++i) {
            for (int j = 1; j <= STEP_NUMBER; ++j) {
                Graph graph = new Graph(j * VERTEX_NUMBER_PER_STEP);
                graph.generateRandomDirectedGraph(j * EDGES_NUMBER_PER_STEP);

                TimeMeasuring.start();
                //YenKSP yenKSP = new YenKSP(graph, SOURCE, TARGET, K);
                Path path = new Path(graph, SOURCE, TARGET);
                TimeMeasuring.stop();

                values[j - 1][0] = j * VERTEX_NUMBER_PER_STEP;
                values[j - 1][1] = j * EDGES_NUMBER_PER_STEP;
                values[j - 1][2] = TimeMeasuring.getElapsedTime();

                System.out.println(path);
                System.out.println((String.format("Выполнение %d/%d", (i + 1) * j, OUTER_LOOP * STEP_NUMBER)));
            }
        }
        System.out.println("Тестирование времени завершено.");
        return values;
    }

    private static void saveResult(String filename, double[][] values) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filename));
            for (double[] v1 : values) {
                for (Double v : v1) {
                    writer.write(String.format("%f\n", v));
                }
                writer.write("\n");
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
