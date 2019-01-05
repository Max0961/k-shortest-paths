package com.github.max0961.ksp;

import com.github.max0961.model.DirectedEdge;
import com.github.max0961.model.Graph;
import com.github.max0961.model.Path;
import com.github.max0961.model.Vertex;
import com.github.max0961.util.BinaryHeap;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class YenKSP {
    private final ArrayList<Path> ksp;
    private final BinaryHeap<Path> candidates;

    public YenKSP(Graph graph, String source, String target, int k) {
        ksp = new ArrayList<>();
        candidates = new BinaryHeap<>();
        kShortestPaths(k, graph, graph.vertex(source), graph.vertex(target));
    }

    private void kShortestPaths(int K, Graph graph, Vertex source, Vertex target) {
        // Расчитать первый кратчайший путь
        ksp.add(new Path(graph, source, target));
        LinkedList<DirectedEdge> removedEdges = new LinkedList<>();
        LinkedList<Vertex> removedVertices = new LinkedList<>();
        // K - 1 кратчайших путей
        for (int k = 1; k < K; ++k) {
            Path previous = ksp.get(k - 1);
            // Узел ответвления располагается от первой вершины до ближайшей к последней вершине в k-1 кратчайшем пути
            for (int i = 0; i < previous.length() - 1; ++i) {
                Vertex spur = previous.vertexAt(i);
                Path root = new Path(previous.getVertices(i));
                // Удаление ранее использованных ребер из (i) в (i + 1) гарантирует, что путь ответвления будет другим
                for (Path p : ksp) {
                    if (Path.isEqualLists(root.getVertices(), p.getVertices(i))) {
                        Vertex u = p.vertexAt(i);
                        Vertex v = p.vertexAt(i + 1);
                        if (Graph.hasEdge(u, v)) {
                            DirectedEdge removed = new DirectedEdge(u, v);
                            removedEdges.add(removed);
                            graph.removeEdge(u.getLabel(), v.getLabel());
                        }
                    }
                }
                // Удадить из графа все кроме spur вершины root
                for (Vertex rpv : root.getVertices()) {
                    if (rpv != spur) {
                        removedVertices.add(rpv);
                        graph.removeVertex(rpv.getLabel());
                    }
                }
                // Рассчитать путь ответвления от вершины ответвления до вершины назначения
                Path spurPath = new Path(graph, spur, target);
                // Восстановить граф
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
            Path path;
            do {
                path = candidates.remove();
            } while (Path.isEqualLists(ksp.get(ksp.size() - 1).getVertices(), path.getVertices()));
            ksp.add(path);
        }
    }

    private void restoreGraph(Graph graph, LinkedList<DirectedEdge> edges, LinkedList<Vertex> vertices) {
        while (!vertices.isEmpty()) {
            graph.addVertex(vertices.pop());
        }
        while (!edges.isEmpty()) {
            DirectedEdge edge = edges.pop();
            Graph.addEdge(edge.getSource(), edge.getTarget(), edge.getWeight());
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < ksp.size(); ++i) {
            stringBuilder.append(i + 1).append(")\t").append(ksp.get(i)).append("\n");
            ;
        }
        return stringBuilder.toString();
    }
}
