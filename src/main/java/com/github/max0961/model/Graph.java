package com.github.max0961.model;

import java.io.*;
import java.text.NumberFormat;
import java.util.*;

/**
 * Содержит множество вершин и методы добавления и удаления ребра
 *
 * @see Vertex
 */

public class Graph {
    private final HashMap<String, Vertex> vertices;

    /**
     * Создает граф с указанным количесвом вершин, метки - целые числа от 0
     * @param verticesNumber Количество вершин
     */
    public Graph(int verticesNumber) {
        vertices = new HashMap(verticesNumber);
        for (int i = 0; i < verticesNumber; ++i) {
            Vertex vertex = new Vertex(Integer.toString(i));
            vertices.put(vertex.label(), vertex);
        }
    }

    /**
     * Создает граф с, читая его из файла
     * @param fileName Имя файла
     */
    public Graph(String fileName) {
        vertices = new HashMap();
        readFromFile(fileName);
    }

    public int verticesNumber() {
        return vertices.size();
    }

    /**
     * Создает вершину в графе, если такую вершину он не сожержит нет
     * @param label Метка вершины
     * @return true, если вершина создана, false в противном случае
     */
    public boolean addVertex(String label) {
        if (vertices.containsKey(label)) {
            return false;
        }
        vertices.put(label, new Vertex(label));
        return true;
    }

    public void removeVertex(String label) {
        vertices.remove(label);
    }

    public Vertex getVertex(String label) {
        validateLabel(label);
        return vertices.get(label);
    }

    public HashMap<String, Vertex> getVertices() {
        return vertices;
    }

    public int edgeNumber() {
        int total = 0;
        for (Vertex v : vertices.values()) {
            total += v.adjacency().size();
        }
        return total;
    }

    public boolean hasEdge(String from, String to) {
        validateLabel(from);
        validateLabel(to);
        return vertices.get(from).adjacency().containsKey(vertices.get(to));
    }

    public void addEdge(String from, String to, double weight) {
        validateLabel(from);
        validateLabel(to);
        vertices.get(from).addEdgeTo(vertices.get(to), weight);
    }

    public void removeEdge(String from, String to) {
        validateLabel(from);
        validateLabel(to);
        vertices.get(from).removeEdgeTo(vertices.get(to));
    }

    public void clearTreeData() {
        for (Vertex vertex : vertices.values()) {
            vertex.clearTreeData();
        }
    }

    public void readFromFile(String fileName) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String string;
            NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);

            while ((string = bufferedReader.readLine()) != null) {
                String[] edgeDescription = string.split("\t+");
                if (edgeDescription.length == 3) {
                    addVertex(edgeDescription[0]);
                    addVertex(edgeDescription[1]);
                    addEdge(edgeDescription[0],
                            edgeDescription[1],
                            format.parse(edgeDescription[2]).doubleValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Вершины без соседей (недостижимые из любой другой вершины) не будут сохранены,
     * так как файл представляет список рёбер
     */
    public void saveToFile(String filename) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filename));
            for (Vertex vertex : vertices.values()) {
                HashMap<Vertex, Double> adjacency = vertex.adjacency();
                for (Map.Entry<Vertex, Double> entry : adjacency.entrySet()) {
                    String s = String.format("%s\t%s\t%f\n", vertex, entry.getKey(), entry.getValue());
                    writer.write(s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                Objects.requireNonNull(writer).close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void generateRandomDirectedGraph(int edgeNumber) {
        Random random = new Random();
        for (int i = 0; i < edgeNumber; ++i) {
            int from, to;
            do {
                from = Math.abs(random.nextInt()) % vertices.size();
                to = Math.abs(random.nextInt()) % vertices.size();
            }
            while (from == to || hasEdge(Integer.toString(from), Integer.toString(to)));
            addEdge(Integer.toString(from), Integer.toString(to), random.nextDouble());
        }
    }

    public void generateRandomUndirectedGraph(int edgeNumber) {
        Random random = new Random();
        for (int i = 0; i < edgeNumber; ++i) {
            int either, other;
            do {
                either = Math.abs(random.nextInt()) % vertices.size();
                other = Math.abs(random.nextInt()) % vertices.size();
            }
            while (either == other || hasEdge(Integer.toString(either), Integer.toString(other))
                    || hasEdge(Integer.toString(either), Integer.toString(other)));
            double w = random.nextDouble();
            addEdge(Integer.toString(either), Integer.toString(other), w);
            addEdge(Integer.toString(other), Integer.toString(either), w);
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Граф с |V| = %d и |E| = %d.", this.verticesNumber(), this.edgeNumber());
        for (int i = 0; i < vertices.size(); ++i) {
            stringBuilder.append(String.format("%d: ", i));
            for (Map.Entry<Vertex, Double> entry : vertices.get(i).adjacency().entrySet()) {
                stringBuilder.append(String.format("to %d ", entry.getKey().label()))
                        .append(String.format("w=%.2f", entry.getValue())).append("; ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    private void validateLabel(String label) {
        int size = this.verticesNumber();
        if (size == 0) {
            throw new IllegalArgumentException(String.format("Vertex %s can not be found in the graph because it is empty", label));
        }
        if (!vertices.containsKey(label)) {
            throw new IllegalArgumentException(String.format("Vertex %s can not be found in the graph", label, size - 1));
        }
    }
}
