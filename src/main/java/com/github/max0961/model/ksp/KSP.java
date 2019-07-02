package com.github.max0961.model.ksp;

import com.github.max0961.model.Graph;
import com.github.max0961.model.Path;
import lombok.Getter;

import java.util.LinkedList;

/**
 * Вычисляет K-кратчайшие пути в графе между двумя вершинами.
 */
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

    public void findKsp(int k, Graph graph, String source, String target) {
        findKsp(k, graph, graph.getVertex(source), graph.getVertex(target));
    }

    public abstract void findKsp(int K, Graph graph, Graph.Vertex source, Graph.Vertex target);

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int minSpaceCount = 1;

        stringBuilder.append("k");
        fillSpaces(stringBuilder, getIntLen(ksp.size() + 1));
        stringBuilder.append("path");
        int psml = findPathStringMaxLength();
        fillSpaces(stringBuilder, Math.max(psml - 4 + 1, minSpaceCount));
        stringBuilder.append("weight").append("\n");

        for (int i = 0; i < ksp.size(); ++i) {
            stringBuilder.append(i + 1);
            fillSpaces(stringBuilder, getIntLen(ksp.size() + 1) - getIntLen(i + 1) + minSpaceCount);
            stringBuilder.append(ksp.get(i));
            fillSpaces(stringBuilder, Math.max(psml, 4) - ksp.get(i).toString().length() + minSpaceCount);
            stringBuilder.append(ksp.get(i).getTotalWeight()).append("\n");
        }
        return stringBuilder.toString();
    }

    private int getIntLen(int value) {
        return Integer.toString(value).length();
    }

    private int findPathStringMaxLength() {
        int maxLength = 0;
        for (Path path : ksp) {
            if (path.toString().length() > maxLength) {
                maxLength = path.toString().length();
            }
        }
        return maxLength;
    }

    private void fillSpaces(StringBuilder stringBuilder, int n) {
        for (int i = 0; i < n; ++i) {
            stringBuilder.append(" ");
        }
    }
}
