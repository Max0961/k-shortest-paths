package com.github.max0961.util;

import com.github.max0961.model.Graph;
import com.github.max0961.model.Vertex;

import java.util.Map;

/**
 * Время работы O(|V||E|) в наихудшем случае.
 */
public final class BellmanFordSP {
    private static boolean hasChanges = true;

    private BellmanFordSP() {
    }

    public static void compute(Graph graph, String source) {
        compute(graph, graph.vertex(source));
    }

    public static boolean compute(Graph graph, Vertex source) {
        source.setDistance(0.0);
        for (int i = 0; i < graph.verticesNumber() - 1; ++i) {
            if (hasChanges == false) {
                break;
            }
            hasChanges = false;
            for (String label : graph.getVertices().keySet()) {
                Vertex u = graph.vertex(label);
                for (Map.Entry<Vertex, Double> entry : u.getAdjacency().entrySet()) {
                    Vertex v = entry.getKey();
                    double w = entry.getValue();
                    relax(u, v, w);
                }
            }
        }
        hasChanges = true;
        for (String label : graph.getVertices().keySet()) {
            Vertex u = graph.vertex(label);
            for (Map.Entry<Vertex, Double> entry : u.getAdjacency().entrySet()) {
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
            hasChanges = true;
        }
    }
}
