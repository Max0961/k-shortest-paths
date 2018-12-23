package com.github.max0961.model;

import java.util.HashMap;

public class Vertex {
    private final int label;
    private final HashMap<Vertex, Double> adjacency = new HashMap<>();
    private Double distance;
    private Vertex predecessor;

    public Vertex(int label) {
        this.label = label;
        this.distance = Double.MAX_VALUE;
    }

    public int label() {
        return this.label;
    }

    public HashMap<Vertex, Double> adjacency() {
        return this.adjacency;
    }

    public void addEdgeTo(Vertex vertex, double weight) {
        this.adjacency.put(vertex, weight);
    }

    public void removeEdgeTo(Vertex vertex) {
        this.adjacency.remove(vertex);
        if (vertex.predecessor == this) {
            vertex.predecessor = null;
            vertex.distance = Double.MAX_VALUE;
        }
    }

    public Double getWeight(Vertex vertex){
        return this.adjacency.get(vertex);
    }

    public DirectedEdge getEdgeTo(Vertex vertex) {
        if (this.adjacency.containsKey(vertex)) {
            return new DirectedEdge(this, vertex);
        }
        return null;
    }

    public Double getDistance() {
        return this.distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public boolean hasPredecessor() {
        return this.predecessor != null;
    }

    public Vertex getPredecessor() {
        return this.predecessor;
    }

    public void setPredecessor(Vertex predecessor) {
        this.predecessor = predecessor;
    }

    public void clear() {
        this.adjacency().clear();
        this.predecessor = null;
        this.distance = Double.MAX_VALUE;
    }
}
