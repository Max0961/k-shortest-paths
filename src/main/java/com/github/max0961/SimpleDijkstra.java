package com.github.max0961;

import java.util.*;

public class SimpleDijkstra {

    private Graph graph;
    private PriorityQueue<Vertex> pq;

    public SimpleDijkstra(Graph graph, int source) {
        this.graph = graph;
        validateVertex(source);
        shortestPaths(graph, source);
    }

    public double distanceTo(int v) {
        validateVertex(v);
        return graph.vertex(v).getDistance();
    }

    public boolean hasPathTo(int v) {
        validateVertex(v);
        return graph.vertex(v).getDistance() < Double.POSITIVE_INFINITY;
    }

    public ArrayList<DirectedEdge> pathTo(int target) {
        validateVertex(target);
        if (!hasPathTo(target)) return null;
        ArrayList<DirectedEdge> path = new ArrayList<>();
        Vertex v = graph.vertex(target);
        while (v.hasPredecessor()){
            path.add(new DirectedEdge(v.getPredecessor(), v));
            v = v.getPredecessor();
        }
        return path;
    }

    public void shortestPaths(Graph graph, int source) {
        Vertex s = graph.vertex(source);
        s.setDistance(0);
        boolean[] visited = new boolean[graph.size()];
        for (int i = 0; i < graph.size(); ++i) {
            Vertex u = extractMin(visited);
            visited[u.label()] = true;
            for (Map.Entry<Vertex, Double> entry : graph.vertex(u.label()).adjacency().entrySet()) {
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

    private Vertex extractMin(boolean[] visited) {
        int minDistLabel = 0;
        double minDist = Double.MAX_VALUE;
        for (int i = 0; i < graph.size(); ++i) {
            if (!visited[i] && graph.vertex(i).getDistance() < minDist) {
                minDistLabel = i;
                minDist = graph.vertex(i).getDistance();
            }
        }
        return graph.vertex(minDistLabel);
    }

    private void validateVertex(int v) {
        int size = graph.size();
        if (v < 0 || v >= size) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (size - 1));
        }
    }
}
