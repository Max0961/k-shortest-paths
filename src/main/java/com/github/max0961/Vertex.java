package com.github.max0961;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Vertex implements Comparable<Vertex>, Cloneable{
    private final int label;

    private final HashMap<Vertex, Double> adjacency = new HashMap<>();
    private Double distance;
    private Vertex predecessor;

    public Vertex(int label) {
        this.label = label;
        this.distance = Double.MAX_VALUE;
    }

    public void addEdge(Vertex vertex, double weight) {
        adjacency.put(vertex, weight);
    }

    public void removeEdge(Vertex vertex) {
        adjacency.remove(vertex);
    }

    public DirectedEdge getEdgeTo(Vertex vertex) {
        return new DirectedEdge(this, vertex);
    }

    public HashMap<Vertex, Double> adjacency(){
        return adjacency;
    }

    public int label(){
        return label;
    }

    public Double getDistance(){
        return distance;
    }

    public void setDistance(double distance){
        this.distance = distance;
    }

    public boolean hasPredecessor(){
        return predecessor != null;
    }

    public Vertex getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(Vertex predecessor) {
        this.predecessor = predecessor;
    }

    @Override
    public int compareTo(Vertex other) {
        return this.distance.compareTo(other.distance);
    }
}
