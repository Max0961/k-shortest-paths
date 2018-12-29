package com.github.max0961.model;

import java.util.HashMap;

/**
 *  Вершина содержит метку, ссылки на своих соседей, расчитанные или нет расстояние от источника
 *  и ссылку на предыдущую верщину в дереве ратчайших путей.
 */
public class Vertex implements Comparable {
    private final String label;
    private final HashMap<Vertex, Double> adjacency = new HashMap<>();
    private Double distance;
    private Vertex predecessor;

    public Vertex(String label) {
        this.label = label;
        distance = Double.MAX_VALUE;
        predecessor = null;
    }

    public String label() {
        return label;
    }

    public HashMap<Vertex, Double> adjacency() {
        return adjacency;
    }

    public void addEdgeTo(Vertex vertex, double weight) {
        this.adjacency.put(vertex, weight);
    }

    public void removeEdgeTo(Vertex vertex) {
        adjacency.remove(vertex);
        if (vertex.predecessor == this) vertex.clearTreeData();
    }

    public void clearTreeData() {
        predecessor = null;
        distance = Double.MAX_VALUE;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public boolean hasPredecessor() {
        return predecessor != null;
    }

    public Vertex getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(Vertex predecessor) {
        this.predecessor = predecessor;
    }

    @Override
    public String toString() {
        return new String(label);
    }

    @Override
    public int compareTo(Object object) {
        Vertex vertex = (Vertex) object;
        return distance.compareTo(vertex.distance);
    }
}
