package com.github.max0961;

public class DirectedEdge {
    private double weight;
    private Vertex u;
    private Vertex v;

    public double getWeight() {
        return weight;
    }

    public Vertex from() {
        return u;
    }

    public Vertex to() {
        return v;
    }

    public int fromLabel() {
        return u.getLabel();
    }

    public int toLabel() {
        return v.getLabel();
    }

    public DirectedEdge(Vertex u, Vertex v, double weight){
        this.u = u;
        this.v = v;
        this.weight = weight;
    }

    @Override
    public String toString(){
        return String.format("w(%d,%d)=%.2f", u.getLabel(), v.getLabel(), weight);
    }
}
