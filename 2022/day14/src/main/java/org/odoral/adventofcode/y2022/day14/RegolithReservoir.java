package org.odoral.adventofcode.y2022.day14;

import com.google.common.collect.ImmutableList;

import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.exception.AdventOfCodeException;
import org.odoral.adventofcode.common.model.Point;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RegolithReservoir {

    public static final char ROCK = '#';
    public static final List<UnaryOperator<Point>> DISPLACEMENTS = ImmutableList.of(
        Point::increaseY,
        p -> p.increaseY().decreaseX(),
        p -> p.increaseY().increaseX()
    );
    public static final Point SAND_START_POINT = new Point(500, 0);

    public static void main(String[] args) throws IOException {
        RegolithReservoir regolithReservoir = new RegolithReservoir();

        Map<Point, Character> world = RegolithReservoir.Input.loadWorld();
        int count = regolithReservoir.countHowManyUnitsOfSandComeToRest(world, regolithReservoir.getValidDisplacementPart1(world), RegolithReservoir.getAbyssLevel(world));
        log.info("{} units of sand come to rest.", count);

        world = RegolithReservoir.Input.loadWorld();
        count = regolithReservoir.countHowManyUnitsOfSandComeToRest(world, regolithReservoir.getValidDisplacementPart2(world), RegolithReservoir.getFloorLevel(world));
        log.info("{} units of sand come to rest.", count);
    }

    protected BiPredicate<UnaryOperator<Point>, Point> getValidDisplacementPart1(Map<Point, Character> world) {
        return (d, p) -> !world.containsKey(d.apply(p));
    }

    protected BiPredicate<UnaryOperator<Point>, Point> getValidDisplacementPart2(Map<Point, Character> world) {
        int floor = getFloorLevel(world);
        return (d, p) -> {
            Point newPoint = d.apply(p);
            return newPoint.y < floor && !world.containsKey(newPoint);
        };
    }

    public static int getFloorLevel(Map<Point, Character> world) {
        return getAbyssLevel(world) + 2;
    }

    public static int getAbyssLevel(Map<Point, Character> world) {
        return world.keySet()
            .stream()
            .mapToInt(point -> point.y)
            .max()
            .orElse(0);
    }

    public int countHowManyUnitsOfSandComeToRest(Map<Point, Character> world, BiPredicate<UnaryOperator<Point>, Point> isValidDisplacement, int endLevel) {
        int counter = 0;
        Optional<Point> restPoint;
        Predicate<Point> isEndReached = p -> p.y > endLevel || world.containsKey(p);
        while ((restPoint = calculateRestPoint(SAND_START_POINT, isValidDisplacement, isEndReached)).isPresent()) {
            world.put(restPoint.get(), 'O');
            counter++;
        }
        Input.printWorld(world);
        return counter;
    }

    protected Optional<Point> calculateRestPoint(Point sandStartPoint, BiPredicate<UnaryOperator<Point>, Point> isValidDisplacement, Predicate<Point> isEndReached) {
        Optional<UnaryOperator<Point>> nextDisplacement;
        Point currentPoint = new Point(sandStartPoint.x, sandStartPoint.y);
        while (!isEndReached.test(currentPoint) &&
            (nextDisplacement = calculateNextDisplacement(isValidDisplacement, currentPoint)).isPresent()) {
            currentPoint = nextDisplacement.get().apply(currentPoint);
        }

        if (isEndReached.test(currentPoint)) {
            log.debug("Flowing into the abyss -> {}", currentPoint);
            return Optional.empty();
        } else {
            log.debug("Rest point -> {}", currentPoint);
            return Optional.of(currentPoint);
        }
    }

    protected Optional<UnaryOperator<Point>> calculateNextDisplacement(BiPredicate<UnaryOperator<Point>, Point> isValidDisplacement, Point currentPoint) {
        return DISPLACEMENTS.stream()
            .filter(d -> isValidDisplacement.test(d, currentPoint))
            .findFirst();
    }

    public static class Input {

        public static final String VERTEX_SEPARATOR = " -> ";

        private Input() {
        }

        public static Map<Point, Character> loadWorld() throws IOException {
            Map<Point, Character> world = new HashMap<>();
            CommonUtils.loadResource("/input.txt", Function.identity())
                .forEach(line -> {
                    String[] vertices = line.split(VERTEX_SEPARATOR);
                    for (int vertexIndex = 1; vertexIndex < vertices.length; vertexIndex++) {
                        Point segmentInit = toPoint(vertices[vertexIndex - 1]);
                        Point segmentEnd = toPoint(vertices[vertexIndex]);

                        UnaryOperator<Point> displacement = calculateDisplacement(segmentInit, segmentEnd);

                        do {
                            world.put(segmentInit, ROCK);
                            segmentInit = displacement.apply(segmentInit);
                        } while (!segmentInit.equals(segmentEnd));

                        world.put(segmentEnd, ROCK);
                    }
                });
            return world;
        }

        public static Point toPoint(String vertexCoordinates) {
            String[] coordinates = vertexCoordinates.split(",");
            return new Point(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
        }

        public static UnaryOperator<Point> calculateDisplacement(Point init, Point end) {
            if (init.x == end.x) {
                if (init.y > end.y) {
                    return Point::decreaseY;
                } else {
                    return Point::increaseY;
                }
            } else if (init.y == end.y) {
                if (init.x > end.x) {
                    return Point::decreaseX;
                } else {
                    return Point::increaseX;
                }
            } else {
                throw new AdventOfCodeException("Invalid init:" + init + " and end:" + end);
            }
        }

        public static void printWorld(Map<Point, Character> world) {
            int worldMinX = world.keySet()
                .stream()
                .mapToInt(point -> point.x)
                .min()
                .orElse(0);
            int worldMaxX = world.keySet()
                .stream()
                .mapToInt(point -> point.x)
                .max()
                .orElse(0);
            int worldMinY = world.keySet()
                .stream()
                .mapToInt(point -> point.y)
                .min()
                .orElse(0);
            int worldMaxY = getAbyssLevel(world);

            for (int yIndex = worldMinY; yIndex <= worldMaxY; yIndex++) {
                StringBuilder sb = new StringBuilder();
                for (int xIndex = worldMinX; xIndex <= worldMaxX; xIndex++) {
                    sb.append(world.getOrDefault(new Point(xIndex, yIndex), '.'));
                }
                log.debug(sb.toString());
            }
        }
    }

}