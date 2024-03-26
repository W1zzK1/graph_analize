package me.gorbunov.algorithms;

import org.jgrapht.Graph;
import org.jgrapht.alg.cycle.TarjanSimpleCycles;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.*;

public class Znamenatel {
    public double findZnamenatel(Graph<String, DefaultWeightedEdge> graphCopy, List<String> dangerousVertices) {
        Set<String> vertexSet = graphCopy.vertexSet();

        // преобразование набора вершин в список
        List<String> verticesInPath2 = new ArrayList<>(vertexSet);
        verticesInPath2.removeAll(dangerousVertices);

        // Находим все циклы в графе
        TarjanSimpleCycles<String, DefaultWeightedEdge> cycleFinder = new TarjanSimpleCycles<>(graphCopy);
        List<List<String>> allCycles = cycleFinder.findSimpleCycles();

        Map<List<String>, Double> cycleWeights = new HashMap<>();
        double ans1 = 0;
        for (var cycle : allCycles) {
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

            cycleWeights.put(cycle, weight);
            ans1 += weight;
        }
        var test = new Znamenatel.NonTouchingCyclesFinder(cycleWeights);

        double a1 = 0;
        for (var k : test.findNonTouchingCycleGroups(2)) {
            a1 += k;
        }

        double a2 = 0;
        for (var k : test.findNonTouchingCycleGroups(3)) {
            a2 += k;
        }

        return 1 - ans1 + a1 - a2;
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
}
