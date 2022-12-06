package org.odoral.adventofcode.y2020.day10;

import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AdapterArray {
    
    public static void main(String [] args) throws IOException {
        AdapterArray adapterArray = new AdapterArray();
        List<Integer> adapters = adapterArray.loadAdaptersData("/input.txt");

        Map<Integer, Integer> joltDiffCounter = adapterArray.countJoltDiffs(adapters, 0, 3);

        long solution = adapterArray.calculateFirstSolution(joltDiffCounter);

        log.info("Solution is: {}", solution);
        
        log.info("Different ways to arrange adapters is: {}", adapterArray.calculateArrangements(adapters, 0, 3));
    }

    public List<Integer> loadAdaptersData(String resource) throws IOException {
        return CommonUtils.loadResource(resource, Integer::parseInt);
    }

    public Map<Integer, Integer> countJoltDiffs(List<Integer> adapters, int initialJoltage, int finalJoltageHigher) {
        Map<Integer, Integer> result = new HashMap<>();
        AtomicInteger previousJoltage = new AtomicInteger(initialJoltage);
        
        adapters.add(adapters.stream()
            .max(Integer::compare)
            .orElseThrow(() -> new IllegalStateException("There is no adapter provided")) + finalJoltageHigher);
        
        adapters.stream()
            .sorted()
            .forEach(adapterJoltage -> {
                result.compute(adapterJoltage - previousJoltage.get(), (k, v) -> Objects.isNull(v) ? 1 : ++v);
                previousJoltage.set(adapterJoltage);
            });
        return result;
    }

    public long calculateFirstSolution(Map<Integer, Integer> joltDiffCounter) {
        return Long.valueOf(joltDiffCounter.get(1)) * Long.valueOf(joltDiffCounter.get(3));
    }

    public long calculateArrangements(List<Integer> adapters, int initialJoltage, int finalJoltageHigher) {
        Set<Node<Integer>> result = new HashSet<>();
        Node<Integer> initialNode = new Node<>(initialJoltage);
        result.add(initialNode);

        List<Integer> sortedAdapters = adapters.stream()
            .sorted()
            .collect(Collectors.toList());

        for (int adapterJolt : sortedAdapters) {
            Node<Integer> leaf = new Node<>(adapterJolt);
            result.addAll(result.stream()
                .filter(node -> adapterJolt >= node.value && adapterJolt <= (node.value + finalJoltageHigher))
                .map(node -> {
                    node.leafs.add(leaf);
                    return leaf;
                }).distinct()
                .collect(Collectors.toList()));
        }

        List<Node<Integer>> filteredAlternatives = result.stream()
            .filter(node -> node.leafs.size() > 1)
            .collect(Collectors.toList());
        
        log.info("There are {} adapters with alternatives.", filteredAlternatives.size());
        
        List<Node<Integer>> groupedAlternatives = new ArrayList<>();
        long total = 1;
        for (Node<Integer> node : filteredAlternatives) {
            if (!groupedAlternatives.isEmpty() &&
                (groupedAlternatives.get(groupedAlternatives.size() - 1).value + finalJoltageHigher) < node.value) {
                total *= calculatePathsForGroupedAlternatives(groupedAlternatives);
                groupedAlternatives.clear();
            }
            groupedAlternatives.add(node);
        }
        total*= calculatePathsForGroupedAlternatives(groupedAlternatives);
        
        return total;
    }

    protected long calculatePathsForGroupedAlternatives(List<Node<Integer>> groupedDesviations) {
        if(groupedDesviations.isEmpty()){
            return 1;
        }else {
            Map<Integer, Node<Integer>> nodeMap = groupedDesviations.stream()
                .collect(Collectors.toMap(node -> node.value, Function.identity()));
            return countAddedPaths(groupedDesviations.get(0), nodeMap);
        }
    }

    protected long countAddedPaths(Node<Integer> node, Map<Integer, Node<Integer>> nodeMap) {
        if(nodeMap.get(node.value) == null){
            return 1;
        }else{
            return node.leafs.stream()
                .parallel()
                .mapToLong(n -> countAddedPaths(n, nodeMap))
                .sum();
        }
    }

    public static class Node<T extends Comparable<T>> implements Comparable<Node<T>>{
        final T value;
        final List<Node<T>> leafs = new ArrayList<>();

        public Node(T value) {
            this.value = value;
        }

        @Override
        public int compareTo(Node<T> other) {
            return value.compareTo(other.value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Node<?> node = (Node<?>) o;

            return value.equals(node.value);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Node.class.getSimpleName() + "[", "]")
                .add("value=" + value)
                .add("leafs=" + leafs.size())
                .toString();
        }
    }
}
