package com.github.max0961.model;

import lombok.Getter;

public class DirectedEdge {
    @Getter
    private final Vertex source;
    @Getter
    private final Vertex target;
    @Getter
    private double weight;

    public DirectedEdge(Vertex source, Vertex target){
        this.source = source;
        this.target = target;
        this.weight = source.getAdjacency().get(target);
    }

    @Override
    public String toString(){
        return String.format("w(%s, %s)=%.2f", source, target, weight);
    }
}
