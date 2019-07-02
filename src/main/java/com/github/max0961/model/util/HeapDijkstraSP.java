package com.github.max0961.model.util;

import com.github.max0961.model.Graph;

import java.util.Map;

/**
 * Временная сложность O((n + m)log(n)).
 * Используется бинарная куча.
 * Вершины добавляются в очередь по мере необходимости.
 *
 * @see BinaryHeap
 */
public final class HeapDijkstraSP {
    private static BinaryHeap<Graph.Vertex> priorityQueue;

    private HeapDijkstraSP() {
    }

    public static void compute(Graph graph, String root) {
        compute(graph.getVertex(root));
    }

    public static void compute(Graph.Vertex root) {
        priorityQueue = new BinaryHeap<>();
        root.setDistance(0.0);
        priorityQueue.add(root);
        while (!priorityQueue.isEmpty()) {
            Graph.Vertex u = priorityQueue.remove();
            for (Map.Entry<Graph.Vertex, Double> entry : u.getAdjacencyMap().entrySet()) {
                Graph.Vertex v = entry.getKey();
                double w = entry.getValue();
                relax(u, v, w);
            }
        }
    }

    private static void relax(Graph.Vertex u, Graph.Vertex v, double weight) {
        if (v.getDistance() > u.getDistance() + weight) {
            v.setDistance(u.getDistance() + weight);
            v.setPredecessor(u);
            priorityQueue.add(v);
        }
    }
}
