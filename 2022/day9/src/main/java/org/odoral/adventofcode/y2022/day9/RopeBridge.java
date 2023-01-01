package org.odoral.adventofcode.y2022.day9;

import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.model.Point;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RopeBridge {

    public static void main(String[] args) throws IOException {
        RopeBridge ropeBridge = new RopeBridge();
        List<String> instructions = CommonUtils.loadResource("/input.txt", Function.identity());

        Set<Point> positions = ropeBridge.calculateVisitedPositions(instructions, 2);
        log.info("Visited positions for 2 knots rope: {}", positions.size());

        positions = ropeBridge.calculateVisitedPositions(instructions, 10);
        log.info("Visited positions for 10 knots rope: {}", positions.size());
    }

    protected Set<Point> calculateVisitedPositions(List<String> instructions, int knotCount) {
        List<Point> rope = buildRope(knotCount);

        Set<Point> visitedPositions = new HashSet<>();
        rope.stream().findFirst().ifPresent(visitedPositions::add);

        for (String instruction : instructions) {
            String[] fields = instruction.split("\\s");
            String where = fields[0];
            int steps = Integer.parseInt(fields[1]);
            Function<Point, Point> headMoveFunction = Function.identity();
            switch (where) {
                case "U":
                    headMoveFunction = Point::increaseY;
                    break;
                case "D":
                    headMoveFunction = Point::decreaseY;
                    break;
                case "L":
                    headMoveFunction = Point::decreaseX;
                    break;
                case "R":
                    headMoveFunction = Point::increaseX;
                    break;
            }

            for (int stepIndex = 0; stepIndex < steps; stepIndex++) {
                for (int ropeIndex = 0; ropeIndex < rope.size(); ropeIndex++) {
                    Function<Point, Point> movement = headMoveFunction;
                    if (ropeIndex > 0) {
                        Point knotAhead = rope.get(ropeIndex - 1);
                        Point currentKnot = rope.get(ropeIndex);
                        movement = calculateMoveFunction(knotAhead, currentKnot);
                    }
                    rope.set(ropeIndex, movement.apply(rope.get(ropeIndex)));
                    if (ropeIndex == rope.size() - 1) {
                        visitedPositions.add(rope.get(ropeIndex));
                    }
                }
            }
        }

        log.info("rope after {}", instructions);
        printPositions(rope);
        log.info("positions after {}", instructions);
        printPositions(visitedPositions);
        return visitedPositions;
    }

    protected List<Point> buildRope(int knotNumber) {
        return IntStream.range(0, knotNumber)
            .mapToObj(index -> new Point(0, 0))
            .collect(Collectors.toList());
    }

    protected Function<Point, Point> calculateMoveFunction(Point knotAhead, Point currentKnot) {
        Function<Point, Point> movement = Function.identity();
        if (knotAhead.distance(currentKnot) < 2) {
            movement = Function.identity();
        } else {
            if (knotAhead.x != currentKnot.x) {
                if (knotAhead.x > currentKnot.x) {
                    movement = movement.andThen(Point::increaseX);
                } else {
                    movement = movement.andThen(Point::decreaseX);
                }
            }
            if (knotAhead.y != currentKnot.y) {
                if (knotAhead.y > currentKnot.y) {
                    movement = movement.andThen(Point::increaseY);
                } else {
                    movement = movement.andThen(Point::decreaseY);
                }
            }
        }
        return movement;
    }

    protected void printPositions(Collection<Point> points) {
        int minX = points.stream().mapToInt(p -> p.x).min().orElse(0);
        int maxX = points.stream().mapToInt(p -> p.x).max().orElse(0);
        int minY = points.stream().mapToInt(p -> p.y).min().orElse(0);
        int maxY = points.stream().mapToInt(p -> p.y).max().orElse(0);

        log.info(IntStream.range(minX, maxX).mapToObj(index -> "-").collect(Collectors.joining()));
        for (int y = maxY; y >= minY; y--) {
            StringBuilder sb = new StringBuilder();
            for (int x = minX; x <= maxX; x++) {
                sb.append(points.contains(new Point(x, y)) ? "Y" : ".");
            }
            log.info(sb.toString());
        }
        log.info(IntStream.range(minX, maxX).mapToObj(index -> "-").collect(Collectors.joining()));

    }

}