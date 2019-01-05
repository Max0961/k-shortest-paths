package com.github.max0961.util;

import com.github.max0961.model.Graph;
import com.github.max0961.model.Vertex;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Время работы O((|V| + |E|)log|V|).
 * Используется бинарная куча.
 * Вершины добавляются в очередь по мере необходимости.
 *
 * @see BinaryHeap
 */
public final class HeapDijkstraSP {
    private static BinaryHeap<Vertex> priorityQueue;

    private HeapDijkstraSP() {
    }

    public static void compute(Graph graph, String source) {
        compute(graph, graph.vertex(source));
    }

    public static void compute(Graph graph, Vertex source) {
        Set<Vertex> visited = new HashSet<>();;
        init();
        source.setDistance(0.0);
        priorityQueue.add(source);
        while (!priorityQueue.isEmpty()) {
            Vertex u = priorityQueue.remove();
            visited.add(u);
            for (Map.Entry<Vertex, Double> entry : u.getAdjacency().entrySet()) {
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

    private static void init() {
        priorityQueue = new BinaryHeap<>();
    }
}
