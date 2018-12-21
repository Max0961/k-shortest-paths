package com.github.max0961;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Graph {

    private final Vertex[] vertices;
    private ArrayList<DirectedEdge>[] adjacency;
    private int edgeCount;

    public Graph(int size) {
        vertices = new Vertex[size];
        adjacency = new ArrayList[size];
        for (int i = 0; i < size; ++i) {
            vertices[i] = new Vertex(i);
            adjacency[i] = new ArrayList<>();
        }
        this.edgeCount = 0;
    }

    public int vertexCount() {
        return vertices.length;
    }

    public int edgeCount() {
        return edgeCount;
    }

    public Vertex[] getVertices(){
        return vertices;
    }

    public ArrayList<DirectedEdge> getAdjacency(int v) {
        return adjacency[v];
    }

    public void addEdge(DirectedEdge edge) {
        adjacency[edge.from().getLabel()].add(edge);
        edgeCount++;
    }

    public void addEdge(int u, int v, double weight) {
        DirectedEdge edge = new DirectedEdge(vertices[u], vertices[v], weight);
        adjacency[edge.from().getLabel()].add(edge);
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
            adjacency[u].add(new DirectedEdge(vertices[u], vertices[v], random.nextDouble()));
        }
    }

    public String printPath(Stack<DirectedEdge> path){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Shortest path: ");
        stringBuilder.append(path.peek().fromLabel());
        for (DirectedEdge edge : path){
            stringBuilder.append("->").append(edge.toLabel());
        }
        return stringBuilder.toString();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < adjacency.length; ++i) {
            stringBuilder.append("\n");
            for (DirectedEdge edge : adjacency[i])
                stringBuilder.append(edge).append(" ");
        }
        return stringBuilder.toString();
    }
}
