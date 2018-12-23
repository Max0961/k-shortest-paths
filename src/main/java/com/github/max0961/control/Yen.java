package com.github.max0961.control;

import com.github.max0961.model.DirectedEdge;
import com.github.max0961.model.Graph;
import com.github.max0961.model.Vertex;

import java.util.ArrayList;
import java.util.LinkedList;

public class Yen {
    private Graph graph;
    private SimpleDijkstra dijkstra;

    public Yen(Graph graph, int source, int target, int k) {
        this.graph = graph;
        graph.validateLabel(source);
        graph.validateLabel(target);
        kShortestPaths(k, graph.vertex(source), graph.vertex(target));
    }

    private void kShortestPaths(int k, Vertex source, Vertex terget) {
        ArrayList<LinkedList<DirectedEdge>> a = new ArrayList<>(k);
        dijkstra = new SimpleDijkstra(graph, source.label());

        for (int i = 1; i <= k; ++i){

        }
    }
}
