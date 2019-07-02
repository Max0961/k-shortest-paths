package com.github.max0961.model.util;

import com.github.max0961.model.Graph;

import java.util.Map;

/**
 * Временная сложность O(n*m) в наихудшем случае.
 */
public final class BellmanFordSP {
    private static boolean hasChanges = true;

    private BellmanFordSP() {
    }

    public static boolean compute(Graph graph, String root) {
        return compute(graph, graph.getVertex(root));
    }

    public static boolean compute(Graph graph, Graph.Vertex root) {
        root.setDistance(0.0);
        for (int i = 0; i < graph.verticesNumber() - 1; ++i) {
            if (!hasChanges) {
                break;
            }
            hasChanges = false;
            for (Graph.Vertex u : graph.getVertices()) {
                for (Map.Entry<Graph.Vertex, Double> entry : u.getAdjacencyMap().entrySet()) {
                    Graph.Vertex v = entry.getKey();
                    double w = entry.getValue();
                    relax(u, v, w);
                }
            }
        }
        return hasReachableNegativeCycle(graph);
    }

    private static boolean hasReachableNegativeCycle(Graph graph){
        hasChanges = true;
        for (Graph.Vertex u : graph.getVertices()) {
            for (Map.Entry<Graph.Vertex, Double> entry : u.getAdjacencyMap().entrySet()) {
                Graph.Vertex v = entry.getKey();
                double w = entry.getValue();
                if (v.getDistance() > u.getDistance() + w) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void relax(Graph.Vertex u, Graph.Vertex v, double weight) {
        if (v.getDistance() > u.getDistance() + weight) {
            v.setDistance(u.getDistance() + weight);
            v.setPredecessor(u);
            hasChanges = true;
        }
    }
}
