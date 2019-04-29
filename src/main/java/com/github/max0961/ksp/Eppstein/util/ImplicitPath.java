package com.github.max0961.ksp.Eppstein.util;

import com.github.max0961.model.DirectedEdge;
import com.github.max0961.model.Graph;
import com.github.max0961.model.Path;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.ListIterator;

public class ImplicitPath implements Comparable<ImplicitPath> {
    @Getter
    @Setter
    private DirectedEdge lastSidetrack;
    @Getter
    @Setter
    private int prefPathPosition;
    @Getter
    private ImplicitPath prefPath;
    private double sidetracksWeight;

    public ImplicitPath(DirectedEdge lastSidetrack, int prefPathPosition, ImplicitPath prefPath) {
        this.lastSidetrack = lastSidetrack;
        this.prefPathPosition = prefPathPosition;
        this.prefPath = prefPath;
        if (prefPath != null) {
            sidetracksWeight = prefPath.sidetracksWeight + lastSidetrack.getDelta();
        } else {
            sidetracksWeight = 0;
        }
    }

    public Path toExplicitPath(LinkedList<Path> ksp) {
        LinkedList<Graph.Vertex> vertices = new LinkedList<>();
        Graph.Vertex v;
        if (ksp.size() > 0) {
            Path prefPath = ksp.get(prefPathPosition);
            ListIterator<Graph.Vertex> iterator = prefPath.getVertices().listIterator();
            do {
                v = iterator.next();
                vertices.add(v);
            } while (iterator.hasNext() && v != lastSidetrack.getSource());
        }
        v = lastSidetrack.getTarget();
        vertices.add(v);
        while (hasNext(v)) {
            v = next(v);
            vertices.add(v);
        }
        return new Path(vertices);
    }

    private Graph.Vertex next(Graph.Vertex v) {
        return v.getPredecessor();
    }

    private boolean hasNext(Graph.Vertex v) {
        return v.hasPredecessor();
    }

    @Override
    public int compareTo(ImplicitPath path) {
        return Double.compare(this.sidetracksWeight, path.sidetracksWeight);
    }
}
