package com.github.max0961.model;

import com.github.max0961.util.BellmanFordSP;
import com.github.max0961.util.HeapDijkstraSP;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.NumberFormat;
import java.util.*;

/**
 * Класс Graph реализует произвольный граф, представленный списком смежности.
 * Помимо прочих, содержит методы сохранения граф в файл, чтения графа из файла
 * и метод создания случайного графа.
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
    public Graph(String fileName) {
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

    public Vertex getVertex(String label) {
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
        if (vertices.containsKey(label)) {
            return false;
        }
        vertices.put(label, new Vertex(label));
        return true;
    }

    /**
     * Добавляет вершину в граф, если вершина с её меткой не имеется в графе
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
    public void removeVertex(String label) {
        validateLabel(label);
        vertices.remove(label);
    }


    public int edgesNumber() {
        int total = 0;
        for (Vertex v : vertices.values()) {
            total += v.getAdjacencyMap().size();
        }
        return total;
    }

    public ArrayList<DirectedEdge> getEdges() {
        ArrayList<DirectedEdge> edges = new ArrayList<>(edgesNumber());
        for (Vertex vertex : vertices.values()) {
            edges.addAll(vertex.getEdges());
        }
        return edges;
    }

    public boolean hasEdge(String source, String target) {
        validateLabel(source);
        validateLabel(target);
        return hasEdge(this.getVertex(source), this.getVertex(target));
    }

    public static boolean hasEdge(Vertex source, Vertex target) {
        return source.getAdjacencyMap().containsKey(target);
    }

    public static boolean hasEdge(DirectedEdge edge) {
        return edge.getSource().getAdjacencyMap().containsKey(edge.getTarget());
    }

    public boolean addEdge(String source, String target, double weight) {
        validateLabel(source);
        validateLabel(target);
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
        validateLabel(source);
        validateLabel(target);
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
        } else return BellmanFordSP.compute(this, root);
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
                addEdge(edge);
            }
        }
    }

    public void readFromFile(String fileName) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveToFile(String fileName) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateRandomGraph(boolean directed, boolean constantDegree, int verticesNumber, float density) {
        float meanDegree = (verticesNumber - 1) * density;
        generateRandomGraph(directed, constantDegree, verticesNumber, Math.round(meanDegree));
    }

    /**
     * Генерирует граф без петель и параллельных ребер.
     * Время O(n^2)
     *
     * @param verticesNumber
     * @param meanDegree
     */
    public void generateRandomGraph(boolean directed, boolean constantDegree, int verticesNumber, int meanDegree) {
        if (meanDegree > verticesNumber - 1) throw new IllegalArgumentException("Exceeded maximum number of edges");
        for (int i = 0; i < verticesNumber; ++i) {
            Vertex vertex = new Vertex(Integer.toString(i + 1));
            vertices.put(vertex.getLabel(), vertex);
        }

        Random random = new Random();

        int[] adjacencyLengths = new int[verticesNumber + 1];
        for (int i = 0; i < adjacencyLengths.length; ++i) {
            adjacencyLengths[i] = i * meanDegree;
        }
        if (!constantDegree) {
            for (int i = 1; i < verticesNumber; ++i) {
                int direction = (int) Math.signum(random.nextDouble() - 0.5);
                int bound = verticesNumber / meanDegree / 2;
                int shift = 0;
                if (bound > 0) shift = random.nextInt(bound + 1);
                adjacencyLengths[i] += shift * direction;
            }
        }

        int[] adjacency = new int[verticesNumber - 1];

        String source, target;
        for (int i = 0; i < verticesNumber; ++i) {
            source = Integer.toString(i + 1);
            setRandomAdjacencyArray(adjacency, i, random);
            for (int j = 0; j < adjacencyLengths[i + 1] - adjacencyLengths[i]; ++j) {
                target = Integer.toString(adjacency[j] + 1);
                vertices.get(source).addEdgeTo(vertices.get(target), random.nextDouble());
            }
        }

        if (!directed) {
            for (Vertex vertex : vertices.values()) {
                for (Map.Entry<Vertex, Double> entry : vertex.getAdjacencyMap().entrySet()) {
                    Vertex neighbor = entry.getKey();
                    double weight = entry.getValue();
                    neighbor.addEdgeTo(vertex, weight);
                }
            }
        }
    }

    private static void setRandomAdjacencyArray(int[] adj, int sourceIndex, Random random) {
        int shift = 0;
        for (int i = 0; i < adj.length; ++i) {
            if (i == sourceIndex) shift = 1;
            adj[i] = i + shift;
        }
        for (int i = 0; i < adj.length; ++i) {
            swap(i, random.nextInt(adj.length), adj);
        }
    }

    private static void swap(int a, int b, int[] array) {
        int c = array[a];
        array[a] = array[b];
        array[b] = c;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Vertex vertex : vertices.values()) {
            stringBuilder.append(vertex).append(" {");
            Iterator<Map.Entry<Vertex, Double>> iterator = vertex.getAdjacencyMap().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Vertex, Double> entry = iterator.next();
                stringBuilder.append(entry.getKey()).append(String.format("(%.6f)", entry.getValue()));
                if (iterator.hasNext()) stringBuilder.append("; ");
            }
            stringBuilder.append("}\n");
        }
        stringBuilder.append(String.format("|V| = %d и |E| = %d\n", this.verticesNumber(), this.edgesNumber()));
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    private void validateLabel(String label) {
        if (vertices.size() == 0) {
            throw new IllegalArgumentException(String.format("The graph does not have any vertices so it has no vertex with label %s", label));
        }
        if (!vertices.containsKey(label)) {
            throw new IllegalArgumentException(String.format("The graph has no vertex with label %s", label));
        }
    }

    /**
     * Вершина содержит метку, ссылки на своих соседей и веса ребер,
     * расчитанные или нет расстояние от источника
     * и ссылку на предыдущую верщину в дереве ратчайших путей
     */
    public static class Vertex implements Comparable<Vertex> {
        @Getter
        private final String label;
        @Getter
        private final HashMap<Vertex, Double> adjacencyMap;
        @Getter
        @Setter
        private Double distance;
        @Getter
        @Setter
        private Vertex predecessor;

        public Vertex(String label) {
            this.label = label;
            adjacencyMap = new LinkedHashMap<>();
            distance = Double.MAX_VALUE;
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
            return distance.compareTo(vertex.distance);
        }
    }
}
