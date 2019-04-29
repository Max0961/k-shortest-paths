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
    @Setter
    private Double incrementalWeight;

    /**
     * Конструктор ребра, которое уже определено смежностью данных вершин в графе
     *
     * @param source
     * @param target
     */
    public DirectedEdge(Graph.Vertex source, Graph.Vertex target) {
        this.source = source;
        this.target = target;
        weight = source.getAdjacencyMap().get(target);
        if (weight == null) {
            String exception = String.format("Vertices %s and %s are not adjacent in its graph", source, target);
            throw new IllegalArgumentException(exception);
        }
    }

    public DirectedEdge(Graph.Vertex source, Graph.Vertex target, double weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    public void inverse() {
        Graph.Vertex t = source;
        source = target;
        target = t;
    }

    @Override
    public String toString() {
        return new String(source + "->" + target);
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
        DirectedEdge e = new DirectedEdge(source, target, weight);
        e.delta = this.delta;
        e.incrementalWeight = this.incrementalWeight;
        return e;
    }
}
