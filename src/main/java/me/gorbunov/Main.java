package me.gorbunov;

import me.gorbunov.algorithms.Chislitel;
import me.gorbunov.algorithms.GraphAnalize;
import me.gorbunov.algorithms.Znamenatel;
import me.gorbunov.dto.GraphVertex;
import org.jgrapht.Graph;
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
            graph.addVertex(Integer.toString(gr.getOut()));
            graph.addVertex(Integer.toString(gr.getTo()));
        }

        // Теперь добавляем ребра
        for (GraphVertex graphVertex : vertexList) {
            String source = Integer.toString(graphVertex.getOut());
            String target = Integer.toString(graphVertex.getTo());
            float weight = graphVertex.getWeigth();

            DefaultWeightedEdge edge = graph.getEdge(source, target);
            if (edge == null) {
                // Ребра не существует, добавляем новое
                edge = graph.addEdge(source, target);
            }
            // Устанавливаем вес (для нового или существующего ребра)
            graph.setEdgeWeight(edge, weight);
        }

        GraphAnalize graphAnalize = new GraphAnalize();

        var graphCopy = graphAnalize.cloneGraph(graph);

        String startVertex = "1";
        graph.removeAllEdges(startVertex, startVertex);
        graph.removeAllEdges("2", "6"); // в данном случае удалено, потому что "слишком частая вероятность"

        List<String> dangerousVertices = List.of("6");

        Set<DefaultWeightedEdge> a = graph.edgesOf("1");
        Map<DefaultWeightedEdge, Double> edgesMap = new HashMap<>();

        for (DefaultWeightedEdge edge : a) {
            edgesMap.put(edge, graph.getEdgeWeight(edge));
        }

        //graphAnalize.changeWeights(graphCopy, "1", "1", 0.65F);



        Chislitel foo = new Chislitel();
        double chislitel = foo.FindChislitel(graph, startVertex, "6", dangerousVertices);

        Znamenatel baz = new Znamenatel();
        double znamenatel = baz.findZnamenatel(graphCopy, dangerousVertices);


        System.out.println("числитель = " + chislitel + ", Знаменатель = " + znamenatel);
        String result = String.format("%.2f", chislitel / znamenatel);
        System.out.print(result);
    }
}