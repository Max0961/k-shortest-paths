package com.github.max0961.model;

import lombok.Getter;

import java.io.*;
import java.text.NumberFormat;
import java.util.*;

/**
 * Содержит множество вершин и методы добавления и удаления ребра
 *
 * @see Vertex
 */

public class Graph {
    private static final int LIMIT = 100;
    @Getter
    private final HashMap<String, Vertex> vertices;

    /**
     * Создает граф с указанным количесвом вершин, метки - натуральные числа
     *
     * @param verticesNumber количество вершин
     */
    public Graph(int verticesNumber) {
        vertices = new LinkedHashMap<>();
        for (int i = 1; i <= verticesNumber; ++i) {
            Vertex vertex = new Vertex(Integer.toString(i));
            vertices.put(vertex.getLabel(), vertex);
        }
    }

    /**
     * Создает граф, читая его из файла
     *
     * @param fileName имя файла
     */
    public Graph(String fileName) {
        vertices = new LinkedHashMap();
        readFromFile(fileName);
    }


    public int verticesNumber() {
        return vertices.size();
    }

    public Vertex vertex(String label) {
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
            total += v.getAdjacency().size();
        }
        return total;
    }

    public boolean hasEdge(String source, String target) {
        validateLabel(source);
        validateLabel(target);
        return hasEdge(this.vertex(source), this.vertex(target));
    }

    public static boolean hasEdge(Vertex source, Vertex target) {
        return source.getAdjacency().containsKey(target);
    }

    public void addEdge(String source, String target, double weight) {
        validateLabel(source);
        validateLabel(target);
        addEdge(this.vertex(source), this.vertex(target), weight);
    }

    public static void addEdge(Vertex source, Vertex target, double weight) {
        source.addEdgeTo(target, weight);
    }

    public boolean removeEdge(String source, String target) {
        validateLabel(source);
        validateLabel(target);
        return removeEdge(this.vertex(source), this.vertex(target));
    }

    public static boolean removeEdge(Vertex source, Vertex target) {
        return source.removeEdgeTo(target);
    }


    public void clearSpTreeData() {
        for (Vertex vertex : vertices.values()) {
            vertex.clearSpTreeData();
        }
    }


    public void readFromFile(String fileName) {
        try {
            NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            ArrayList<String[]> description = new ArrayList<>();
            String current;
            while ((current = bufferedReader.readLine()) != null) {
                description.add(current.split("\t+"));
                String label = description.get(description.size() - 1)[0];
                addVertex(label);
            }
            for (String[] string : description) {
                if (string.length == 3) {
                    addEdge(string[0], string[1], format.parse(string[2]).doubleValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveToFile(String fileName) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
            for (Vertex vertex : vertices.values()) {
                if (vertex.getAdjacency().size() == 0) {
                    writer.write(String.format("%s\n", vertex));
                    continue;
                }
                for (Map.Entry<Vertex, Double> entry : vertex.getAdjacency().entrySet()) {
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
            int source, target;
            do {
                source = Math.abs(random.nextInt()) % vertices.size() + 1;
                target = Math.abs(random.nextInt()) % vertices.size() + 1;
            }
            while (source == target || hasEdge(Integer.toString(source), Integer.toString(target)));
            addEdge(Integer.toString(source), Integer.toString(target), random.nextDouble());
        }
    }

    public void generateRandomUndirectedGraph(int edgeNumber) {
        Random random = new Random();
        for (int i = 0; i < edgeNumber; ++i) {
            int either, other;
            do {
                either = Math.abs(random.nextInt()) % vertices.size() + 1;
                other = Math.abs(random.nextInt()) % vertices.size() + 1;
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
        stringBuilder.append(String.format("|V| = %d и |E| = %d\n", this.verticesNumber(), this.edgesNumber()));
        if (vertices.size() > LIMIT) {
            return stringBuilder.append(String.format("Лимит %d вершин\n", LIMIT)).toString();
        }
        for (Vertex vertex : vertices.values()) {
            stringBuilder.append(String.format("%s: ", vertex));
            for (Map.Entry<Vertex, Double> entry : vertex.getAdjacency().entrySet()) {
                stringBuilder.append(String.format("->%s", entry.getKey()))
                        .append(String.format("(%.2f)", entry.getValue())).append("; ");
            }
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
            stringBuilder.append(".\n");
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    private void validateLabel(String label) {
        if (this.verticesNumber() == 0) {
            throw new IllegalArgumentException(String.format("the graph does not have any getVertices so it has no vertex with label %s", label));
        }
        if (!vertices.containsKey(label)) {
            throw new IllegalArgumentException(String.format("the graph has no vertex with label %s", label));
        }
    }
}
