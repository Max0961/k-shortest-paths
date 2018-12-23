package com.github.max0961;

import com.github.max0961.control.SimpleDijkstra;
import com.github.max0961.model.Graph;
import com.github.max0961.util.TimeMeasuring;

public class Application {
    static final int VERTEX_NUMBER = 5000;
    static final int EDGES_NUMBER = 10000;

    public static void main(String[] args) {
        TimeMeasuring.start();
        Graph graph = new Graph(VERTEX_NUMBER);
        graph.generateRandomDirectedGraph(EDGES_NUMBER);
        TimeMeasuring.end();
        System.out.println(TimeMeasuring.getElapsedTime());
        TimeMeasuring.start();
        SimpleDijkstra dijkstra = new SimpleDijkstra(graph, 0);
        TimeMeasuring.end();
        System.out.println(TimeMeasuring.getElapsedTime());

        //System.out.println(graph);
        System.out.println(graph.printPath(0, 9));
    }
}
