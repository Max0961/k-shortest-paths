package com.github.max0961;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Graph {

    private final Vertex[] vertices;

    public Graph(int size) {
        vertices = new Vertex[size];
        for (int i = 0; i < size; ++i) vertices[i] = new Vertex(i);
    }

    public int size() {
        return vertices.length;
    }

    public Vertex vertex(int label) {
        return vertices[label];
    }

    public Vertex[] vertices() {
        return vertices;
    }

    public int edgeNumber() {
        int total = 0;
        for (Vertex v : vertices) {
            total += v.adjacency().size();
        }
        return total;
    }

    public void addEdge(int u, int v, double weight) {
        vertices[u].addEdge(vertices[v], weight);
    }

    public void readFromFile(String fileName) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String string;

            while ((string = bufferedReader.readLine()) != null) {
                String[] edgeDescription = string.split("\\s");
                if (edgeDescription.length == 3) {
                    addEdge(Integer.parseInt(edgeDescription[0]),
                            Integer.parseInt(edgeDescription[1]),
                            Double.parseDouble(edgeDescription[2]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateRandomGraph(int edges) {
        Random random = new Random();
        for (int i = 0; i < edges; ++i) {
            int u, v;
            do {
                u = Math.abs(random.nextInt()) % vertices.length;
                v = Math.abs(random.nextInt()) % vertices.length;
            }
            while (u == v);
            addEdge(u, v, random.nextDouble());
        }
    }

    public String printPath(ArrayList<DirectedEdge> path) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(path.get(path.size() - 1).from().label());
        for (int i = path.size() - 1; i >= 0; --i) {
            stringBuilder.append("->").append(path.get(i).to().label());
        }
        return stringBuilder.toString();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < vertices.length; ++i) {
            stringBuilder.append("Vertex ").append("\"").append(i).append("\":");
            for (Map.Entry<Vertex, Double> entry : vertices[i].adjacency().entrySet()) {
                stringBuilder.append(" to ").append("\"").append(entry.getKey().label()).append("\"")
                        .append(" w = ").append(String.format("%.2f", entry.getValue())).append("; ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
