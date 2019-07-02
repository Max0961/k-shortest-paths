package com.github.max0961.model.ksp.Eppstein;

import com.github.max0961.model.Path;
import com.github.max0961.model.ksp.KSP;
import com.github.max0961.model.DirectedEdge;
import com.github.max0961.model.Graph;
import com.github.max0961.model.util.BinaryHeap;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Упрощенная версия алгоритма Эппштейна.
 * Здесь не реорганизуется куча путей.
 * Вычисляет K-кратчайших путей (допуская циклы) в ориентированном графе между двумя вершинами.
 */
public final class SimpleEppsteinKSP extends KSP {
    private HashMap<String, DirectedEdge> sidetracks;
    private HashMap<Graph.Vertex, ArrayList<DirectedEdge>> allEdges;

    public SimpleEppsteinKSP() {
        super();
    }

    public SimpleEppsteinKSP(Graph graph, String source, String target, int k) {
        super(graph, source, target, k);
    }

    @Override
    public void findKsp(int K, Graph graph, Graph.Vertex source, Graph.Vertex target) {
        ksp.clear();
        graph.clearSpTree();
        graph.reverse();
        if (!graph.formSpTree(target)) throw new IllegalArgumentException("The graph has reachable negative cycle");
        graph.reverse();
        computeSidetrackMap(graph);
        computeAllEdgesMap(graph);
        BinaryHeap<ImplicitPath> candidates = new BinaryHeap<>();
        // Добавим в очередь кандидатов первый кратчайший путь
        candidates.add(new ImplicitPath(new DirectedEdge(source, source, 0), -1, null));
        for (int k = 0; k < K && !candidates.isEmpty(); ++k) {
            /*  Неявный путь представлен как:
                    - позиция родительского неявного пути
                    - ребро ответвления (lastSidetrack)
             */
            ImplicitPath imp = candidates.remove();
            ksp.add(imp.toExplicitPath(ksp));
            if (ksp.getLast().getTarget() != target) {
                ksp.removeLast();
                break;
            }
            // Добавим потомки этого неявного пути в очередь с приоритетом. Временная сложность O(m) в худшем случае.
            addChildrenToHeap(candidates, imp, k);
        }
    }

    private void addChildrenToHeap(BinaryHeap candidates, ImplicitPath implicitPath, int k) {
        LinkedList<DirectedEdge> edges = new LinkedList<>();
        edges.addAll(allEdges.get(implicitPath.getLastSidetrack().getTarget()));
        while (!edges.isEmpty()) {
            DirectedEdge poppedEdge = edges.pop();
            String label = poppedEdge.toString();
            if (sidetracks.containsKey(label)) {
                candidates.add(new ImplicitPath(sidetracks.get(label), k, implicitPath));
                continue;
            }
            edges.addAll(allEdges.get(poppedEdge.getTarget()));
        }
    }

    private void computeSidetrackMap(Graph graph) {
        sidetracks = new HashMap<>();
        for (DirectedEdge edge : graph.getEdges()) {
            edge.setDelta(edge.getWeight() + edge.getTarget().getDistance() - edge.getSource().getDistance());
            if (edge.getSource().getPredecessor() != edge.getTarget()) {
                sidetracks.put(edge.toString(), edge);
            }
        }
    }

    private void computeAllEdgesMap(Graph graph) {
        allEdges = new HashMap<>();
        for (Graph.Vertex vertex : graph.getVertices()) {
            allEdges.put(vertex, vertex.getEdges());
        }
    }

    public static class ImplicitPath implements Comparable<ImplicitPath> {
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
            if (ksp.size() > 0) {
                Path prefPath = ksp.get(prefPathPosition);
                for (Graph.Vertex vertex : prefPath.getVertices()){
                    vertices.add(vertex);
                    if (vertex == lastSidetrack.getSource()) break;
                }
            }
            Graph.Vertex vertex = lastSidetrack.getTarget();
            vertices.add(vertex);
            while (hasNext(vertex)) {
                vertex = next(vertex);
                vertices.add(vertex);
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
}
