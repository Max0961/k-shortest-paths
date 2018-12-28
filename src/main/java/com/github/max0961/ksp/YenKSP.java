//package com.github.max0961.ksp;
//
//import com.github.max0961.model.Graph;
//import com.github.max0961.model.Path;
//import com.github.max0961.model.Vertex;
//
//import java.util.BinaryHeap;
//
//public class YenKSP {
//    private final Path[] ksp;
//    private final BinaryHeap<Path> candidates;
//
//    public YenKSP(Graph graph, int source, int target, int k) {
//        ksp = new Path[k];
//        candidates = new BinaryHeap<Path>();
//        kShortestPaths(k, graph, graph.getVertex(source), graph.getVertex(target));
//    }
//
//    private void kShortestPaths(int K, Graph graph, Vertex source, Vertex target) {
//        ksp[0] = new Path(graph, source, target);
//        for (int k = 1; k <= K; ++k) {
//            Path previous = ksp[k - 1];
//            for (int i = 0; i < previous.length(); ++i){
//                Vertex spur = previous.vertex(i);
//                Path root = new Path(previous.vertices(i));
//                for(Path p : ksp){
//                    if (root.vertices() == p.vertices(i));
//                    graph.removeEdge(i, i + 1);
//                }
//                for(Vertex v : root.vertices()){
//                    graph.getVertex(v)
//                }
//            }
//            Path rootPath = ksp[k - 1];
//            //LinkedList<DirectedEdge> removedEdges = new LinkedList<DirectedEdge>();
////            for (int i = 1; i < previousPath.length() - 1; i++){
////                graph.removeEdge(previousPath.getEdgeAt(i));
////                candidates.add(new Path(graph, source, target));
////                graph.addEdge(previousPath.getEdgeAt(i));
////            }
////            ksp[k] = candidates.poll();
////            candidates.clearTreeData();
//        }
//    }
//
//    @Override
//    public String toString(){
//        StringBuilder stringBuilder = new StringBuilder();
//        for (Path path : ksp){
//            stringBuilder.append(path);
//        }
//        return stringBuilder.toString();
//    }
//}
