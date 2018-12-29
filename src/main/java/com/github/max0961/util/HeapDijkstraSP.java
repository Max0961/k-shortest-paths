package com.github.max0961.util;

import com.github.max0961.model.Graph;
import com.github.max0961.model.Vertex;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Время работы O(|V|log|V| + |E|log|V|).
 * Используется бинарная куча.
 * @see BinaryHeap
 */
public class HeapDijkstraSP {
    private static BinaryHeap priorityQueue;
    private static Set<Vertex> visited = new HashSet<>();

    private HeapDijkstraSP() {
    }

    public static void compute(Graph graph, String source) {
        compute(graph, graph.getVertex(source));
    }

    public static void compute(Graph graph, Vertex source) {
        source.setDistance(0);
        priorityQueue = new BinaryHeap();
        priorityQueue.add(source);
        while (!priorityQueue.isEmpty()) {
            Vertex u = (Vertex) priorityQueue.remove();
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
            priorityQueue.add(v);
        }
    }
}
