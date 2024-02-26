package me.gorbunov;

import me.gorbunov.dto.GraphVertex;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.cycle.JohnsonSimpleCycles;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;

import java.sql.SQLException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        Graph<String, DefaultWeightedEdge> graph = new DirectedWeightedPseudograph<>(DefaultWeightedEdge.class);


        DAO dao = new DAO();
        List<GraphVertex> vertexList = dao.getAllVertex();

        // Добавляем все вершины в граф
        for (GraphVertex gr : vertexList) {
            System.out.println(gr.toString());
            graph.addVertex(Integer.toString(gr.getOut()));
            graph.addVertex(Integer.toString(gr.getTo()));
        }

        // Теперь добавляем ребра
        for (GraphVertex graphVertex : vertexList) {
            String source = Integer.toString(graphVertex.getOut());
            String target = Integer.toString(graphVertex.getTo());
            double weight = graphVertex.getWeigth();

            DefaultWeightedEdge edge = graph.getEdge(source, target);
            if (edge == null) {
                // Ребра не существует, добавляем новое
                edge = graph.addEdge(source, target);
            }
            // Устанавливаем вес (для нового или существующего ребра)
            graph.setEdgeWeight(edge, weight);
        }
//
//        System.out.println(graph);


//        graph.addVertex("1");
//        graph.addVertex("2");
//        graph.addVertex("3");
//        graph.addVertex("4");
//        graph.addVertex("5");
//        graph.addVertex("6");
//
//        DefaultWeightedEdge e0 = graph.addEdge("1", "1");
//        graph.setEdgeWeight(e0, 0.72);
//
//        DefaultWeightedEdge e1 = graph.addEdge("1", "2");
//        graph.setEdgeWeight(e1, 0.18);
//
//        DefaultWeightedEdge e4 = graph.addEdge("1", "4");
//        graph.setEdgeWeight(e4, 0.1);
//
//        DefaultWeightedEdge e2 = graph.addEdge("2", "1");
//        graph.setEdgeWeight(e2, 0.85);
//
//        DefaultWeightedEdge e3 = graph.addEdge("2", "3");
//        graph.setEdgeWeight(e3, 0.075);
//
//        DefaultWeightedEdge e5 = graph.addEdge("2", "6");
//        graph.setEdgeWeight(e5, 0.075);
//
//        DefaultWeightedEdge e6 = graph.addEdge("3", "1");
//        graph.setEdgeWeight(e6, 1);
//
//        DefaultWeightedEdge e7 = graph.addEdge("4", "1");
//        graph.setEdgeWeight(e7, 0.7);
//
//        DefaultWeightedEdge e8 = graph.addEdge("4", "5");
//        graph.setEdgeWeight(e8, 0.15);
//
//        DefaultWeightedEdge e9 = graph.addEdge("4", "6");
//        graph.setEdgeWeight(e9, 0.15);
//
//        DefaultWeightedEdge e10 = graph.addEdge("5", "1");
//        graph.setEdgeWeight(e10, 1);
//
//        DefaultWeightedEdge e11 = graph.addEdge("6", "6");
//        graph.setEdgeWeight(e11, 1);
//
//        System.out.println(graph);

//        graph.addVertex("0");
//        graph.addVertex("1");
//        graph.addVertex("2");
//        graph.addVertex("3");
//        graph.addVertex("4");
//        graph.addVertex("5");
//
//
//        DefaultWeightedEdge e0 = graph.addEdge("0", "0");
//        graph.setEdgeWeight(e0, 0.7);
//
//        DefaultWeightedEdge e1 = graph.addEdge("0", "1");
//        graph.setEdgeWeight(e1, 0.3);
//
//        DefaultWeightedEdge e2 = graph.addEdge("1", "0");
//        graph.setEdgeWeight(e2, 0.5);
//
//        DefaultWeightedEdge e3 = graph.addEdge("1", "2");
//        graph.setEdgeWeight(e3, 0.3);
//
//        DefaultWeightedEdge e4 = graph.addEdge("1", "3");
//        graph.setEdgeWeight(e4, 0.2);
//
//        DefaultWeightedEdge e5 = graph.addEdge("2", "2");
//        graph.setEdgeWeight(e5, 0.7);
//
//        DefaultWeightedEdge e6 = graph.addEdge("2", "4");
//        graph.setEdgeWeight(e6, 0.3);
//
//        DefaultWeightedEdge e7 = graph.addEdge("3", "3");
//        graph.setEdgeWeight(e7, 0.3);
//
//        DefaultWeightedEdge e8 = graph.addEdge("3", "4");
//        graph.setEdgeWeight(e8, 0.2);
//
//        DefaultWeightedEdge e9 = graph.addEdge("3", "5");
//        graph.setEdgeWeight(e9, 0.5);
//
//        DefaultWeightedEdge e10 = graph.addEdge("4", "2");
//        graph.setEdgeWeight(e10, 0.3);
//
//        DefaultWeightedEdge e11 = graph.addEdge("4", "4");
//        graph.setEdgeWeight(e11, 0.7);
//
//        DefaultWeightedEdge e12 = graph.addEdge("5", "5");
//        graph.setEdgeWeight(e12, 1);

        GraphAnalize graphAnalize = new GraphAnalize();

        var graphCopy = graphAnalize.cloneGraph(graph);

        String startVertex = "1";
        graph.removeAllEdges(startVertex, startVertex);
        graph.removeAllEdges("2", "6");

        var paths = graphAnalize.getListAllPaths(graph, startVertex, "6");
        System.out.println(paths);

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

        for (Map.Entry<GraphPath<String, DefaultWeightedEdge>, Double> entry : pathMap.entrySet()) {
            System.out.println(entry);
        }

        // ребра и вершины на пути
        List<String> verticesInPath = paths.stream()
                .flatMap(p -> p.getVertexList().stream())
                .toList();
        List<DefaultWeightedEdge> edgesInPath = paths.stream()
                .flatMap(p -> p.getEdgeList().stream())
                .toList();
//
//        System.out.println(verticesInPath);
//        System.out.println(edgesInPath);
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
        List<String> dangerousVertices = Arrays.asList("6");
//        List<String> dangerousVertices = Arrays.asList("4", "5");
        Map<DefaultWeightedEdge, Double> weigthList = new HashMap<>();

        double chislitel = 0;

        for (GraphPath<String, DefaultWeightedEdge> path : paths) {
            System.out.println(path);
            // Получить все вершины на данном пути
            List<String> verticesInPath1 = path.getVertexList();

            // Получить все петли, которые находятся на данном пути или касаются вершин на данном пути
            List<DefaultWeightedEdge> loopsInPath = allLoops.stream()
                    .filter(loop -> verticesInPath1.contains(graph.getEdgeSource(loop)))
                    .toList();
            System.out.println("все петли на данном пути = " + loopsInPath);
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
                    System.out.println("зашел в цикл");
                    double weight = graph.getEdgeWeight(loop);
                    weigthList.put(loop, 1 - weight);
                    System.out.println("Weight of loop " + loop + " = " + weight);
                    chislitel += edgeWeight * (1 - weight);
                }
            }
        }

        /// Поиск знаменателя
        Set<String> vertexSet = graphCopy.vertexSet();

        // преобразование набора вершин в список
        List<String> verticesInPath2 = new ArrayList<>(vertexSet);
        verticesInPath2.removeAll(dangerousVertices);

        // Находим все циклы в графе  с помощью алгоритма Джонсона
        JohnsonSimpleCycles<String, DefaultWeightedEdge> cycleFinder = new JohnsonSimpleCycles<>(graphCopy);
        List<List<String>> allCycles = cycleFinder.findSimpleCycles();
        System.out.println(allCycles);

//        List<List<String>> loops = new ArrayList<>();
//        for (DefaultWeightedEdge edge : graphCopy.edgeSet()) {
//            if (graphCopy.getEdgeSource(edge).equals(graphCopy.getEdgeTarget(edge))) {
//                String vertex = graphCopy.getEdgeSource(edge);
//                loops.add(Arrays.asList(vertex, vertex));
//            }
//        }
//        List<DefaultWeightedEdge> loops = new ArrayList<>();
//        for (DefaultWeightedEdge edge : graphCopy.edgeSet()) {
//            if (graphCopy.getEdgeSource(edge).equals(graphCopy.getEdgeTarget(edge))) {
//                loops.add(edge);
//            }
//        }

        Map<List<String>, Double> cycleWeights = new HashMap<>();
        double ans1 = 0;
        for (List<String> cycle : allCycles) {
            double weight = 1;
            if (cycle.retainAll(verticesInPath2)) {
                continue; // проверка на наличие на пути и опасное состояние.
            }
            for (int i = 0; i < cycle.size() - 1; i++) {
                DefaultWeightedEdge edge = graphCopy.getEdge(cycle.get(i), cycle.get(i + 1));
                weight *= graphCopy.getEdgeWeight(edge);
            }
            // Добавьте вес последнего ребра в цикле
            DefaultWeightedEdge lastEdge = graphCopy.getEdge(cycle.get(cycle.size() - 1), cycle.get(0));
            weight *= graphCopy.getEdgeWeight(lastEdge);

            System.out.println("Вес цикла: " + weight + " сам цикл: " + cycle);
            cycleWeights.put(cycle, weight);
            ans1 += weight;
        }
        var test = new NonTouchingCyclesFinder(cycleWeights);

        double a1 = 0;
        for (var k : test.findNonTouchingCycleGroups(2)) {
            a1 += k;
        }

        double a2 = 0;
        for (var k : test.findNonTouchingCycleGroups(3)) {
            a2 += k;
        }

        // final
        double znamenatel = 1 - ans1 + a1 - a2;
        System.out.println("числитель = " + chislitel + ", Знаменатель = " + znamenatel);
        String result = String.format("%.2f", chislitel / znamenatel);
        System.out.print(result);
    }

    public static class NonTouchingCyclesFinder {
        private Map<List<String>, Double> cycleWeights;
        private List<List<String>> cycles;
        private List<Set<String>> cycleVertexSets;

        public NonTouchingCyclesFinder(Map<List<String>, Double> cycleWeights) {
            this.cycleWeights = cycleWeights;
            this.cycles = new ArrayList<>(cycleWeights.keySet());
            this.cycleVertexSets = new ArrayList<>();
            for (List<String> cycle : cycles) {
                this.cycleVertexSets.add(new HashSet<>(cycle));
            }
        }

        public List<Double> findNonTouchingCycleGroups(int groupSize) {
            List<Double> products = new ArrayList<>();
            findNonTouchingCycleGroups(new ArrayList<>(), new HashSet<>(), 0, groupSize, products);
            return products;
        }

        private void findNonTouchingCycleGroups(List<Integer> currentGroup, Set<String> currentVertices, int start, int groupSize, List<Double> products) {
            if (currentGroup.size() == groupSize) {
                double product = 1;
                for (int index : currentGroup) {
                    product *= cycleWeights.get(cycles.get(index));
                }
                products.add(product);
            } else {
                for (int i = start; i < cycles.size(); i++) {
                    if (Collections.disjoint(currentVertices, cycleVertexSets.get(i))) {
                        currentGroup.add(i);
                        currentVertices.addAll(cycleVertexSets.get(i));
                        findNonTouchingCycleGroups(currentGroup, currentVertices, i + 1, groupSize, products);
                        currentGroup.remove(currentGroup.size() - 1);
                        currentVertices.removeAll(cycleVertexSets.get(i));
                    }
                }
            }
        }
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

        public List<GraphPath<String, DefaultWeightedEdge>> getListAllPaths(Graph<String, DefaultWeightedEdge> graph, String startVertex, String targetVertex) {
            AllDirectedPaths<String, DefaultWeightedEdge> allPaths = new AllDirectedPaths<>(graph);
            return allPaths.getAllPaths(
                    startVertex,
                    targetVertex,
                    true,
                    null);
        }
    }
}