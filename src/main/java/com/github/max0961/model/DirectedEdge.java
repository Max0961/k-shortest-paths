package com.github.max0961.model;

import lombok.Getter;
import lombok.Setter;

public class DirectedEdge implements Comparable<DirectedEdge>, Cloneable {
    @Getter
    private Graph.Vertex source;
    @Getter
    private Graph.Vertex target;
    @Getter
    private Double weight;
    @Getter
    @Setter
    private Double delta;
    @Getter
    private Double incrementalWeight;

    /**
     * Конструктор ребра, в котором вес определяется уже имеющейся смежностью данных вершин в графе
     *
     * @param source источник
     * @param target цель
     */
    public DirectedEdge(Graph.Vertex source, Graph.Vertex target) {
        this.source = source;
        this.target = target;
        weight = source.getAdjacencyMap().get(target);
        if (weight == null) {
            String exception = String.format("Vertex %s are not adjacent to vertex %s in the graph", source, target);
            throw new IllegalArgumentException(exception);
        }
    }

    /**
     * @param source источник
     * @param target цель
     */
    public DirectedEdge(Graph.Vertex source, Graph.Vertex target, double weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    protected void inverse() {
        Graph.Vertex t = source;
        source = target;
        target = t;
    }

    @Override
    public String toString() {
        return source + "-" + target;
    }

    @Override
    public int compareTo(DirectedEdge edge) {
        return Double.compare(delta, edge.delta);
    }

    public void setIncrementalWeight(DirectedEdge parent) {
        if (parent == null) {
            this.incrementalWeight = this.delta;
            return;
        }
        this.incrementalWeight = this.delta - parent.delta;
    }

    public DirectedEdge clone() {
        try {
            super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        DirectedEdge e = new DirectedEdge(source, target, weight);
        e.delta = this.delta;
        e.incrementalWeight = this.incrementalWeight;
        return e;
    }
}
