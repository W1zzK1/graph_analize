package me.gorbunov.algorithms;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chislitel {
    public List<GraphPath<String, DefaultWeightedEdge>> getListAllPaths(Graph<String, DefaultWeightedEdge> graph, String startVertex, String targetVertex) {
        AllDirectedPaths<String, DefaultWeightedEdge> allPaths = new AllDirectedPaths<>(graph);
        return allPaths.getAllPaths(
                startVertex,
                targetVertex,
                true,
                null);
    }

    public double FindChislitel(Graph<String, DefaultWeightedEdge> graph, String startVertex, String finishVertex, List<String> dangerousVertices) {
        Map<GraphPath<String, DefaultWeightedEdge>, Double> pathMap = new HashMap<>();
        List<GraphPath<String, DefaultWeightedEdge>> paths = getListAllPaths(graph, startVertex, finishVertex);

        for (GraphPath<String, DefaultWeightedEdge> path : paths) {
            double weightFinal = 1.0;
            List<DefaultWeightedEdge> edgesInPath = path.getEdgeList();
            for (DefaultWeightedEdge edge : edgesInPath) {
                double weight = graph.getEdgeWeight(edge);
                weightFinal *= weight;
            }
            pathMap.put(path, weightFinal);
        }
        // Получить все петли в графе
        List<DefaultWeightedEdge> allLoops = graph.edgeSet().stream()
                .filter(e -> graph.getEdgeSource(e).equals(graph.getEdgeTarget(e)))
                .toList();

        // Определить опасные вершины
        Map<DefaultWeightedEdge, Double> weigthList = new HashMap<>();

        double chislitel = 0;

        for (GraphPath<String, DefaultWeightedEdge> path : paths) {
//            System.out.println(path);
            // Получить все вершины на данном пути
            List<String> verticesInPath1 = path.getVertexList();

            // Получить все петли, которые находятся на данном пути или касаются вершин на данном пути
            List<DefaultWeightedEdge> loopsInPath = allLoops.stream()
                    .filter(loop -> verticesInPath1.contains(graph.getEdgeSource(loop)))
                    .toList();
//            System.out.println("все петли на данном пути = " + loopsInPath);
            // Получить все петли, которые не находятся на данном пути и не касаются вершин на данном пути
            List<DefaultWeightedEdge> loopsNotInPath = allLoops.stream()
                    .filter(loop -> !loopsInPath.contains(loop) && !dangerousVertices.contains(graph.getEdgeSource(loop)))
                    .toList();

            double edgeWeight = 1.0;
            for (var k : path.getEdgeList()) {
                edgeWeight *= graph.getEdgeWeight(k);
            }

            if (loopsNotInPath.isEmpty()) {
                chislitel += edgeWeight;
            } else {
                for (DefaultWeightedEdge loop : loopsNotInPath) {
//                    System.out.println("зашел в цикл");
                    double weight = graph.getEdgeWeight(loop);
                    weigthList.put(loop, 1 - weight);
//                    System.out.println("Weight of loop " + loop + " = " + weight);
                    chislitel += edgeWeight * (1 - weight);
                }
            }
        }
        return chislitel;
    }
}
