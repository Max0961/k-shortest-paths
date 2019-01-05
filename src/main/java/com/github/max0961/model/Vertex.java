package com.github.max0961.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
 * Вершина содержит метку, ссылки на своих соседей, расчитанные или нет расстояние от источника
 * и ссылку на предыдущую верщину в дереве ратчайших путей.
 */
public class Vertex implements Comparable<Vertex> {
    @Getter
    private final String label;
    @Getter
    private final HashMap<Vertex, Double> adjacency;
    @Getter
    @Setter
    private Double distance;
    @Getter
    @Setter
    private Vertex predecessor;

    public Vertex(String label) {
        this.label = label;
        adjacency = new HashMap<>();
        distance = Double.MAX_VALUE;
    }

    public boolean hasPredecessor() {
        return predecessor != null;
    }

    public void addEdgeTo(Vertex vertex, double weight) {
        this.adjacency.put(vertex, weight);
    }

    public boolean removeEdgeTo(Vertex vertex) {
        if (!adjacency.containsKey(vertex)) {
            return false;
        }
        adjacency.remove(vertex);
        return true;
    }

    public void clearSpTreeData() {
        distance = Double.MAX_VALUE;
        predecessor = null;
    }

    @Override
    public String toString() {
        return new String(label);
    }

    @Override
    public int hashCode() {
        return label.hashCode();
    }

    @Override
    public int compareTo(Vertex vertex) {
        return distance.compareTo(vertex.distance);
    }
}
