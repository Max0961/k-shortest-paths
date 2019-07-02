package com.github.max0961.model.ksp;

import com.github.max0961.model.Graph;
import com.github.max0961.model.Path;
import com.github.max0961.model.util.BinaryHeap;
import lombok.Getter;

import java.util.LinkedList;
import java.util.Map;

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
        BinaryHeap<LinkedPartialPath> candidates = new BinaryHeap<>();
        candidates.add(new LinkedPartialPath(null, source, 0));
        while (!candidates.isEmpty() && target.getCounter() < K) {
            LinkedPartialPath partialPath = candidates.remove();
            Graph.Vertex u = partialPath.getLastVertex();
            u.incCounter();
            // Если удаленный из кучи путь есть путь полный, добавим его в список и перейдем к следующей итерации
            if (u == target) {
                ksp.add(partialPath.toPath());
                continue;
            }
            if (u.getCounter() > K) continue;
            // Добавляем в очередь с приоритетом все дополнения этого пути смежными решинами поседней вершины пути
            for (Map.Entry<Graph.Vertex, Double> entry : u.getAdjacencyMap().entrySet()) {
                candidates.add(new LinkedPartialPath(partialPath, entry.getKey(), entry.getValue()));
            }
        }
        for (Graph.Vertex v : graph.getVertices()) {
            v.reset();
        }
    }

    private static class LinkedPartialPath implements Comparable<LinkedPartialPath> {
        @Getter
        LinkedPartialPath previousPath;
        @Getter
        Graph.Vertex lastVertex;
        @Getter
        double totalWeight;

        public LinkedPartialPath(LinkedPartialPath previousPath, Graph.Vertex lastVertex, double edgeWeight) {
            this.previousPath = previousPath;
            this.lastVertex = lastVertex;
            if (previousPath != null) {
                this.totalWeight = previousPath.totalWeight + edgeWeight;
            } else {
                this.totalWeight = 0;
            }
        }

        public Path toPath() {
            LinkedList<Graph.Vertex> vertices = new LinkedList<>();
            buildPath(this, vertices);
            return new Path(vertices);
        }

        private void buildPath(LinkedPartialPath partialPath, LinkedList<Graph.Vertex> vertices) {
            if (partialPath.previousPath == null) {
                vertices.add(partialPath.lastVertex);
                return;
            }
            buildPath(partialPath.previousPath, vertices);
            vertices.add(partialPath.lastVertex);
        }

        @Override
        public int compareTo(LinkedPartialPath partialPath) {
            return Double.compare(this.totalWeight, partialPath.totalWeight);
        }
    }
}
