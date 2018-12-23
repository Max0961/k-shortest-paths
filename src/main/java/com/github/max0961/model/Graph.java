package com.github.max0961.model;

import java.io.*;
import java.util.*;

/**
 * Sets the vertex array and builds or removes the edges.
 *
 * @see Vertex
 * @see DirectedEdge
 */

public class Graph {
    private Vertex[] vertices;

    public Graph(int size) {
        vertices = new Vertex[size];
        for (int i = 0; i < vertices.length; ++i){
            vertices[i] = new Vertex(i);
        }
    }

    public Graph(String fileName){
        readFromFile(fileName);
    }

    public int size() {
        return vertices.length;
    }

    public int edgeNumber() {
        int total = 0;
        for (Vertex v : vertices) {
            total += v.adjacency().size();
        }
        return total;
    }

    public Vertex vertex(int label) {
        validateLabel(label);
        return vertices[label];
    }

    public boolean hasEdge(int either, int other) {
        validateLabel(either);
        validateLabel(other);
        return vertices[either].adjacency().containsKey(vertices[other])
                || vertices[other].adjacency().containsKey(vertices[either]);
    }

    public void addEdge(int from, int to, double weight) {
        validateLabel(from);
        validateLabel(to);
        vertices[from].addEdgeTo(vertices[to], weight);
    }

    public void removeEdge(int from, int to) {
        validateLabel(from);
        validateLabel(to);
        vertices[from].removeEdgeTo(vertices[to]);
    }

    public void clear() {
        for (Vertex vertex : vertices) {
            vertex.clear();
        }
    }

    public String printPath(int source, int target) {
        validateLabel(source);
        validateLabel(target);
        StringBuilder stringBuilder = new StringBuilder();
        buildPathString(vertices[source], vertices[target], stringBuilder);
        return stringBuilder.toString();
    }

    private void buildPathString(Vertex s, Vertex v, StringBuilder sb) {
        if (v == s) sb.append(String.format("%d", s.label()));
        else if (!v.hasPredecessor()) sb.append(String.format("no path from \"%d\" to \"%d\"", s.label(), v.label()));
        else {
            buildPathString(s, v.getPredecessor(), sb);
            sb.append(String.format("->%d", v.label()));
        }
    }

    public void validateLabel(int label) {
        int size = this.size();
        if (label < 0 || label >= size) {
            throw new IllegalArgumentException(String.format("vertex \"%d\" is not number between 0 and %d", label, size - 1));
        }
    }

    public void readFromFile(String fileName) {
        int size = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String string = bufferedReader.readLine();
            vertices = new Vertex[Integer.parseInt(string)];

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

    public void saveToFile(String filename) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filename));
            writer.write(vertices.length + "\n");
            for (Vertex vertex : vertices) {
                String label = Integer.toString(vertex.label());
                HashMap<Vertex, Double> adjacency = vertex.adjacency();
                for (Map.Entry<Vertex, Double> entry : adjacency.entrySet()) {
                    writer.write(String.format("%\td%\td%f\n", vertex.label(), entry.getKey().label(), entry.getValue()));
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
                from = Math.abs(random.nextInt()) % vertices.length;
                to = Math.abs(random.nextInt()) % vertices.length;
            }
            while (from == to || hasEdge(from, to));
            addEdge(from, to, random.nextDouble());
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < vertices.length; ++i) {
            stringBuilder.append(String.format("\"%d\": ", i));
            for (Map.Entry<Vertex, Double> entry : vertices[i].adjacency().entrySet()) {
                stringBuilder.append(String.format("to \"%d\" ", entry.getKey().label()))
                        .append(String.format("w=%.2f", entry.getValue())).append("; ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
