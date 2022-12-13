package org.odoral.adventofcode.y2022.day12;

import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.exception.AdventOfCodeException;
import org.odoral.adventofcode.common.model.ValuedPoint;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HillClimbingAlgorithm {

    public static final char STARTING_POINT = 'S';
    public static final char DESTINATION_POINT = 'E';

    public static void main(String[] args) throws IOException {
        HillClimbingAlgorithm hillClimbingAlgorithm = new HillClimbingAlgorithm();
        ValuedPoint<Node>[][] heightMap = HillClimbingAlgorithm.Input.loadHeightMap("/input.txt");
        int steps = hillClimbingAlgorithm.lookForMinimumPath(heightMap, hillClimbingAlgorithm.findSpecialPoint(heightMap, STARTING_POINT).get(0));
        log.info("Fewest steps for part1: {}", steps);

        steps = hillClimbingAlgorithm.lookForMinimumPath(heightMap, hillClimbingAlgorithm.findSpecialPoint(heightMap, 'a'));
        log.info("Fewest steps for part2: {}", steps);
    }

    protected List<ValuedPoint<Node>> findSpecialPoint(ValuedPoint<Node>[][] heightMap, char pointMark) {
        return Stream.of(heightMap)
            .flatMap(Stream::of)
            .filter(vp -> vp.value.letter == pointMark)
            .collect(Collectors.toList());
    }

    protected void clearSearchResultsMap(ValuedPoint<Node>[][] heightMap) {
        Stream.of(heightMap)
            .flatMap(Stream::of)
            .map(point -> point.value)
            .forEach(Node::clear);
    }

    protected int lookForMinimumPath(ValuedPoint<Node>[][] heightMap, List<ValuedPoint<Node>> startingPoints) {
        return startingPoints.stream()
            .mapToInt(startingPoint -> lookForMinimumPath(heightMap, startingPoint))
            .min()
            .orElseThrow(() -> new AdventOfCodeException("No path found."));

    }

    protected int lookForMinimumPath(ValuedPoint<Node>[][] heightMap, ValuedPoint<Node> startingPoint) {
        clearSearchResultsMap(heightMap);
        startingPoint.value.steps = 0;

        Queue<ValuedPoint<Node>> neighboursToAnalise = new ConcurrentLinkedDeque<>();
        neighboursToAnalise.add(startingPoint);

        while (!neighboursToAnalise.isEmpty()) {
            ValuedPoint<Node> currentPoint = neighboursToAnalise.poll();
            CommonUtils.findNeighbours(heightMap, currentPoint.x, currentPoint.y, false, p -> p.value)
                .stream()
                .filter(neighbour -> isValidStep(currentPoint, neighbour))
                .forEach(neighbour -> {
                    if (neighbour.value.steps > (currentPoint.value.steps + 1)) {
                        neighbour.value.steps = (currentPoint.value.steps + 1);
                        neighboursToAnalise.add(neighbour);
                    }
                });
        }

        return findSpecialPoint(heightMap, DESTINATION_POINT).get(0).value.steps;
    }

    protected boolean isValidStep(ValuedPoint<Node> node, ValuedPoint<Node> neighbour) {
        Character nodeValue = translate(node.value.letter);
        Character neighbourValue = translate(neighbour.value.letter);
        return neighbourValue - nodeValue <= 1;
    }

    protected char translate(char value) {
        switch (value) {
            case STARTING_POINT:
                return 'a';
            case DESTINATION_POINT:
                return 'z';
            default:
                return value;
        }
    }

    public static class Input {

        public static ValuedPoint<Node>[][] loadHeightMap(String resource) throws IOException {
            return CommonUtils.loadMatrixResource(resource, (xPos, content) -> {
                AtomicInteger yPos = new AtomicInteger();

                return Arrays.stream(CommonUtils.toCharacterArray(content))
                    .map(value -> new ValuedPoint<>(xPos, yPos.getAndIncrement(), new Node(value)))
                    .toArray(ValuedPoint[]::new);
            }, ValuedPoint.class);
        }
    }

    public static class Node {
        char letter;
        int steps = Integer.MAX_VALUE;

        public Node(char letter) {
            this.letter = letter;
        }

        public void clear() {
            steps = Integer.MAX_VALUE;
        }
    }
}