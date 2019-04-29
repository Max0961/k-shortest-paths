package com.github.max0961.util;

import com.github.max0961.model.Graph;

import java.util.*;

/**
 * Время работы O(n^2 + m).
 * Основной цикл выполняется n раз, и каждый раз ищется вершина с минимальным значением distance
 * {@link Graph.Vertex#distance}.
 * Проверяется каждая смежная вершина с текущей вершиной, количество проверок равно m.
 */

public final class SimpleDijkstraSP {
    private SimpleDijkstraSP() {
    }

    public static void compute(Graph graph, String root) {
        compute(graph, graph.getVertex(root));
    }

    public static void compute(Graph graph, Graph.Vertex root) {
        Set<Graph.Vertex> visited = new HashSet<>();
        root.setDistance(0.0);
        for (int i = 0; i < graph.verticesNumber(); ++i) {
            Graph.Vertex u = minDistVertex(graph, visited);
            visited.add(u);
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
        }
    }

    private static Graph.Vertex minDistVertex(Graph graph, Set<Graph.Vertex> visited) {
        Graph.Vertex minDistVertex = null;
        double minDist = Double.MAX_VALUE;
        for (Graph.Vertex v : graph.getVertices()) {
            if (!visited.contains(v) && v.getDistance() <= minDist) {
                minDistVertex = v;
                minDist = v.getDistance();
            }
        }
        return minDistVertex;
    }
}
