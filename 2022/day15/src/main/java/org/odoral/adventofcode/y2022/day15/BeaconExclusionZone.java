package org.odoral.adventofcode.y2022.day15;

import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.exception.AdventOfCodeException;
import org.odoral.adventofcode.common.model.KeyValue;
import org.odoral.adventofcode.common.model.Point;
import org.odoral.adventofcode.common.model.ValuedPoint;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BeaconExclusionZone {

    public static void main(String[] args) throws IOException {
        BeaconExclusionZone beaconExclusionZone = new BeaconExclusionZone();

        int count = beaconExclusionZone.countPositionsWithoutBeaconsInRow(2000000);
        log.info("There are {} positions without beacons.", count);

        long tuningFrequency = beaconExclusionZone.calculateTuningFrequency();
        log.info("Distress signal tuning frequency: {}.", tuningFrequency);

    }

    public int countPositionsWithoutBeaconsInRow(int yRow) throws IOException {
        List<KeyValue<ValuedPoint<Integer>, Point>> sensors = Input.load();
        Set<Point> elements = sensors.stream()
            .map(KeyValue::getValue)
            .collect(Collectors.toSet());
        int[] boundaries = calculateBoundaries(sensors, this::calculateRadiusBoundary);// minX, maxY, maxX, minY
        log.info("Boundaries: minX:{}, maxY:{}, maxX:{}, minY:{}", boundaries[0], boundaries[1], boundaries[2], boundaries[3]);
        int count = 0;
        for (int xIndex = boundaries[0]; xIndex <= boundaries[2]; xIndex++) {
            Point currentPoint = new Point(xIndex, yRow);
            if (!elements.contains(currentPoint) && sensors.stream()
                .anyMatch(s -> s.getKey().value >= calculateRadius(s.getKey(), currentPoint))) {
                count++;
            }
        }
        return count;
    }

    public long calculateTuningFrequency() throws IOException {
        List<KeyValue<ValuedPoint<Integer>, Point>> sensors = Input.load();

        int[] boundaries = calculateBoundaries(sensors, this::calculateSensorBoundary);// minX, maxY, maxX, minY
        log.info("Boundaries: minX:{}, maxY:{}, maxX:{}, minY:{}", boundaries[0], boundaries[1], boundaries[2], boundaries[3]);

        Point point = lookForDistressSignal(sensors, boundaries);

        return BigInteger.valueOf(4_000_000)
            .multiply(BigInteger.valueOf(point.x))
            .add(BigInteger.valueOf(point.y))
            .longValue();
    }

    protected Point lookForDistressSignal(List<KeyValue<ValuedPoint<Integer>, Point>> sensors, int[] boundaries) {
        for (int yIndex = boundaries[3]; yIndex <= boundaries[1]; yIndex++) {
            final int rowIndex = yIndex;
            List<int[]> sensorRanges = sensors.stream()
                .filter(s -> Math.abs(s.getKey().y - rowIndex) <= s.getKey().value)
                .map(s -> {
                    ValuedPoint<Integer> sensor = s.getKey();

                    return new int[]{
                        Math.max(sensor.x - sensor.value + (Math.abs(sensor.y - rowIndex)), boundaries[0]),
                        Math.min(sensor.x + sensor.value - (Math.abs(sensor.y - rowIndex)), boundaries[2])
                    };
                })
                .sorted(Comparator.comparingInt(a -> a[0]))
                .collect(Collectors.toList());
            List<int[]> mergedRanges = mergeRanges(sensorRanges);

            if (mergedRanges.size() > 1) {
                Point currentPoint = new Point(mergedRanges.get(0)[1] + 1, yIndex);
                log.info("Distress signal found at: {}", currentPoint);
                return currentPoint;
            }
        }
        throw new AdventOfCodeException("Distress signal not found");
    }

    protected List<int[]> mergeRanges(List<int[]> sensorRanges) {
        List<int[]> result = new ArrayList<>(sensorRanges);
        for (int index = 0; index < result.size(); index++) {
            int[] a1 = result.get(index);
            for (int otherIndex = index + 1; otherIndex < result.size(); otherIndex++) {
                int[] a2 = result.get(otherIndex);
                if (a1[0] <= a2[0] && a1[1] >= a2[1]) {
                    result.remove(otherIndex);
                    otherIndex--;
                } else if (a1[0] <= a2[0] && a1[1] >= a2[0] - 1) {
                    result.get(index)[1] = a2[1];
                    result.remove(otherIndex);
                    otherIndex--;
                }
            }
        }
        return result;
    }

    public void drawSensorsAndBeacons() throws IOException {
        Map<Point, Character> map = new HashMap<>();
        List<KeyValue<ValuedPoint<Integer>, Point>> sensors = Input.load();
        sensors.forEach(sensor -> {
            map.put(sensor.getKey(), 'S');
            map.put(sensor.getValue(), 'B');
        });
        int[] boundaries = calculateBoundaries(sensors, this::calculateSensorBoundary);// minX, maxY, maxX, minY
        for (int yIndex = boundaries[3]; yIndex <= boundaries[1]; yIndex++) {
            StringBuilder sb = new StringBuilder();
            for (int xIndex = boundaries[0]; xIndex <= boundaries[2]; xIndex++) {
                Point currentPoint = new Point(xIndex, yIndex);

                sb.append(map.getOrDefault(currentPoint, sensors.stream()
                    .anyMatch(sensor -> sensor.getKey().value >= calculateRadius(sensor.getKey(), currentPoint)) ? '#' : '.'));
            }
            log.debug(sb.toString());
        }
    }

    protected int[] calculateBoundaries(List<KeyValue<ValuedPoint<Integer>, Point>> sensors, Function<KeyValue<ValuedPoint<Integer>, Point>, int[]> calculateBoundary) {
        return sensors.stream()
            .map(calculateBoundary)
            .reduce(null, (a1, a2) -> {
                if (a1 == null) {
                    return a2;
                } else if (a2 == null) {
                    return a1;
                } else {
                    return new int[]{
                        Math.min(a1[0], a2[0]),
                        Math.max(a1[1], a2[1]),
                        Math.max(a1[2], a2[2]),
                        Math.min(a1[3], a2[3])
                    };
                }
            });
    }

    protected int[] calculateRadiusBoundary(KeyValue<ValuedPoint<Integer>, Point> sensor) {
        return new int[]{
            sensor.getKey().x - sensor.getKey().value,
            sensor.getKey().y + sensor.getKey().value,
            sensor.getKey().x + sensor.getKey().value,
            sensor.getKey().y - sensor.getKey().value
        };
    }

    protected int[] calculateSensorBoundary(KeyValue<ValuedPoint<Integer>, Point> sensor) {
        return new int[]{
            sensor.getKey().x,
            sensor.getKey().y,
            sensor.getKey().x,
            sensor.getKey().y
        };
    }

    public static int calculateRadius(Point sensor, Point beacon) {
        return Math.abs(sensor.x - beacon.x) + Math.abs(sensor.y - beacon.y);
    }

    public static class Input {

        public static final String SENSORX = "sensorx";
        public static final String SENSORY = "sensory";
        public static final String BEACONX = "beaconx";
        public static final String BEACONY = "beacony";
        public static final String INPUT_PATTERN = "Sensor at x=(?<" + SENSORX + ">[-0-9]+), y=(?<" + SENSORY + ">[-0-9]+): closest beacon is at x=(?<" + BEACONX + ">[-0-9]+), y=(?<" + BEACONY + ">[-0-9]+)";
        public static final Pattern PATTERN = Pattern.compile(INPUT_PATTERN);

        private Input() {
        }

        public static List<KeyValue<ValuedPoint<Integer>, Point>> load() throws IOException {
            return CommonUtils.loadResource("/input.txt", Input::parse);
        }

        protected static KeyValue<ValuedPoint<Integer>, Point> parse(String line) {
            Matcher matcher = PATTERN.matcher(line);
            if (matcher.find()) {
                Point sensor = new Point(
                    Integer.parseInt(matcher.group(SENSORX)),
                    Integer.parseInt(matcher.group(SENSORY)));
                Point beacon = new Point(
                    Integer.parseInt(matcher.group(BEACONX)),
                    Integer.parseInt(matcher.group(BEACONY)));
                return KeyValue.<ValuedPoint<Integer>, Point>builder()
                    .key(new ValuedPoint<>(sensor.x, sensor.y, calculateRadius(sensor, beacon)))
                    .value(beacon)
                    .build();
            } else {
                throw new AdventOfCodeException("Pattern not found: " + line);
            }
        }
    }

}