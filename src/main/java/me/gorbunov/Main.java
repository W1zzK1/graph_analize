package me.gorbunov;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.cycle.PatonCycleBase;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.*;

import java.util.stream.Collectors;

import java.util.*;

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

        GraphAnalize graphAnalize = new GraphAnalize();

        var graphCopy = graphAnalize.cloneGraph(graph);
        System.out.println(graphCopy);

        String startVertex = "0";
        graph.removeAllEdges(startVertex, startVertex);

        var paths = graphAnalize.getListAllPaths(graph, "0", "4");

        Map<GraphPath<String, DefaultWeightedEdge>, Double> pathMap = new HashMap<>();

        for (GraphPath<String, DefaultWeightedEdge> path : paths) {
            double weightFinal = 1.0;
            List<DefaultWeightedEdge> edgesInPath = path.getEdgeList();
            for (DefaultWeightedEdge edge : edgesInPath) {
                double weight = graph.getEdgeWeight(edge);
                weightFinal *= weight;
            }
            pathMap.put(path, weightFinal);
        }

        ///System.out.println(pathMap);

        graphAnalize.addDangerState("4");
        graphAnalize.addDangerState("5");
        ///System.out.println(graphAnalize.isDangerousState("5"));

        // ребра и вершины на пути
        List<String> verticesInPath = paths.stream()
                .flatMap(p -> p.getVertexList().stream())
                .distinct()
                .toList();
        List<DefaultWeightedEdge> edgesInPath = paths.stream()
                .flatMap(p -> p.getEdgeList().stream())
                .toList();

        System.out.println(verticesInPath);
        System.out.println(edgesInPath);
//
//        // Получить все вершины и ребра, которые не находятся на пути
//        List<String> verticesNotInPath = graph.vertexSet().stream()
//                .filter(v -> !verticesInPath.contains(v))
//                .toList();
//        List<DefaultWeightedEdge> edgesNotInPath = graph.edgeSet().stream()
//                .filter(e -> !edgesInPath.contains(e))
//                .toList();
//
//        System.out.println("Vertices not in path: " + verticesNotInPath);
//        System.out.println("Edges not in path: " + edgesNotInPath);
//
//        Set<DefaultWeightedEdge> edgeSets = new HashSet<>(edgesNotInPath);


        // Получить все петли в графе
        List<DefaultWeightedEdge> allLoops = graph.edgeSet().stream()
                .filter(e -> graph.getEdgeSource(e).equals(graph.getEdgeTarget(e)))
                .toList();

        // Определить опасные вершины
        List<String> dangerousVertices = Arrays.asList("4", "5");
        ArrayList<Double> weigthList = new ArrayList<>();

        for (GraphPath<String, DefaultWeightedEdge> path : paths) {
            // Получить все вершины на данном пути
            List<String> verticesInPath1 = path.getVertexList();

            // Получить все петли, которые находятся на данном пути или касаются вершин на данном пути
            List<DefaultWeightedEdge> loopsInPath = allLoops.stream()
                    .filter(loop -> verticesInPath1.contains(graph.getEdgeSource(loop)))
                    .toList();

            // Получить все петли, которые не находятся на данном пути и не касаются вершин на данном пути
            List<DefaultWeightedEdge> loopsNotInPath = allLoops.stream()
                    .filter(loop -> !loopsInPath.contains(loop) && !dangerousVertices.contains(graph.getEdgeSource(loop)))
                    .toList();

            //System.out.println("For path " + path + ", loops not in path and not touching vertices in path: " + loopsNotInPath);

            for (DefaultWeightedEdge loop : loopsNotInPath) {
                double weight = graph.getEdgeWeight(loop);
                weigthList.add(1 - weight);
                //System.out.println("Weight of loop " + loop + ": " + weight);
            }
        }


        ArrayList<Double> mapValues = new ArrayList<>();
        for (Map.Entry<GraphPath<String, DefaultWeightedEdge>, Double> entry : pathMap.entrySet()) {
            mapValues.add(entry.getValue());
        }

//        System.out.println(weigthList);
//
//        double ans = (mapValues.get(0) * weigthList.get(1) + mapValues.get(1) * weigthList.get(0)) / 0.0315;
//        System.out.println("P[0;4] = " + ans);

    }

    public static class GraphAnalize {
        private Set<String> dangerstate = new HashSet<String>();

        public void addDangerState(String name) {
            dangerstate.add(name);
        }

        public boolean isDangerousState(String state) {
            return dangerstate.contains(state);
        }

        public Graph<String, DefaultWeightedEdge> cloneGraph(Graph<String, DefaultWeightedEdge> original) {
            Graph<String, DefaultWeightedEdge> graphCopy = new DirectedWeightedPseudograph<>(DefaultWeightedEdge.class);
            // Клонирование вершин
            for (String vertex : original.vertexSet()) {
                graphCopy.addVertex(vertex);
            }

            // Клонирование ребер
            for (DefaultWeightedEdge edge : original.edgeSet()) {
                DefaultWeightedEdge edgeCopy = graphCopy.addEdge(original.getEdgeSource(edge), original.getEdgeTarget(edge));
                double weight = original.getEdgeWeight(edge);
                graphCopy.setEdgeWeight(edgeCopy, weight);
            }
            return graphCopy;
        }

        public List<GraphPath<String, DefaultWeightedEdge>> getListAllPaths(Graph<String, DefaultWeightedEdge> graph, String startVertex, String targetVertex){
            AllDirectedPaths<String, DefaultWeightedEdge> allPaths = new AllDirectedPaths<>(graph);
            return allPaths.getAllPaths(
                    startVertex,
                    targetVertex,
                    true,
                    null);
        }
    }
}