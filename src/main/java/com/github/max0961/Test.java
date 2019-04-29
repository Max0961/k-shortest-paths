package com.github.max0961;

import com.github.max0961.ksp.Eppstein.EppsteinKSP;
import com.github.max0961.ksp.Eppstein.SimpleEppsteinKSP;
import com.github.max0961.model.Graph;
import com.github.max0961.util.TimeMeasuring;

public class Test {
    public static void main(String[] args) {
        System.out.println(doSample("D:\\Графы\\dense graph.txt"));
    }

    private static String doSample(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        //Graph graph = new Graph(fileName);
        Graph graph = new Graph();
        graph.generateRandomGraph(true, true, 100, 1f);
        graph.saveToFile(fileName);
        stringBuilder.append(graph);
        TimeMeasuring.start();
        stringBuilder.append(new SimpleEppsteinKSP(graph, "1", "2", 100));
        TimeMeasuring.stop();
        stringBuilder.append(String.format("Выполнение за %fc", TimeMeasuring.getElapsedTime()));
        return stringBuilder.toString();
    }
}
