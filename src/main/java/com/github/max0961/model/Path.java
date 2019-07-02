package com.github.max0961.model;

import com.github.max0961.model.util.HeapDijkstraSP;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Класс Path представляет путь как связный список вершин
 *
 * @see Graph.Vertex
 */
public class Path implements Comparable<Path> {
    @Getter
    private LinkedList<Graph.Vertex> vertices;
    @Getter
    private Graph.Vertex source;
    @Getter
    private Graph.Vertex target;
    @Getter
    private double totalWeight;
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
            computeTotalWeight();
        } else {
            isReachableTarget = false;
        }
    }

    /**
     * Путь, расчитанный от источника до цели
     *
     * @param graph  граф, где расчитывается путь
     * @param source метка источника
     * @param target метка цели
     */
    public Path(Graph graph, String source, String target) {
        this(graph, graph.getVertex(source), graph.getVertex(target));
    }

    /**
     * Путь, расчитанный от источника до цели
     *
     * @param graph  граф, где расчитывается путь
     * @param source источник
     * @param target цель
     */
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
     * Скопированный путь
     *
     * @param path копируемый путь
     */
    public Path(Path path) {
        this.vertices = new LinkedList<>(path.vertices);
        this.source = path.source;
        this.target = path.target;
        this.totalWeight = path.totalWeight;
        this.isReachableTarget = path.isReachableTarget;
    }

    private void computeTotalWeight() {
        totalWeight = 0.0;
        for (int i = 0; i < vertices.size() - 1; ++i) {
            if (!vertices.get(i).getAdjacencyMap().containsKey(vertices.get(i + 1))){
                totalWeight = Double.MAX_VALUE;
                isReachableTarget = false;
                break;
            }
            totalWeight += vertices.get(i).getAdjacencyMap().get(vertices.get(i + 1));
        }
        isReachableTarget = true;
    }

    public ArrayList<DirectedEdge> getEdges() {
        ArrayList<DirectedEdge> edges = new ArrayList<>(vertices.size() - 1);
        for (int i = 0; i < vertices.size() - 1; ++i) {
            edges.add(new DirectedEdge(vertices.get(i), vertices.get(i + 1)));
        }
        return edges;
    }

    /**
     * Добавляет в конец к этому пути другой путь, обновляя значение веса.
     *
     * @param path добавляемый путь
     */
    public void add(Path path) {
        if (this.vertices.getLast() == path.vertices.getFirst()) {
            vertices.removeLast();
            vertices.addAll(path.getVertices());
            totalWeight += path.getTotalWeight();
            target = path.getTarget();
        } else {
            vertices.addAll(path.getVertices());
            computeTotalWeight();
        }
    }

    /**
     * Добавляет в конец к этому пути ребро, обновляя значение веса,
     * только в том случае, если это ребро начинается с вершины, на которую заканчивается путь
     *
     * @param edge добавляемое ребро
     */
    public void add(DirectedEdge edge) {
        if (vertices.getLast() == edge.getSource()) {
            vertices.add(edge.getTarget());
            totalWeight += edge.getWeight();
            target = edge.getTarget();
        }
    }

    public Graph.Vertex vertexAt(int i) throws IllegalArgumentException {
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

    /**
     * Возвращает количество вершин в пути
     *
     * @return колисество вершин
     */
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

    public boolean hasCycle() {
        HashSet<Graph.Vertex> set = new HashSet<>();
        set.addAll(this.getVertices());
        return this.length() != set.size();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (!isReachableTarget) {
            if (source != null && target != null) {
                stringBuilder.append(String.format("Нет пути из %s в %s", source, target));
            } else {
                stringBuilder.append("Нет пути");
            }
            return stringBuilder.toString();
        }
        //stringBuilder.append(String.format("%f: ", totalWeight));
        ListIterator<Graph.Vertex> iterator = vertices.listIterator();
        stringBuilder.append(iterator.next());
        while (iterator.hasNext()) {
            stringBuilder.append("–").append(iterator.next());
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
