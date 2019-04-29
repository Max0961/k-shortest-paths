package com.github.max0961.ksp.Eppstein;

import com.github.max0961.ksp.Eppstein.util.EdgeHeap;
import com.github.max0961.ksp.Eppstein.util.ImplicitPath;
import com.github.max0961.ksp.KSP;
import com.github.max0961.model.DirectedEdge;
import com.github.max0961.model.Graph;
import com.github.max0961.model.Path;
import com.github.max0961.util.BinaryHeap;
import com.github.max0961.util.HeapDijkstraSP;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Вычисляет K кратчайших путей (допуская циклы) в графе между двумя вершинами.
 * Время работы O(m + n * log(n) + k * log(k))).
 */
public final class EppsteinKSP extends KSP {
    private HashMap<String, DirectedEdge> sidetracks;
    private HashMap<Graph.Vertex, DirectedEdge> outRoots;
    private HashMap<Graph.Vertex, EdgeHeap> outRestHeaps;
    private HashMap<Graph.Vertex, EdgeHeap> treeHeaps;

    public EppsteinKSP(Graph graph, String source, String target, int k) {
        super(graph, source, target, k);
    }

    public void findKsp(int K, Graph graph, Graph.Vertex source, Graph.Vertex target) {
        graph.reverse();
        if (!graph.formSpTree(target)) throw new IllegalArgumentException("The graph has reachable negative cycle");
        graph.reverse();

        sidetracks = computeSidetrackMap(graph);
        // H_out(v)
        outRoots = new HashMap<>();
        outRestHeaps = new HashMap<>();
        computeOutHeaps(graph);
        // H_T(v)
        treeHeaps = computeTreeHeaps(graph);
        HashMap<Graph.Vertex, PointerHeap> graphHeaps = computeGraphHeaps(graph);

//        BinaryHeap<ImplicitPath> candidates = new BinaryHeap<>();
//        // Добавим в очередь кандидатов первый кратчайший путь
//        candidates.add(new ImplicitPath(new DirectedEdge(source, source, 0), -1, null));
//        for (int k = 0; k < K && !candidates.isEmpty(); ++k) {
//            /*  Неявный путь представлен как:
//                    - позиция родительского неявного пути
//                    - ребро ответвления (lastSidetrack)
//             */
//            ImplicitPath path = candidates.remove();
//            ksp.add(path.toExplicitPath(ksp));
//            // Добавить потомки этого неявного пути в очередь с приоритетом.
//            // Временная сложность O(m).
//            addExplicitChildrenToQueue(candidates, path, treeHeaps, k);
//        }
    }

    private Graph.Vertex next(Graph.Vertex v) {
        return v.getPredecessor();
    }

    private boolean hasNext(Graph.Vertex v) {
        return v.hasPredecessor();
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

    private void addExplicitChildrenToQueue(BinaryHeap c, ImplicitPath p, HashMap<Graph.Vertex, EdgeHeap> treeHeaps, int k) {
        //ImplicitPath candidate = new ImplicitPath(treeHeaps.,k, p);
    }

    private void addCrossEdgeChildToQueue(HashMap<Graph.Vertex,
            EdgeHeap> heaps, ImplicitPath p, int prefPath, ArrayList<Path> ksp, PriorityQueue<ImplicitPath> pathPQ) {

    }

    /**
     * Для каждой вершины v в графе строим кучу H_out(v) из всех исходящих из нее ребер в G - T
     */
    private void computeOutHeaps(Graph graph) {
        for (Graph.Vertex v : graph.getVertices()) {
            EdgeHeap rest = new EdgeHeap();
            for (DirectedEdge e : v.getEdges()) {
                if (sidetracks.containsKey(e.toString())) {
                    rest.add(sidetracks.get(e.toString()));
                }
            }
            if (!rest.isEmpty()) {
                outRoots.put(v, rest.remove());
                outRestHeaps.put(v, rest);
            }
        }
    }

    /**
     * Для каждой вершины v в графе строим кучу H_T(v) из ребер bestSidetracks.
     * Для ребер каждой кучи определяются уникальные значения setIncrementalWeight,
     * поэтому необходимо использовать копии экземпляров ребер для каждой кучи.
     */
    private HashMap<Graph.Vertex, EdgeHeap> computeTreeHeaps(Graph graph) {
        HashMap<Graph.Vertex, EdgeHeap> treeHeaps = new HashMap<>();
        for (Graph.Vertex v : graph.getVertices()) {
            EdgeHeap treeHeap = new EdgeHeap();
            while (hasNext(v)) {
                if (outRoots.containsKey(v)) {
                    treeHeap.add(outRoots.get(v).clone());
                }
                v = next(v);
            }
            for (int i = 0; i < treeHeap.size(); ++i) {
                DirectedEdge parentBestSidetrack = treeHeap.parent(i);
                treeHeap.getEdge(i).setIncrementalWeight(parentBestSidetrack);
            }
            treeHeaps.put(v, treeHeap);
        }
        return treeHeaps;
    }

    /**
     * Для каждой вершины v в графе строим кучу H_G(v).
     */
    private HashMap<Graph.Vertex, PointerHeap> computeGraphHeaps(Graph graph) {
        HashMap<Graph.Vertex, PointerHeap> graphHeaps = new HashMap<>();
        for (Graph.Vertex v : graph.getVertices()) {
            EdgeHeap treeHeap = treeHeaps.get(v);
            if (treeHeap == null) continue;
            PointerHeap graphHeap = new PointerHeap(treeHeap.peek(), 3);
            traverseHeap(graphHeap, treeHeap, 0);
        }
        return graphHeaps;
    }

    private void traverseHeap(PointerHeap heap, EdgeHeap treeHeap, int i) {
        heap.addChild(outRestHeaps.get(heap.sidetrack.getSource()));
        Integer left, right;
        if (treeHeap.hasLeftChild(i)) {
            left = treeHeap.leftChildIndex(i);
            PointerHeap childHeap = new PointerHeap(treeHeap.getEdge(left), 3);
            heap.addChild(childHeap);
            traverseHeap(childHeap, treeHeap, left);
        }
        if (treeHeap.hasRightChild(i)) {
            right = treeHeap.rightChildIndex(i);
            PointerHeap childHeap = new PointerHeap(treeHeap.getEdge(right), 3);
            heap.addChild(childHeap);
            traverseHeap(childHeap, treeHeap, right);
        }
    }

    static class PointerHeap {
        @Getter
        @Setter
        private DirectedEdge sidetrack;
        @Getter
        @Setter
        private ArrayList<Object> children;

        public PointerHeap(DirectedEdge sidetrack, int degree) {
            this.sidetrack = sidetrack;
            this.children = new ArrayList<>(degree);
        }

        public void addChild(Object child) {
            this.children.add(child);
        }
    }
}
