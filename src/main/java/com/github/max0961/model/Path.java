package com.github.max0961.model;

import com.github.max0961.util.BellmanFordSP;
import com.github.max0961.util.HeapDijkstraSP;
import com.github.max0961.util.SimpleDijkstraSP;
import lombok.Getter;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Путь - связный список вершин
 *
 * @see Vertex
 */
public class Path implements Comparable<Path> {
    private Graph graph;
    @Getter
    private LinkedList<Vertex> vertices;
    @Getter
    private double totalWeight;

    /**
     * Путь, созданный из источника вершин
     *
     * @param vertices список вершин
     */
    public Path(LinkedList<Vertex> vertices) {
        this.vertices = new LinkedList<>(vertices);
        if (vertices.size() == 0) {
            return;
        }
        totalWeight = 0;
        for (int i = 0; i < vertices.size() - 1; ++i) {
            totalWeight += vertices.get(i).getAdjacency().get(vertices.get(i + 1));
        }
    }

    /**
     * Путь, расчитанный от источника до цели
     *
     * @param graph  граф, где расчитывается путь
     * @param source источник
     * @param target цель
     */
    public Path(Graph graph, String source, String target) {
        this(graph, graph.vertex(source), graph.vertex(target));
    }

    public Path(Graph graph, Vertex source, Vertex target) {
        this.graph = graph;
        this.graph.clearSpTreeData();
        HeapDijkstraSP.compute(graph, source);
        vertices = new LinkedList<>();
        retrieve(target, vertices);
        if (vertices.size() == 0) {
            return;
        }
        totalWeight = target.getDistance();
    }

    /**
     * Добавляет в конец к этому пути другой путь и его стоимость
     *
     * @param path другой путь
     */
    public Path add(Path path) {
        if (path.length() == 0) {
            return this;
        }
        if (this.length() > 0 && this.vertexAt(length() - 1) == path.vertexAt(0)) {
            vertices.pollLast();
        }
        vertices.addAll(path.getVertices());
        totalWeight += path.getTotalWeight();
        return this;
    }

    public Vertex vertexAt(int i) {
        if (vertices.size() <= i) {
            throw new IllegalArgumentException(String.format("no vertex with index %d since the path size is %d", i, vertices.size()));
        }
        if (i < 0) {
            throw new IllegalArgumentException(String.format("negative index %d", i));
        }
        return vertices.get(i);
    }

    @SuppressWarnings("unchecked")
    public LinkedList<Vertex> getVertices(int index) {
        LinkedList<Vertex> subList = (LinkedList<Vertex>) vertices.clone();
        if (index + 1 <= subList.size()) {
            subList.subList(index + 1, subList.size()).clear();
        }
        return subList;
    }

    public int length() {
        return this.vertices.size();
    }

    public static boolean isEqualLists(LinkedList<Vertex> a, LinkedList<Vertex> b) {
        if (a.size() != b.size()) return false;
        for (int i = 0; i < a.size(); ++i) {
            if (a.get(i) != b.get(i)) return false;
        }
        return true;
    }

    public static void retrieve(Vertex vertex, LinkedList<Vertex> vertices) {
        while (vertex.hasPredecessor()) {
            vertices.add(0, vertex);
            vertex = vertex.getPredecessor();
        }
        if (vertices.size() > 0) {
            vertices.add(0, vertex);
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (vertices.size() == 0) {
            stringBuilder.append("Нет пути");
            return stringBuilder.toString();
        }
        ListIterator<Vertex> iterator = vertices.listIterator();
        stringBuilder.append(iterator.next());
        while (iterator.hasNext()) {
            stringBuilder.append("->").append(iterator.next());
        }
        stringBuilder.append(String.format("(%f). ", totalWeight));
        return stringBuilder.toString();
    }

    @Override
    public int compareTo(Path path) {
        return Double.compare(this.totalWeight, path.totalWeight);
    }
}
