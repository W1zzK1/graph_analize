package me.gorbunov.algorithms;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;

import java.util.HashSet;
import java.util.Set;

public class GraphAnalize {
    private Set<String> dangerstate = new HashSet<String>();


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

    // Метод для изменения весов дуг, исходящих из заданной вершины
    public void changeWeights(Graph<String, DefaultWeightedEdge> graph, String startVertex, Set<String> visited) {
        if (visited.contains(startVertex)) {
            // Если вершина уже была посещена, прерываем выполнение, чтобы избежать зацикливания
            return;
        }
        visited.add(startVertex); // Добавляем вершину в набор посещенных

        Set<DefaultWeightedEdge> edges = graph.outgoingEdgesOf(startVertex);
        float totalWeight = 0; // Суммарный вес всех исходящих дуг

        for (DefaultWeightedEdge edge : edges) {
            totalWeight += (float) graph.getEdgeWeight(edge);
        }

        // Расчет новых весов для достижения суммы весов, равной 1
        for (DefaultWeightedEdge edge : edges) {
            float currentWeight = (float) graph.getEdgeWeight(edge);
            float adjustedWeight = currentWeight / totalWeight; // Пропорциональное распределение весов
            graph.setEdgeWeight(edge, adjustedWeight);

            // Рекурсивный вызов для следующей вершины
            String nextVertex = graph.getEdgeTarget(edge);
            changeWeights(graph, nextVertex, visited);
        }
    }

    // Вспомогательный метод для запуска процесса изменения весов
    public void adjustGraphWeights(Graph<String, DefaultWeightedEdge> graph, String startVertex) {
        changeWeights(graph, startVertex, new HashSet<>());
    }

    public void AchangeWeights(Graph<String,
            DefaultWeightedEdge> graph,
                              String startVertex,
                              String endVertex,
                              float newWeight) {
        DefaultWeightedEdge edge = graph.getEdge(startVertex, endVertex);
        Set<DefaultWeightedEdge> edges = graph.outgoingEdgesOf(startVertex);
        Set<DefaultWeightedEdge> mutableCopyEdges = new HashSet<>(edges);
        float oldWeight = (float) graph.getEdgeWeight(edge);
        float raznitsa = oldWeight - newWeight;
        DefaultWeightedEdge qq = findElementInSet(mutableCopyEdges, edge);
        deleteEdge(mutableCopyEdges, qq);
        Float total = findTotal(mutableCopyEdges, graph);
        findAndSetNewWeights(graph, mutableCopyEdges, oldWeight, raznitsa, total);

    }

    private void findAndSetNewWeights(Graph<String, DefaultWeightedEdge> graph, Set<DefaultWeightedEdge> set, float oldWeight, float raznitsa, float total) {
        for (DefaultWeightedEdge i : set) {
            float p = (float) graph.getEdgeWeight(i);
            //System.out.println(p + " ^ " + (p / total) + " ^ " + raznitsa);
            var qwe = p + (p / total) * raznitsa;
            System.out.println(qwe);
            graph.setEdgeWeight(i, qwe);
        }
    }

    private DefaultWeightedEdge findElementInSet(Set<DefaultWeightedEdge> edges, DefaultWeightedEdge edgeToFind) {
        for (DefaultWeightedEdge i : edges) {
            if (i.equals(edgeToFind)) {
                return i;
            }
        }
        return null;
    }

    private void deleteEdge(Set<DefaultWeightedEdge> edges, DefaultWeightedEdge edgeToDel) {
        edges.removeIf(i -> i.equals(edgeToDel));
    }

    private Float findTotal(Set<DefaultWeightedEdge> set, Graph<String, DefaultWeightedEdge> graph) {
        float total = 0;
        for (DefaultWeightedEdge i : set) {
            total += (float) graph.getEdgeWeight(i);
        }
        return total;
    }
}
