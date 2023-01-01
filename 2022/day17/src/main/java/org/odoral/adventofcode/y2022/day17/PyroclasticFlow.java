package org.odoral.adventofcode.y2022.day17;

import org.apache.commons.lang3.StringUtils;
import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.exception.AdventOfCodeException;
import org.odoral.adventofcode.common.model.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PyroclasticFlow {

    public static final int VERTICAL_CHAMBER_WIDTH = 7;
    public static final int ROCK_LEFT_EDGE = 2;
    public static final int ROCK_BOTTOM_EDGE = 3;
    public static final char ROCK = '#';

    public static void main(String[] args) throws IOException {
        PyroclasticFlow pyroclasticFlow = new PyroclasticFlow();
        List<PyroclasticFlow.Rock> rocks = PyroclasticFlow.Rock.parse();
        List<Character> jetPattern = PyroclasticFlow.Input.parse();
        long towerHeight = pyroclasticFlow.getTowerHeight(rocks, jetPattern, 2022);
        log.info("Tower height part 1: {}", towerHeight);

        towerHeight = pyroclasticFlow.getTowerHeight(rocks, jetPattern, 1000000000000L);
        log.info("Tower height part 2: {}", towerHeight);
    }

    public long getTowerHeight(List<Rock> rocks, List<Character> jetPattern, long totalRockCount) {
        LinkedList<String> verticalChamber = new LinkedList<>();
        List<Integer> rockCycleHeights = new ArrayList<>();
        int rockCount = 0;
        AtomicInteger jetCount = new AtomicInteger();
        Point rockFallPosition;
        while (rockCount < totalRockCount) {
            Rock rock = rocks.get(rockCount % rocks.size());

            rockFallPosition = doRockFall(rock, jetPattern, jetCount, verticalChamber);
            addRock(verticalChamber, rockFallPosition, rock);
            rockCount++;

            rockCycleHeights.add(rockCycleHeights.isEmpty() ?
                getVerticalChamberHeight(verticalChamber) :
                getVerticalChamberHeight(verticalChamber) - rockCycleHeights.stream().mapToInt(i -> i).sum());

            if (rockCount % rocks.size() == 0) {
                Optional<Integer[]> pattern = lookForPattern(rockCycleHeights);
                if (pattern.isPresent()) {
                    int preamble = pattern.get()[0];
                    int rockCountPattern = pattern.get()[1];
                    int preambleHeight = pattern.get()[2];
                    int patternHeight = pattern.get()[3];
                    log.info("Pattern found: preamble: {}, rock_count_pattern: {}", preamble, rockCountPattern);

                    long patternCycles = (totalRockCount - preamble) / rockCountPattern;
                    long remainingRocks = (totalRockCount - preamble) % rockCountPattern;

                    return preambleHeight +
                        (patternHeight * patternCycles) +
                        rockCycleHeights.subList(rockCycleHeights.size() - rockCountPattern, rockCycleHeights.size())
                            .stream()
                            .limit(remainingRocks)
                            .mapToInt(i -> i)
                            .sum();
                }
            }
        }
        log.info("Chamber after {} rocks", rockCount);
        printChamber(verticalChamber);
        return getVerticalChamberHeight(verticalChamber);
    }

    protected Point doRockFall(Rock rock, List<Character> jetPattern, AtomicInteger jetCount, LinkedList<String> verticalChamber) {
        Point rockPosition;
        rockPosition = new Point(ROCK_LEFT_EDGE, getVerticalChamberHeight(verticalChamber) + ROCK_BOTTOM_EDGE + rock.shape.length - 1);

        boolean overlap = false;
        while (!overlap) {
            UnaryOperator<Point> movement = getNextJetMovement(jetPattern, jetCount.getAndIncrement());
            Point jetMovement = movement.apply(rockPosition);
            if (!overlaps(verticalChamber, rock, jetMovement)) {
                rockPosition = jetMovement;
            }
            Point fallMovement = rockPosition.decreaseY();
            overlap = overlaps(verticalChamber, rock, fallMovement);
            if (!overlap) {
                rockPosition = fallMovement;
            }
        }
        return rockPosition;
    }

    protected Optional<Integer[]> lookForPattern(List<Integer> rockCycleHeights) {
        Optional<Integer[]> result = Optional.empty();
        int minGroups = 5;
        for (int rockCount = 1; rockCount * minGroups < rockCycleHeights.size(); rockCount++) {
            List<Integer> candidatePattern = rockCycleHeights.subList(rockCycleHeights.size() - rockCount, rockCycleHeights.size());
            int index = 1;
            while (index < minGroups &&
                rockCount * minGroups < rockCycleHeights.size() &&
                samePattern(candidatePattern, rockCycleHeights.subList(rockCycleHeights.size() - (rockCount * (index + 1)), rockCycleHeights.size() - (rockCount * index)))) {
                index++;
            }
            if (index >= minGroups) {
                int preamble = rockCycleHeights.size() - (rockCount * index);
                int preambleHeight = rockCycleHeights.stream().limit(preamble).mapToInt(i -> i).sum();
                int patternHeight = candidatePattern.stream().mapToInt(i -> i).sum();
                return Optional.of(new Integer[]{
                    preamble,
                    rockCount,
                    preambleHeight,
                    patternHeight
                });
            }
        }
        return result;
    }

    protected boolean samePattern(List<Integer> candidatePattern, List<Integer> groupToTest) {
        for (int index = 0; index < candidatePattern.size(); index++) {
            if (!candidatePattern.get(index).equals(groupToTest.get(index))) {
                return false;
            }
        }

        return true;
    }

    protected UnaryOperator<Point> getNextJetMovement(List<Character> jetPattern, int jetCount) {
        char jet = jetPattern.get(jetCount % jetPattern.size());
        UnaryOperator<Point> movement;
        switch (jet) {
            case '<':
                movement = Point::decreaseX;
                break;
            case '>':
                movement = Point::increaseX;
                break;
            default:
                throw new AdventOfCodeException("Unsupported jet movement: " + jet);
        }
        return movement;
    }

    protected boolean overlaps(List<String> verticalChamber, Rock rock, Point rockPosition) {
        for (int y = 0; y < rock.shape.length; y++) {
            for (int x = 0; x < rock.shape[y].length; x++) {
                Character rockContent = rock.shape[y][x];
                if (rockContentOverlaps(verticalChamber, rockPosition, x, y, rockContent)) {
                    return true;
                }
            }
        }

        return false;
    }

    protected boolean rockContentOverlaps(List<String> verticalChamber, Point rockPosition, int rockShapeX, int rockShapeY, Character rockContent) {
        if (rockContent.equals(ROCK)) {
            Point chamberPoint = new Point(rockPosition.x + rockShapeX, rockPosition.y - rockShapeY);

            if (chamberPoint.x < 0 || chamberPoint.x >= VERTICAL_CHAMBER_WIDTH) {
                return true;
            }

            if (chamberPoint.y < 0) {
                return true;
            }

            if (chamberPoint.y < verticalChamber.size() && verticalChamber.get(chamberPoint.y).charAt(chamberPoint.x) == ROCK) {
                return true;
            }
        }
        return false;
    }

    protected void addRock(LinkedList<String> verticalChamber, Point rockPosition, Rock rock) {
        for (int y = rock.shape.length - 1; y >= 0; y--) {
            int yChamber = rockPosition.y - y;
            StringBuilder line = new StringBuilder(IntStream.range(0, VERTICAL_CHAMBER_WIDTH)
                .mapToObj(i -> ".")
                .collect(Collectors.joining()));
            if (yChamber < verticalChamber.size()) {
                line = new StringBuilder(verticalChamber.get(yChamber));
            }
            for (int x = 0; x < rock.shape[y].length; x++) {
                int xChamber = rockPosition.x + x;
                Character rockContent = rock.shape[y][x];
                if (rockContent.equals(ROCK)) {
                    line.replace(xChamber, xChamber + 1, String.valueOf(rockContent));
                }
            }
            if (yChamber < verticalChamber.size()) {
                verticalChamber.remove(yChamber);
            }
            verticalChamber.add(yChamber, line.toString());
        }
    }

    protected void printChamber(LinkedList<String> verticalChamber) {
        for (int rowIndex = verticalChamber.size() - 1; rowIndex >= 0; rowIndex--) {
            log.debug("|{}|", verticalChamber.get(rowIndex));
        }
        log.debug("+-------+");
    }

    protected int getVerticalChamberHeight(List<String> verticalChamber) {
        return Math.toIntExact(verticalChamber.stream()
            .filter(StringUtils::isNotBlank)
            .count());
    }

    public static class Input {
        public static List<Character> parse() throws IOException {
            return CommonUtils.loadResource("/input.txt", Function.identity())
                .stream()
                .flatMap(line -> Stream.of(CommonUtils.toCharacterArray(line)))
                .collect(Collectors.toList());
        }
    }

    public static class Rock {

        Character[][] shape;

        public static List<Rock> parse() throws IOException {
            List<Rock> rocks = new ArrayList<>();
            List<String> rockLines = new ArrayList<>();

            CommonUtils.loadResource("/rocks.txt", Function.identity()).forEach(line -> {
                if (line.isEmpty()) {
                    rocks.add(toRock(rockLines));
                    rockLines.clear();
                } else {
                    rockLines.add(line);
                }
            });
            rocks.add(toRock(rockLines));

            return rocks;
        }

        public static Rock toRock(List<String> rockLines) {
            Rock rock = new Rock();
            rock.shape = rockLines.stream()
                .map(CommonUtils::toCharacterArray)
                .toArray(Character[][]::new);
            return rock;
        }

        public String shape() {
            StringBuilder sb = new StringBuilder();
            for (int x = 0; x < shape.length; x++) {
                Character[] row = shape[x];
                for (int y = 0; y < row.length; y++) {
                    sb.append(shape[x][y]);
                }
                sb.append("\n");
            }
            return sb.toString();
        }
    }

}