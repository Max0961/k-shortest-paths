package com.github.max0961.control;

import com.github.max0961.model.DirectedEdge;
import com.github.max0961.model.Graph;
import com.github.max0961.model.Vertex;

import java.util.LinkedList;
import java.util.Map;

public class BellmanFord {
    private Graph graph;

    public BellmanFord(Graph graph, int source) {
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

    private boolean shortestPathsTree(Graph graph, Vertex source) {
        source.setDistance(0);
        for (int i = 0; i < graph.size() - 1; ++i) {
            for (int j = 0; j < graph.size(); ++j) {
                Vertex u = graph.vertex(j);
                for (Map.Entry<Vertex, Double> entry : this.graph.vertex(u.label()).adjacency().entrySet()) {
                    Vertex v = entry.getKey();
                    double w = entry.getValue();
                    relax(u, v, w);
                }
            }
        }
        for (int j = 0; j < graph.size(); ++j) {
            Vertex u = graph.vertex(j);
            for (Map.Entry<Vertex, Double> entry : this.graph.vertex(u.label()).adjacency().entrySet()) {
                Vertex v = entry.getKey();
                double w = entry.getValue();
                if (v.getDistance() > u.getDistance() + w) {
                    return false;
                }
            }
        }
        return  true;
    }

    private void relax(Vertex u, Vertex v, double weight) {
        if (v.getDistance() > u.getDistance() + weight) {
            v.setDistance(u.getDistance() + weight);
            v.setPredecessor(u);
        }
    }
}
