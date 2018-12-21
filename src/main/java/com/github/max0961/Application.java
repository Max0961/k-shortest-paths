package com.github.max0961;

public class Application
{
    public static void main( String[] args )
    {
        Graph graph = new Graph(10);
        graph.generateRandomGraph(40);
        SimpleDijkstra dijkstra = new SimpleDijkstra(graph, 0);

        System.out.println(graph);
        System.out.println(graph.printPath(dijkstra.pathTo(5)));
    }
}
