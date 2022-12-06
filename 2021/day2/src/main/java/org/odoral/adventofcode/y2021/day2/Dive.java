package org.odoral.adventofcode.y2021.day2;

import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.exception.AdventOfCodeException;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Dive {

    public static void main(String[] args) throws IOException {
        Dive dive = new Dive();
        List<String> plannedRoute = CommonUtils.loadResource("/input.txt", Function.identity());
        Result result = dive.processPlannedRoute(plannedRoute);
        log.info("Destination is: ({},{})={}", result.destination.x, result.destination.y, result.getDestionationResult());

        Result aimResult = dive.processPlannedRouteWithAimApproach(plannedRoute);
        log.info("Destination is: ({},{})={}", aimResult.destination.x, aimResult.destination.y, aimResult.getDestionationResult());
    }

    public Result processPlannedRoute(List<String> plannedRoute) {
        Point currentPosition = new Point(0, 0);

        plannedRoute.forEach(order -> {
            String[] fields = order.split(" ");
            int offset = Integer.parseInt(fields[1]);
            switch (fields[0]) {
                case "forward":
                    currentPosition.move(offset, 0);
                    break;
                case "down":
                    currentPosition.move(0, offset);
                    break;
                case "up":
                    currentPosition.move(0, -offset);
                    break;
                default:
                    throw new AdventOfCodeException("Unsupported move: " + fields[0]);
            }
        });

        return new Result(currentPosition);
    }

    public Result processPlannedRouteWithAimApproach(List<String> plannedRoute) {
        Point currentPosition = new Point(0, 0);

        plannedRoute.forEach(order -> {
            String[] fields = order.split(" ");
            int offset = Integer.parseInt(fields[1]);
            switch (fields[0]) {
                case "forward":
                    currentPosition.forward(offset);
                    break;
                case "down":
                    currentPosition.aim(offset);
                    break;
                case "up":
                    currentPosition.aim(-offset);
                    break;
                default:
                    throw new AdventOfCodeException("Unsupported move: " + fields[0]);
            }
        });

        return new Result(currentPosition);
    }

    public static class Result {
        final Point destination;

        public Result(Point destination) {
            this.destination = destination;
        }

        public int getDestionationResult() {
            return destination.x * destination.y;
        }

    }

    public static class Point {
        int x;
        int y;
        int aim;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void move(int x, int y) {
            this.x += x;
            this.y += y;
        }

        public void aim(int aim) {
            this.aim += aim;
        }

        public void forward(int x) {
            this.x += x;
            this.y += (aim * x);
        }
    }
}