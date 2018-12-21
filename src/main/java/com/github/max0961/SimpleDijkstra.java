package com.github.max0961;

import java.util.*;

public class SimpleDijkstra {

    private Vertex[] distanceTo;
    private DirectedEdge[] edgeTo;
    private PriorityQueue<Vertex> pq;

    public SimpleDijkstra(Graph graph, int source) {
        shortestPaths(graph, source);
    }

    public double distTo(int v) {
        validateVertex(v);
        return distanceTo[v].getDistance();
    }

    public boolean hasPathTo(int v) {
        validateVertex(v);
        return distanceTo[v].getDistance() < Double.POSITIVE_INFINITY;
    }

    public Stack<DirectedEdge> pathTo(int v) {
        validateVertex(v);
        if (!hasPathTo(v)) return null;
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.fromLabel()]) {
            path.push(e);
        }
        return path;
    }

    public void shortestPaths(Graph graph, int s) {
        int n = graph.vertexCount();
        distanceTo = graph.getVertices();
        distanceTo[s].setDistance(0);
        edgeTo = new DirectedEdge[n];
        boolean[] visited = new boolean[n];
        for (int i = 0; i < n; ++i) {
            int v = extractMin(distanceTo, visited);
            visited[v] = true;
            for (DirectedEdge e : graph.getAdjacency(v)) {
                relax(e);
            }
        }
    }

    private void relax(DirectedEdge e) {
        int v = e.fromLabel(), w = e.toLabel();
        if (distanceTo[w].getDistance() > distanceTo[v].getDistance() + e.getWeight()) {
            distanceTo[w].setDistance(distanceTo[v].getDistance() + e.getWeight());
            edgeTo[w] = e;
        }
    }

    private int extractMin(Vertex[] vertices, boolean[] visited) {
        int minDistLabel = 0;
        double minDist = Double.MAX_VALUE;
        for (int i = 0; i < vertices.length; ++i) {
            if (!visited[i] && vertices[i].getDistance() < minDist) {
                minDistLabel = i;
                minDist = vertices[i].getDistance();
            }
        }
        return minDistLabel;
    }

    private void validateVertex(int v) {
        int V = distanceTo.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }
}
