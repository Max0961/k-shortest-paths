package com.github.max0961.util;

import com.github.max0961.model.Graph;
import com.github.max0961.model.Vertex;

import java.util.Map;

/**
 * Время работы O(|V| x |E|)
 */
public final class BellmanFordSP {

    private BellmanFordSP() {}

    public static void compute(Graph graph, String source) {
        compute(graph, graph.getVertex(source));
    }

    public static boolean compute(Graph graph, Vertex source) {
        source.setDistance(0);
        for (int i = 0; i < graph.verticesNumber() - 1; ++i) {
            for (String label : graph.getVertices().keySet()) {
                Vertex u = graph.getVertex(label);
                for (Map.Entry<Vertex, Double> entry : graph.getVertex(u.label()).adjacency().entrySet()) {
                    Vertex v = entry.getKey();
                    double w = entry.getValue();
                    relax(u, v, w);
                }
            }
        }
        for (String label : graph.getVertices().keySet()) {
            Vertex u = graph.getVertex(label);
            for (Map.Entry<Vertex, Double> entry : graph.getVertex(u.label()).adjacency().entrySet()) {
                Vertex v = entry.getKey();
                double w = entry.getValue();
                if (v.getDistance() > u.getDistance() + w) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void relax(Vertex u, Vertex v, double weight) {
        if (v.getDistance() > u.getDistance() + weight) {
            v.setDistance(u.getDistance() + weight);
            v.setPredecessor(u);
        }
    }
}
