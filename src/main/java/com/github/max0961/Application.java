package com.github.max0961;

public class Application
{
    public static void main( String[] args )
    {
        Graph graph = new Graph(1000);
        graph.generateRandomGraph(4000);
        SimpleDijkstra dijkstra = new SimpleDijkstra(graph, 0);

        System.out.println(graph);
        System.out.println(graph.edgeNumber());
        System.out.println(dijkstra.hasPathTo(5));
        System.out.println(graph.printPath(dijkstra.pathTo(5)));
        System.out.println(dijkstra.distanceTo(5));
    }
}
