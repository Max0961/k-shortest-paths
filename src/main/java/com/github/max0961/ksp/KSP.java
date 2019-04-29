package com.github.max0961.ksp;

import com.github.max0961.model.Graph;
import com.github.max0961.model.Path;
import lombok.Getter;

import java.util.LinkedList;

public abstract class KSP {
    @Getter
    protected final LinkedList<Path> ksp;

    public KSP() {
        ksp = new LinkedList<>();
    }

    public KSP(Graph graph, String source, String target, int k) {
        ksp = new LinkedList<>();
        findKsp(k, graph, graph.getVertex(source), graph.getVertex(target));
    }

    public abstract void findKsp(int K, Graph graph, Graph.Vertex source, Graph.Vertex target);

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < ksp.size(); ++i) {
            stringBuilder.append(i + 1).append(") ").append(ksp.get(i)).append("\n");
        }
        return stringBuilder.toString();
    }
}
