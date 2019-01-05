package com.github.max0961.util;

import com.github.max0961.model.Graph;
import com.github.max0961.model.Vertex;

import java.util.*;

/**
 * Время работы O(|V|^2 + |E|).
 * Основной цикл выполняется n раз, и каждый раз ищется вершина с минимальным значением
 * {@link com.github.max0961.model.Vertex#distance}.
 * Проверяется каждая смежная верщина с текущей, количество проверок равно m.
 */

public final class SimpleDijkstraSP {
    private SimpleDijkstraSP() {
    }

    public static void compute(Graph graph, String source) {
        compute(graph, graph.vertex(source));
    }

    public static void compute(Graph graph, Vertex source) {
        Set<Vertex> visited = new HashSet<>();
        source.setDistance(0.0);
        for (int i = 0; i < graph.verticesNumber(); ++i) {
            Vertex u = minDistVertex(graph, visited);
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
        }
    }

    private static Vertex minDistVertex(Graph graph, Set<Vertex> visited) {
        Vertex minDistVertex = null;
        double minDist = Double.MAX_VALUE;
        for (Vertex v : graph.getVertices().values()) {
            if (!visited.contains(v) && v.getDistance() <= minDist) {
                minDistVertex = v;
                minDist = v.getDistance();
            }
        }
        return minDistVertex;
    }
}
