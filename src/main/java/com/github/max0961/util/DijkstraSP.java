package com.github.max0961.util;

import com.github.max0961.model.Graph;
import com.github.max0961.model.Vertex;

import java.util.*;

/**
 * The running time of the algorithm is O(n^2 + m).
 * The main loop is executed n times, in each of them n operations are spent on finding the minimum.
 * The cycles of the neighbors are spent the number of the edges m.
 */

public final class DijkstraSP {
    private static BinaryHeap priorityQueue;
    private static Set<Vertex> visited = new HashSet<>();

    private DijkstraSP() {
    }

    public static void compute(Graph graph, String source) {
        compute(graph, graph.getVertex(source));
    }

    public static void compute(Graph graph, Vertex source) {
        source.setDistance(0);
        priorityQueue = new BinaryHeap(graph.getVertices().values());
        while (!priorityQueue.isEmpty()) {
            Vertex u = (Vertex)priorityQueue.remove();
            visited.add(u);
            for (Map.Entry<Vertex, Double> entry : graph.getVertex(u.label()).adjacency().entrySet()) {
                Vertex v = entry.getKey();
                double w = entry.getValue();
                relax(u, v, w);
            }
        }
    }

    private static void relax(Vertex u, Vertex v, double weight) {
        if (v.getDistance() > u.getDistance() + weight) {
            v.setDistance(u.getDistance() + weight);
            v.setPredecessor(u);
            priorityQueue.bubbleUp(v.label(), v);
        }
    }

    private static Vertex minDistVertex(Graph graph, Set<Vertex> visited) {
        String minDistLabel = "";
        double minDist = Double.MAX_VALUE;
        Iterator iterator = graph.getVertices().values().iterator();
        while (iterator.hasNext()) {
            Vertex v = (Vertex) iterator;
            if (!visited.contains(v) && v.getDistance() < minDist) {
                minDistLabel = v.label();
                minDist = v.getDistance();
            }
        }
        return graph.getVertex(minDistLabel);
    }
}
