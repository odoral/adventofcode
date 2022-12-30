package org.odoral.adventofcode.y2022.day18;

import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.exception.AdventOfCodeException;
import org.odoral.adventofcode.common.model.Point3D;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BoilingBoulders {

    public static void main(String[] args) throws IOException {
        BoilingBoulders boilingBoulders = new BoilingBoulders();
        int surfaceArea = boilingBoulders.calculateSurfaceArea();
        log.info("Surface area: {}", surfaceArea);

        int externalSurfaceArea = boilingBoulders.calculateExternalSurfaceArea();
        log.info("External surface area: {}", externalSurfaceArea);
    }

    public int calculateSurfaceArea() throws IOException {
        Set<Point3D> cubes = Input.parse();
        return calculateSurfaceArea(cubes);
    }

    protected int calculateSurfaceArea(Set<Point3D> cubes) {
        return cubes.stream()
            .mapToInt(cube -> 6 - getExistingNeighbourCubes(cubes, cube).size())
            .sum();
    }

    protected List<Point3D> getExistingNeighbourCubes(Set<Point3D> cubes, Point3D cube) {
        return cube.neighbours()
            .stream()
            .filter(cubes::contains)
            .collect(Collectors.toList());
    }

    public int calculateExternalSurfaceArea() throws IOException {
        Set<Point3D> cubes = Input.parse();
        int[] boundary = calculateBoundary(cubes);
        Point3D initialAirPoint = new Point3D(boundary[0], boundary[1], boundary[2]);

        Set<Point3D> visitedCubes = new HashSet<>();
        Queue<Point3D> queuedCubes = new ArrayDeque<>();
        queuedCubes.add(initialAirPoint);
        int total = 0;
        while (!queuedCubes.isEmpty()) {
            Point3D airPoint = queuedCubes.poll();
            if (!visitedCubes.contains(airPoint)) {
                visitedCubes.add(airPoint);
                total += getExistingNeighbourCubes(cubes, airPoint).size();
                queuedCubes.addAll(getExistingNeighbourAir(cubes, airPoint)
                    .stream()
                    .filter(cube -> insideBoundary(cube, boundary))
                    .filter(cube -> !visitedCubes.contains(cube))
                    .collect(Collectors.toList()));
            }
        }

        return total;
    }

    protected int[] calculateBoundary(Set<Point3D> cubes) {
        int[] boundary = cubes.stream()
            .map(cube -> new int[]{cube.x, cube.y, cube.z, cube.x, cube.y, cube.z})
            .reduce(null, (a1, a2) -> {
                if (a1 == null) {
                    return a2;
                } else if (a2 == null) {
                    return a1;
                } else {
                    return new int[]{
                        Math.min(a1[0], a2[0]),
                        Math.min(a1[1], a2[1]),
                        Math.min(a1[2], a2[2]),
                        Math.max(a1[3], a2[3]),
                        Math.max(a1[4], a2[4]),
                        Math.max(a1[5], a2[5])
                    };
                }
            });
        if (boundary == null) {
            throw new AdventOfCodeException("Boundary was not calculated.");
        }
        // Ensure boundary includes air
        boundary[0]--;
        boundary[1]--;
        boundary[2]--;
        boundary[3]++;
        boundary[4]++;
        boundary[5]++;
        return boundary;
    }

    protected boolean insideBoundary(Point3D point, int[] boundaries) {
        return
            point.x >= boundaries[0] && point.x <= boundaries[3] &&
                point.y >= boundaries[1] && point.y <= boundaries[4] &&
                point.z >= boundaries[2] && point.z <= boundaries[5];
    }

    protected List<Point3D> getExistingNeighbourAir(Set<Point3D> cubes, Point3D air) {
        return air.neighbours()
            .stream()
            .filter(point -> !cubes.contains(point))
            .collect(Collectors.toList());
    }

    public static class Input {

        private Input() {
        }

        public static Set<Point3D> parse() throws IOException {
            return new HashSet<>(CommonUtils.loadResource("/input.txt", line -> {
                String[] coordinates = line.split(",");
                return new Point3D(
                    Integer.parseInt(coordinates[0]),
                    Integer.parseInt(coordinates[1]),
                    Integer.parseInt(coordinates[2])
                );
            }));
        }
    }
}