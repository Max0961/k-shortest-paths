package com.github.max0961.model.ksp.Eppstein;

import com.github.max0961.model.ksp.Eppstein.util.ImplicitPath;
import com.github.max0961.model.ksp.KSP;
import com.github.max0961.model.DirectedEdge;
import com.github.max0961.model.Graph;
import com.github.max0961.model.ksp.util.BinaryHeap;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Упрощенная версия алгоритма Эппштейна.
 * Здесь не реорганизуется куча путей.
 * Вычисляет K-кратчайших путей (допуская циклы) в ориентированном графе между двумя вершинами.
 */
public final class SimpleEppsteinKSP extends KSP {
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
        HashMap<String, DirectedEdge> sidetracks = computeSidetrackMap(graph);
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
            // Добавить потомки этого неявного пути в очередь с приоритетом.
            // Временная сложность O(m).
            addChildrenToHeap(candidates, imp, sidetracks, k);
        }
    }

    private void addChildrenToHeap(BinaryHeap c, ImplicitPath p, HashMap<String, DirectedEdge> sidetracks, int k) {
        LinkedList<DirectedEdge> edges = new LinkedList<>();

        for (DirectedEdge outgoingEdge : p.getLastSidetrack().getTarget().getEdges()) {
            edges.push(outgoingEdge);
        }

        while (!edges.isEmpty()) {
            DirectedEdge poppedEdge = edges.pop();
            String label = poppedEdge.toString();

            if (sidetracks.containsKey(label)) {
                ImplicitPath candidate = new ImplicitPath(sidetracks.get(label), k, p);
                c.add(candidate);
                //System.out.print(sidetracks.get(label).getDelta() + " ");
            } else {
                for (DirectedEdge outgoingEdge : poppedEdge.getTarget().getEdges()) {
                    edges.push(outgoingEdge);
                }
            }
        }
    }

    private HashMap<String, DirectedEdge> computeSidetrackMap(Graph graph) {
        HashMap<String, DirectedEdge> sidetracks = new HashMap<>();
        for (DirectedEdge edge : graph.getEdges()) {
            edge.setDelta(edge.getWeight() + edge.getTarget().getDistance() - edge.getSource().getDistance());
            if (edge.getSource().getPredecessor() != edge.getTarget()) {
                sidetracks.put(edge.toString(), edge);
            }
        }
        return sidetracks;
    }
}
