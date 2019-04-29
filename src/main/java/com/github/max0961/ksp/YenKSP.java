package com.github.max0961.ksp;

import com.github.max0961.model.DirectedEdge;
import com.github.max0961.model.Graph;
import com.github.max0961.model.Path;
import com.github.max0961.util.BinaryHeap;

import java.util.LinkedList;

/**
 * Вычисляет K кратчайших простых путей между двумя вершинами.
 * Время работы O(kn((n + m)log(n)).
 */
public final class YenKSP extends KSP {
    public YenKSP(Graph graph, String source, String target, int k) {
        super(graph, source, target, k);
    }

    @Override
    public void findKsp(int K, Graph graph, Graph.Vertex source, Graph.Vertex target) {
        if (graph.getNegativeEdgeNumber() > 0) throw new IllegalArgumentException("The graph has negative edges");
        BinaryHeap<Path> candidates = new BinaryHeap<>();
        // Рассчитаем первый кратчайший путь
        ksp.add(new Path(graph, source, target));
        LinkedList<DirectedEdge> removedEdges = new LinkedList<>();
        LinkedList<Graph.Vertex> removedVertices = new LinkedList<>();
        // K - 1 кратчайших путей
        for (int k = 1; k < K; ++k) {
            Path prev = ksp.get(k - 1);
            // Узел ответвления располагается от первой вершины до ближайшей к последней вершине в k-1 кратчайшем пути
            for (int i = 0; i < prev.length() - 1; ++i) {
                Graph.Vertex spur = prev.vertexAt(i);
                Path root = new Path(prev.getVertices(i));
                // Удаление ранее использованных ребер из (i) в (i + 1) гарантирует, что путь ответвления будет другим
                for (Path p : ksp) {
                    if (root.equals(new Path(p.getVertices(i)))) {
                        Graph.Vertex u = p.vertexAt(i);
                        Graph.Vertex v = p.vertexAt(i + 1);
                        if (Graph.hasEdge(u, v)) {
                            DirectedEdge removed = new DirectedEdge(u, v);
                            removedEdges.add(removed);
                            graph.removeEdge(removed);
                        }
                    }
                }
                // Удалим из графа все вершины root кроме spur
                for (Graph.Vertex rpv : root.getVertices()) {
                    if (rpv != spur) {
                        removedVertices.add(rpv);
                        graph.removeVertex(rpv.getLabel());
                    }
                }
                // Рассчитаем пути ответвления от spur до target
                Path spurPath = new Path(graph, spur, target);
                // Восстановление графа
                restoreGraph(graph, removedEdges, removedVertices);
                if (spurPath.length() == 0) {
                    continue;
                }
                Path total = root.add(spurPath);
                candidates.add(total);
            }
            if (candidates.isEmpty()) {
                return;
            }
            // Удалим дублированные пути из кандидатов в кратчайшие пути
            Path path;
            do {
                path = candidates.remove();
            } while (prev.equals(path));
            ksp.add(path);
        }
    }

    private void restoreGraph(Graph graph, LinkedList<DirectedEdge> edges, LinkedList<Graph.Vertex> vertices) {
        while (!vertices.isEmpty()) {
            graph.addVertex(vertices.pop());
        }
        while (!edges.isEmpty()) {
            graph.addEdge(edges.pop());
        }
    }
}
