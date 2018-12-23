package com.github.max0961.model;

public class DirectedEdge {
    private final Vertex u;
    private final Vertex v;
    private double weight;

    public double weight() {
        return weight;
    }

    public Vertex from() {
        return u;
    }

    public Vertex to() {
        return v;
    }

    public DirectedEdge(Vertex from, Vertex to){
        this.u = from;
        this.v = to;
        this.weight = u.adjacency().get(v);
    }

    @Override
    public String toString(){
        return String.format("w(%d,%d)=%.2f", u.label(), v.label(), weight);
    }
}
