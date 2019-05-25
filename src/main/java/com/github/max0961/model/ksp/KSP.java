package com.github.max0961.model.ksp;

import com.github.max0961.model.Graph;
import com.github.max0961.model.Path;
import lombok.Getter;

import java.util.LinkedList;

/**
 * Вычисляет K-кратчайших путей в графе между двумя вершинами.
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

        stringBuilder.append("k\t");
        stringBuilder.append("path");
        fillTabs(stringBuilder, getTabNumber("path"));
        stringBuilder.append("weight").append("\n");

        for (int i = 0; i < ksp.size(); ++i) {
            stringBuilder.append(i + 1).append("\t").append(ksp.get(i));
            fillTabs(stringBuilder, getTabNumber(ksp.get(i).toString()));
            stringBuilder.append(ksp.get(i).getTotalWeight()).append("\n");
        }
        return stringBuilder.toString();
    }

    private int getTabNumber(String str) {
        int maxLength = 0;
        for (Path path : ksp) {
            if (path.toString().length() > maxLength) {
                maxLength = path.toString().length();
            }
        }
        return (maxLength / 8 - str.length() / 8) + 1;
    }

    private void fillTabs(StringBuilder stringBuilder, int n) {
        for (int i = 0; i < n; ++i) {
            stringBuilder.append("\t");
        }
    }

    private void fillLine(StringBuilder stringBuilder, int n) {
        for (int i = 0; i < n; ++i) {
            stringBuilder.append("-");
        }
    }
}
