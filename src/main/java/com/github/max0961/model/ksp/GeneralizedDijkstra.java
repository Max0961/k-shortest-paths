package com.github.max0961.model.ksp;

import com.github.max0961.model.Graph;
import com.github.max0961.model.Path;
import com.github.max0961.model.ksp.util.BinaryHeap;

import java.util.LinkedList;

public class GeneralizedDijkstra extends KSP {
    public GeneralizedDijkstra() {
        super();
    }

    public GeneralizedDijkstra(Graph graph, String source, String target, int k) {
        super(graph, source, target, k);
    }

    @Override
    public void findKsp(int K, Graph graph, Graph.Vertex source, Graph.Vertex target) {
        ksp.clear();
        BinaryHeap<Path> candidates = new BinaryHeap<>();
        candidates.add(new Path(graph, source, source));
        while (!candidates.isEmpty() && ksp.size() < K) {
            Path path = candidates.remove();
            Graph.Vertex u = path.getTarget();
            u.incPathCount();
            if (u == target) ksp.add(path);
            if (u.getPathCount() > K) continue;
            for (Graph.Vertex neighbor : path.getTarget().getAdjacencyMap().keySet()) {
                LinkedList<Graph.Vertex> vertices = new LinkedList<>(path.getVertices());
                vertices.add(neighbor);
                candidates.add(new Path(vertices));
            }
        }
        for (Graph.Vertex v : graph.getVertices()) {
            v.reset();
        }
    }
}
