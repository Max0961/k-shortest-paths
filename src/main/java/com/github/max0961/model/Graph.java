package com.github.max0961.model;

import com.github.max0961.model.util.BellmanFordSP;
import com.github.max0961.model.util.HeapDijkstraSP;
import lombok.Getter;
import lombok.Setter;
import lombok.var;

import java.io.*;
import java.text.NumberFormat;
import java.util.*;

/**
 * Класс реализует произвольный граф с таким ограничением, что кратные ребра запрещены.
 *
 * @see Vertex
 */

public class Graph {
    private final HashMap<String, Vertex> vertices = new LinkedHashMap<>();
    private int negativeEdgeCount = 0;

    /**
     * Конструктор без параметров
     */
    public Graph() {
    }

    /**
     * Создает граф, читая его из файла
     *
     * @param fileName имя файла
     */
    public Graph(String fileName) throws Exception {
        readFromFile(fileName);
    }

    public int getNegativeEdgeNumber() {
        return negativeEdgeCount;
    }


    public int verticesNumber() {
        return vertices.size();
    }

    public Collection<Vertex> getVertices() {
        return vertices.values();
    }

    public boolean hasVertex(String label) {
        return vertices.containsKey(label);
    }

    public Vertex getVertex(String label) throws IllegalArgumentException {
        validateLabel(label);
        return vertices.get(label);
    }

    /**
     * Создает новую вершину в графе, если вершина с такой меткой не имеется в графе
     *
     * @param label метка вершины
     * @return true, если вершина создана, false в противном случае
     */
    public boolean addVertex(String label) {
        if (vertices.containsKey(label) || label.length() == 0) {
            return false;
        }
        vertices.put(label, new Vertex(label));
        return true;
    }

    /**
     * Добавляет вершину в граф, если эта вершина не имеется в графе
     *
     * @param vertex вершина
     * @return true, если вершина добавлена, false в противном случае
     */
    public boolean addVertex(Vertex vertex) {
        if (vertices.containsKey(vertex.getLabel())) {
            return false;
        }
        vertices.put(vertex.getLabel(), vertex);
        return true;
    }

    /**
     * Удаляет вершину из графа
     *
     * @param label метка вершины
     */
    public void removeVertex(String label) throws IllegalArgumentException {
        validateLabel(label);
        vertices.remove(label);
    }


    public int edgeNumber() {
        int total = 0;
        for (Vertex v : vertices.values()) {
            total += v.getAdjacencyMap().size();
        }
        return total;
    }

    public ArrayList<DirectedEdge> getEdges() {
        ArrayList<DirectedEdge> edges = new ArrayList<>(edgeNumber());
        for (Vertex vertex : vertices.values()) {
            edges.addAll(vertex.getEdges());
        }
        return edges;
    }

    public boolean hasEdge(String source, String target) {
        return hasEdge(this.getVertex(source), this.getVertex(target));
    }

    public static boolean hasEdge(Vertex source, Vertex target) {
        return source.getAdjacencyMap().containsKey(target);
    }

    public static boolean hasEdge(DirectedEdge edge) {
        return edge.getSource().getAdjacencyMap().containsKey(edge.getTarget());
    }

    public boolean addEdge(String source, String target, double weight) {
        return addEdge(this.getVertex(source), this.getVertex(target), weight);
    }

    public boolean addEdge(Vertex source, Vertex target, double weight) {
        if (source.addEdgeTo(target, weight)) {
            if (weight < 0) ++negativeEdgeCount;
            return true;
        }
        return false;
    }

    public boolean addEdge(DirectedEdge edge) {
        return addEdge(edge.getSource(), edge.getTarget(), edge.getWeight());
    }

    public boolean removeEdge(String source, String target) {
        return removeEdge(this.getVertex(source), this.getVertex(target));
    }

    public boolean removeEdge(Vertex source, Vertex target) {
        Double weight = source.getAdjacencyMap().get(target);
        if (source.removeEdgeTo(target)) {
            if (weight < 0) --negativeEdgeCount;
            return true;
        }
        return false;
    }

    public boolean removeEdge(DirectedEdge edge) {
        return edge.getSource().removeEdgeTo(edge.getTarget());
    }


    public boolean formSpTree(Vertex root) {
        if (negativeEdgeCount == 0) {
            HeapDijkstraSP.compute(root);
            return true;
        }
        return BellmanFordSP.compute(this, root);
    }

    public void clearSpTree() {
        for (Vertex vertex : vertices.values()) {
            vertex.clearSpTree();
        }
    }

    /**
     * Меняет направление каждого ребра, временная сложность O(m)
     */
    public void reverse() {
        HashMap<Vertex, ArrayList<DirectedEdge>> allEdges = new HashMap<>();
        for (Vertex vertex : vertices.values()) {
            allEdges.put(vertex, vertex.getEdges());
            for (DirectedEdge edge : allEdges.get(vertex)) {
                vertex.removeEdgeTo(edge.getTarget());
            }
        }
        for (Vertex vertex : vertices.values()) {
            for (DirectedEdge edge : allEdges.get(vertex)) {
                edge.inverse();
                this.addEdge(edge);
            }
        }
    }

    public void readFromFile(String fileName) throws IOException {
        vertices.clear();
        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String[] description;
        String currentRow;
        while ((currentRow = reader.readLine()) != null) {
            description = currentRow.split("\t+");
            // читает вершину
            if (description.length == 1) {
                addVertex(description[0]);
            }
            // читает исток, сток и вес ребра
            if (description.length == 3) {
                addEdge(description[0], description[1], Double.parseDouble(description[2]));
            }

            if (description.length == 2) {
                addEdge(description[0], description[1], 1);
            }
        }
        reader.close();
    }

    public void saveToFile(String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        // записывает спискок всех вершин
        for (Vertex vertex : vertices.values()) {
            writer.write(vertex + "\n");
        }
        // записывает исток, сток и вес ребра через символ табуляции
        for (Vertex vertex : vertices.values()) {
            for (Map.Entry<Vertex, Double> entry : vertex.getAdjacencyMap().entrySet()) {
                writer.write(vertex + "\t" + entry.getKey() + "\t" + entry.getValue() + "\n");
            }
        }
        writer.close();
    }

    public void generateRandomGraph(boolean directed, boolean constantDegree, int vertexNumber, float density) {
        float meanDegree = (vertexNumber - 1) * density;
        generateRandomGraph(directed, constantDegree, vertexNumber, Math.round(meanDegree));
    }

    /**
     * Генерирует случайный граф за время O(n^2)
     *
     * @param verticesNumber
     * @param meanDegree
     */
    public void generateRandomGraph(boolean isDirected, boolean hasConstantDegree, int verticesNumber, int meanDegree)
            throws IllegalArgumentException {
        vertices.clear();
        if (verticesNumber == 0) return;
        if (meanDegree > verticesNumber - 1) throw new IllegalArgumentException("Exceeded maximum number of edges");

        // Создание вершин в графе
        for (int i = 0; i < verticesNumber; ++i) {
            Vertex vertex = new Vertex(Integer.toString(i + 1));
            vertices.put(vertex.getLabel(), vertex);
        }

        Random random = new Random();

        int[] bounds = new int[verticesNumber + 1];
        for (int i = 0; i < bounds.length; ++i) {
            bounds[i] = i * meanDegree;
        }
        if (!hasConstantDegree && meanDegree > 0) {
            for (int i = 1; i < verticesNumber; ++i) {
                int direction = (int) Math.signum(random.nextDouble() - 0.5);
                int bound = Math.min(verticesNumber - 1 - meanDegree, meanDegree);
                int shift = random.nextInt(bound + 1);
                bounds[i] += shift * direction;
            }
        }

        // Количество входящих и исходящих ребер для каждой вершины
        int i = 0;
        for (Vertex vertex : vertices.values()) {
            vertex.setCounter(bounds[i + 1] - bounds[i]);
            ++i;
        }

        ArrayList<Vertex> adjacencyList = new ArrayList<>(verticesNumber);
        for (Vertex source : vertices.values()) {
            adjacencyList.clear();
            for (Vertex vertex : vertices.values()) {
                if (source != vertex && vertex.getCounter() > 0) {
                    adjacencyList.add(vertex);
                }
            }
            int bound = source.getCounter();
            mix(adjacencyList, random);
            for (i = 0; i < bound; ++i) {
                if (adjacencyList.isEmpty()) continue;
                Vertex target = adjacencyList.remove(adjacencyList.size() - 1);
                double w = random.nextDouble();
                source.addEdgeTo(target, w);
                if (!isDirected) {
                    target.addEdgeTo(source, w);
                }
                source.decCounter();
                target.decCounter();
            }
        }
    }

    private void mix(ArrayList<Vertex> list, Random random) {
        for (int i = 0; i < list.size(); ++i) {
            swap(i, random.nextInt(list.size()), list);
        }
    }

    private static void swap(int a, int b, ArrayList<Vertex> array) {
        var c = array.get(a);
        array.set(a, array.get(b));
        array.set(b, c);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("|V| = %d, |E| = %d\n", this.verticesNumber(), this.edgeNumber()));
        if (this.edgeNumber() > 1e6) {
            stringBuilder.append("Too much edges");
            return stringBuilder.toString();
        }
        Object[] vertexArray = vertices.keySet().toArray();
        Arrays.sort(vertexArray);
        for (Object object : vertexArray) {
            String label = (String) object;
            stringBuilder.append(label).append(" {");
            ArrayList<DirectedEdge> edges = vertices.get(label).getEdges();
            edges.sort((e1, e2) -> e1.getTarget().label.compareTo(e2.getTarget().label));
            for (int i = 0; i < edges.size(); ++i) {
                stringBuilder.append(edges.get(i).getTarget()).
                        append("(").append(edges.get(i).getWeight()).append(")");
                if (i < edges.size() - 1) {
                    stringBuilder.append(", ");
                }
            }
            stringBuilder.append("}\n");
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    private void validateLabel(String label) throws IllegalArgumentException {
        if (vertices.size() == 0) {
            throw new IllegalArgumentException(String.format("The graph does not have any vertices so it has no vertex with label %s", label));
        }
        if (!vertices.containsKey(label)) {
            throw new IllegalArgumentException(String.format("The graph has no vertex with label %s", label));
        }
    }

    /**
     * Класс содержит метку, ссылки на своих соседей, и поля для алгоритмов
     */
    public static class Vertex implements Comparable<Vertex> {
        @Getter
        private final String label;
        @Getter
        private final HashMap<Vertex, Double> adjacencyMap;
        @Getter
        @Setter
        private double distance;
        @Getter
        @Setter
        private Vertex predecessor;
        @Getter
        @Setter
        private int counter = 0;

        public Vertex(String label) {
            this.label = label;
            adjacencyMap = new LinkedHashMap<>();
            distance = Double.MAX_VALUE;
        }

        public void incCounter() {
            ++counter;
        }

        public void decCounter() {
            --counter;
        }

        public void reset() {
            counter = 0;
        }

        public boolean hasPredecessor() {
            return predecessor != null;
        }

        public ArrayList<DirectedEdge> getEdges() {
            ArrayList<DirectedEdge> edges = new ArrayList<>(adjacencyMap.size());
            for (Map.Entry<Graph.Vertex, Double> entry : adjacencyMap.entrySet()) {
                edges.add(new DirectedEdge(this, entry.getKey(), entry.getValue()));
            }
            return edges;
        }

        public boolean addEdgeTo(Vertex vertex, double weight) {
            if (adjacencyMap.containsKey(vertex)) {
                return false;
            }
            this.adjacencyMap.put(vertex, weight);
            return true;
        }

        public boolean removeEdgeTo(Vertex vertex) {
            if (!adjacencyMap.containsKey(vertex)) {
                return false;
            }
            adjacencyMap.remove(vertex);
            return true;
        }

        public void clearSpTree() {
            distance = Double.MAX_VALUE;
            predecessor = null;
        }

        @Override
        public String toString() {
            return label;
        }

        @Override
        public int hashCode() {
            return label.hashCode();
        }

        @Override
        public int compareTo(Vertex vertex) {
            return Double.compare(this.distance, vertex.distance);
        }
    }
}
