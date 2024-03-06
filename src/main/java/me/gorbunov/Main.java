package me.gorbunov;

import me.gorbunov.algorithms.Chislitel;
import me.gorbunov.algorithms.Znamenatel;
import me.gorbunov.dto.GraphVertex;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
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

        System.out.println(graph);


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
        graph.removeAllEdges("2", "6"); // в данном случае удалено, потому что "слишком частая вероятность"

        List<String> dangerousVertices = Arrays.asList("6");

        Chislitel foo = new Chislitel();
        double chislitel = foo.FindChislitel(graph, startVertex, "6");

        Znamenatel baz = new Znamenatel();
        double znamenatel = baz.findZnamenatel(graphCopy, dangerousVertices);


        System.out.println("числитель = " + chislitel + ", Знаменатель = " + znamenatel);
        String result = String.format("%.2f", chislitel / znamenatel);
        System.out.print(result);
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
    }
}