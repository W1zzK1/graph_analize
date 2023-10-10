package me.gorbunov;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Graph<String, DefaultWeightedEdge> graph = new DirectedWeightedPseudograph<>(DefaultWeightedEdge.class);

        graph.addVertex("0");
        graph.addVertex("1");
        graph.addVertex("2");
        graph.addVertex("3");
        graph.addVertex("4");
        graph.addVertex("5");


        DefaultWeightedEdge e0 = graph.addEdge("0", "0");
        graph.setEdgeWeight(e0, 0.7);

        DefaultWeightedEdge e1 = graph.addEdge("0", "1");
        graph.setEdgeWeight(e1, 0.3);

        DefaultWeightedEdge e2 = graph.addEdge("1", "0");
        graph.setEdgeWeight(e2, 0.5);

        DefaultWeightedEdge e3 = graph.addEdge("1", "2");
        graph.setEdgeWeight(e3, 0.3);

        DefaultWeightedEdge e4 = graph.addEdge("1", "3");
        graph.setEdgeWeight(e4, 0.2);

        DefaultWeightedEdge e5 = graph.addEdge("2", "2");
        graph.setEdgeWeight(e5, 0.7);

        DefaultWeightedEdge e6 = graph.addEdge("2", "4");
        graph.setEdgeWeight(e6, 0.3);

        DefaultWeightedEdge e7 = graph.addEdge("3", "3");
        graph.setEdgeWeight(e7, 0.3);

        DefaultWeightedEdge e8 = graph.addEdge("3", "4");
        graph.setEdgeWeight(e8, 0.2);

        DefaultWeightedEdge e9 = graph.addEdge("3", "5");
        graph.setEdgeWeight(e9, 0.5);

        DefaultWeightedEdge e10 = graph.addEdge("4", "2");
        graph.setEdgeWeight(e10, 0.3);

        DefaultWeightedEdge e11 = graph.addEdge("4", "4");
        graph.setEdgeWeight(e11, 0.7);

        DefaultWeightedEdge e12 = graph.addEdge("5", "5");
        graph.setEdgeWeight(e12, 1);

//        AllDirectedPaths<String, DefaultWeightedEdge> allDirectedPaths = new AllDirectedPaths<>(graph);
//        var a = allDirectedPaths.getAllPaths("0", "4", true, null);
//        for (GraphPath<String, DefaultWeightedEdge> b: a) {
//            System.out.println(b);
//            System.out.println(b.getWeight());
//            System.out.println("**********************");
//        }

//        Set<DefaultWeightedEdge> edges = graph.edgeSet();
        AllDirectedPaths<String, DefaultWeightedEdge> allPaths = new AllDirectedPaths<>(graph);
        List<GraphPath<String, DefaultWeightedEdge>> paths = allPaths.getAllPaths(
                "0",
                "4",
                false,
                3);
//
//        for (GraphPath<String, DefaultWeightedEdge> path : paths) {
//            List<DefaultWeightedEdge> edgesInPath = path.getEdgeList();
//            path.getWeight();
//            for (DefaultWeightedEdge edge : edgesInPath) {
//                System.out.println(edge);
//            }
//        }

//        System.out.println(paths.get(0));
        Map<GraphPath<String, DefaultWeightedEdge>, Double> pathMap = new HashMap<>();

        for (GraphPath<String, DefaultWeightedEdge> path : paths) {
//            System.out.println(path);
            double weightFinal = 1.0;
            List<DefaultWeightedEdge> edgesInPath = path.getEdgeList();
            for (DefaultWeightedEdge edge : edgesInPath) {
                double weight = graph.getEdgeWeight(edge);
//                System.out.println(edge + " " + weight);
                weightFinal *= weight;
            }
            pathMap.put(path, weightFinal);
        }

        System.out.println(pathMap);

    }
}