package com.github.max0961.model;

import com.github.max0961.util.BellmanFordSP;
import com.github.max0961.util.HeapDijkstraSP;
import lombok.Getter;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Класс Path содержит связный список вершин и значение веса пути
 *
 * @see Graph.Vertex
 */
public class Path implements Comparable<Path> {
    @Getter
    private LinkedList<Graph.Vertex> vertices;
    @Getter
    public Graph.Vertex source;
    @Getter
    public Graph.Vertex target;
    @Getter
    public Double totalWeight;
    @Getter
    private boolean isReachableTarget;

    /**
     * Путь, созданный из списка вершин
     *
     * @param vertices список вершин
     */
    public Path(LinkedList<Graph.Vertex> vertices) {
        if (vertices.size() > 0) {
            this.vertices = new LinkedList<>(vertices);
            this.source = vertices.getFirst();
            this.target = vertices.getLast();
            totalWeight = 0.0;
            for (int i = 0; i < vertices.size() - 1; ++i) {
                Double edgeWeight = vertices.get(i).getAdjacencyMap().get(vertices.get(i + 1));
                if (edgeWeight == null) {
                    isReachableTarget = false;
                    totalWeight = null;
                    break;
                }
                totalWeight += edgeWeight;
            }
            isReachableTarget = true;
        } else {
            isReachableTarget = false;
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
        this(graph, graph.getVertex(source), graph.getVertex(target));
    }

    public Path(Graph graph, Graph.Vertex source, Graph.Vertex target) {
        graph.clearSpTree();
        HeapDijkstraSP.compute(source);
        this.source = source;
        this.target = target;
        totalWeight = target.getDistance();
        vertices = new LinkedList<>();
        retrieve(target, vertices);
    }

    /**
     * Добавляет в конец к этому пути другой путь, обновляя значение стоимости.
     * Другой путь должен начинаться с вершины, на которую заканчивается этот путь.
     *
     * @param path другой путь
     */
    public Path add(Path path) {
        if (this.length() > 0 && this.vertices.getLast() == path.vertices.getFirst()) {
            vertices.removeLast();
            vertices.addAll(path.getVertices());
            totalWeight += path.getTotalWeight();
            target = path.getTarget();
        }
        return this;
    }

    public Graph.Vertex vertexAt(int i) {
        if (vertices.size() <= i) {
            throw new IllegalArgumentException(String.format("No vertex with index %d since the path length is %d", i, vertices.size()));
        }
        if (i < 0) {
            throw new IllegalArgumentException(String.format("Negative index %d", i));
        }
        return vertices.get(i);
    }

    @SuppressWarnings("unchecked")
    public LinkedList<Graph.Vertex> getVertices(int index) {
        LinkedList<Graph.Vertex> vertices = (LinkedList<Graph.Vertex>) this.vertices.clone();
        if (index < vertices.size()) {
            vertices.subList(index + 1, vertices.size()).clear();
        }
        return vertices;
    }

    public int length() {
        return this.vertices.size();
    }

    private void retrieve(Graph.Vertex v, LinkedList<Graph.Vertex> vertices) {
        while (v.hasPredecessor()) {
            vertices.addFirst(v);
            v = v.getPredecessor();
        }
        // Вставка вершины source в путь, если вершина target из нее достижима,
        // иначе путь будет пустым
        if (v == source) {
            vertices.addFirst(v);
            isReachableTarget = true;
        } else {
            isReachableTarget = false;
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (!isReachableTarget) {
            if (source != null && target != null) {
                stringBuilder.append(String.format("Нет пути из %s в %s", source, target));
            } else {
                stringBuilder.append(String.format("Нет пути"));
            }
            return stringBuilder.toString();
        }
        stringBuilder.append(String.format("%f: ", totalWeight));
        ListIterator<Graph.Vertex> iterator = vertices.listIterator();
        stringBuilder.append(iterator.next());
        while (iterator.hasNext()) {
            stringBuilder.append("-").append(iterator.next());
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object object) {
        Path path = (Path) object;
        if (this.length() != path.length()) return false;
        for (int i = 0; i < this.length(); ++i) {
            if (this.vertices.get(i) != path.vertices.get(i)) return false;
        }
        return true;
    }

    @Override
    public int compareTo(Path path) {
        return Double.compare(this.totalWeight, path.totalWeight);
    }
}
