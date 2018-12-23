package com.github.max0961.control;

import com.github.max0961.model.DirectedEdge;
import com.github.max0961.model.Graph;
import com.github.max0961.model.Vertex;

import java.util.*;

/**
 * The running time of the algorithm is O(n^2 + m).
 * The main loop is executed n times, in each of them n operations are spent on finding the minimum.
 * The cycles of the neighbors are spent the number of the edges m.
 */

public class SimpleDijkstra {
    private Graph graph;

    public SimpleDijkstra(Graph graph, int source) {
        this.graph = graph;
        graph.validateLabel(source);
        shortestPathsTree(graph, graph.vertex(source));
    }

    public LinkedList<DirectedEdge> retrievePath(int target) {
        this.graph.validateLabel(target);
        LinkedList<DirectedEdge> path = new LinkedList<>();
        Vertex v = this.graph.vertex(target);
        while (v.hasPredecessor()) {
            path.add(new DirectedEdge(v.getPredecessor(), v));
            v = v.getPredecessor();
        }
        return path;
    }

    private void shortestPathsTree(Graph graph, Vertex source) {
        source.setDistance(0);
        boolean[] visited = new boolean[graph.size()];
        for (int i = 0; i < graph.size(); ++i) {
            Vertex u = minDistVertex(visited);
            visited[u.label()] = true;
            for (Map.Entry<Vertex, Double> entry : this.graph.vertex(u.label()).adjacency().entrySet()) {
                Vertex v = entry.getKey();
                double w = entry.getValue();
                relax(u, v, w);
            }
        }
    }

    private void relax(Vertex u, Vertex v, double weight) {
        if (v.getDistance() > u.getDistance() + weight) {
            v.setDistance(u.getDistance() + weight);
            v.setPredecessor(u);
        }
    }

    private Vertex minDistVertex(boolean[] visited) {
        int minDistLabel = 0;
        double minDist = Double.MAX_VALUE;
        for (int i = 0; i < this.graph.size(); ++i) {
            if (!visited[i] && graph.vertex(i).getDistance() < minDist) {
                minDistLabel = i;
                minDist = this.graph.vertex(i).getDistance();
            }
        }
        return this.graph.vertex(minDistLabel);
    }
}
