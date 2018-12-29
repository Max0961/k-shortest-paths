package com.github.max0961.model;

import com.github.max0961.util.BellmanFordSP;
import com.github.max0961.util.HeapDijkstraSP;
import com.github.max0961.util.SimpleDijkstraSP;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Путь - связный список вершин
 */
public class Path implements Comparable<Path>, Cloneable {
    private LinkedList<Vertex> vertices;
    private Graph graph;
    private String source;
    private String target;
    private double totalWeight;

    public Path(LinkedList<Vertex> vertices) {
        this.vertices = new LinkedList<>(vertices);
        totalWeight = 0;
        Vertex v = vertices.peek();
        while (v.hasPredecessor()) {
            totalWeight += v.getPredecessor().adjacency().get(v);
        }
        target = vertices.peek().label();
        source = v.label();
    }

    public Path(Graph graph, String source, String target) {
        this(graph, graph.getVertex(source), graph.getVertex(target));
    }

    public Path(Graph graph, Vertex source, Vertex target) {
        this.graph = graph;
        this.source = source.label();
        this.target = target.label();
        if (source.getDistance() != 0) {
            this.graph.clearTreeData();
            HeapDijkstraSP.compute(graph, source);
        }
        this.vertices = new LinkedList<>();
        this.totalWeight = target.getDistance();
        retrieve(source, target, vertices);
    }

    public void add(Path path) {
        vertices.addAll(path.vertices());
        totalWeight += path.getTotalWeight();
    }

    public LinkedList<Vertex> vertices() {
        return vertices;
    }

    public LinkedList<Vertex> vertices(int index) {
        return (LinkedList) vertices.subList(0, index);
    }

    public Vertex vertex(int index) {
        return vertices.get(index);
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(double totalWeight) {
        totalWeight = totalWeight;
    }

    public int length() {
        return this.vertices.size() - 1;
    }

    public static void retrieve(Vertex source, Vertex vertex, LinkedList<Vertex> vertices) {
        if (source == vertex) vertices.add(vertex);
        if (!vertex.hasPredecessor()) return;
        retrieve(source, vertex.getPredecessor(), vertices);
        vertices.add(vertex);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (vertices.size() == 0) {
            stringBuilder.append(String.format("Не существует путь из %s в %s", source, target));
            return stringBuilder.toString();
        }
        stringBuilder.append(String.format("Кратчайший путь из %s в %s: ", source, target));
        ListIterator<Vertex> iterator = vertices.listIterator();
        stringBuilder.append(iterator.next().label());
        while (iterator.hasNext()) {
            stringBuilder.append("->").append(iterator.next().label());
        }
        stringBuilder.append(String.format(";\nВес пути: %f", totalWeight));
        return stringBuilder.toString();
    }

    @Override
    public int compareTo(Path path) {
        return Double.compare(this.totalWeight, path.totalWeight);
    }
}
