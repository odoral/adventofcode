package org.odoral.adventofcode.y2021.day5;

import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.model.Point;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HydrothermalVenture {

    public static final Predicate<Line> FILTER_HORIZONTAL_AND_VERTICAL_LINES = line -> line.start.x == line.end.x || line.start.y == line.end.y;
    public static final Predicate<Line> NO_FILTER = line -> true;

    public static void main(String[] args) throws IOException {
        HydrothermalVenture hydrothermalVenture = new HydrothermalVenture();
        List<HydrothermalVenture.Line> input = CommonUtils.loadResource("/input.txt", hydrothermalVenture::mapLine);
        HydrothermalVenture.Result result = hydrothermalVenture.drawLines(input, FILTER_HORIZONTAL_AND_VERTICAL_LINES);
        log.info("Result for horizontal and vertical lines: {}", result.countOverlaps(2));

        result = hydrothermalVenture.drawLines(input, NO_FILTER);
        log.info("Result for all lines: {}", result.countOverlaps(2));
    }

    public Line mapLine(String config) {
        String[] fields = config.split(" -> ");
        String[] startFields = fields[0].split(",");
        String[] endFields = fields[1].split(",");

        return new Line(new Point(Integer.parseInt(startFields[0]), Integer.parseInt(startFields[1])),
            new Point(Integer.parseInt(endFields[0]), Integer.parseInt(endFields[1])));
    }

    public Result drawLines(List<Line> lines, Predicate<Line> lineFilter) {
        Integer[][] map = createMap(lines);

        lines.stream()
            .filter(lineFilter)
            .map(Line::pointIterator)
            .forEach(it -> {
                while (it.hasNext()) {
                    Point point = it.next();
                    if (Objects.isNull(map[point.x][point.y])) {
                        map[point.x][point.y] = 1;
                    } else {
                        map[point.x][point.y] += 1;
                    }
                }
            });

        return new Result(map);
    }

    protected Integer[][] createMap(List<Line> lines) {
        int mapSize = lines.stream()
            .flatMap(line -> Stream.of(line.start, line.end))
            .mapToInt(p -> Math.max(p.x, p.y))
            .max()
            .orElseThrow(() -> new IllegalArgumentException("No points to build the map")) + 1;
        log.info("Map size: {},{}", mapSize, mapSize);
        return new Integer[mapSize][mapSize];
    }

    public static class Result {
        final Integer[][] map;

        public Result(Integer[][] map) {
            this.map = map;
        }

        public long countOverlaps(int minOverlaps) {
            return Stream.of(map)
                .flatMap(Stream::of)
                .filter(Objects::nonNull)
                .filter(i -> i >= minOverlaps)
                .count();
        }
    }

    public class Line {
        final Point start;
        final Point end;

        public Line(Point start, Point end) {
            this.start = start;
            this.end = end;
        }

        public Iterator<Point> pointIterator() {
            return new Iterator<Point>() {

                final int incrementX = Integer.compare(end.x, start.x);
                final int incrementY = Integer.compare(end.y, start.y);
                Point currentPoint = null;

                @Override
                public boolean hasNext() {
                    return !end.equals(currentPoint);
                }

                @Override
                public Point next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    if (currentPoint == null) {
                        currentPoint = start;
                    } else {
                        currentPoint = new Point(currentPoint.x + incrementX, currentPoint.y + incrementY);
                    }
                    return currentPoint;
                }
            };
        }
    }
}