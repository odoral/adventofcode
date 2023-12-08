package org.odoral.adventofcode.y2023.day6;

import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WaitForIt {

    public static void main(String[] args) throws IOException {
        WaitForIt waitForIt = new WaitForIt();
        List<Race> races = Race.parse(CommonUtils.loadResource("/input.txt", Function.identity()), Race.MAP_RACES_FIRST_PART);
        Result result = waitForIt.getNumberOfWaysToBeatRecord(races);

        log.info("Solution: {}", result.solution);

        races = Race.parse(CommonUtils.loadResource("/input.txt", Function.identity()), Race.MAP_RACES_SECOND_PART);
        result = waitForIt.getNumberOfWaysToBeatRecord(races);

        log.info("Solution: {}", result.solution);
    }

    protected Result getNumberOfWaysToBeatRecord(List<Race> races) {
        return new Result(races.stream()
            .map(this::getNumberOfWaysToBeatRecord)
            .collect(Collectors.toList()));
    }

    protected Long getNumberOfWaysToBeatRecord(Race race) {
        long recordBeaten = 0;
        for (int buttonHoldDownInMillis = 0; buttonHoldDownInMillis < race.time; buttonHoldDownInMillis++) {
            long distance = calculateDistance(buttonHoldDownInMillis, race.time);

            if (distance > race.distance) {
                recordBeaten++;
            }
        }

        return recordBeaten;
    }

    protected long calculateDistance(int buttonHoldDownInMillis, long time) {
        return (time - buttonHoldDownInMillis) * buttonHoldDownInMillis;
    }

    public static class Result {
        final List<Long> numberOfWaysToBeatRecord;
        final long solution;

        public Result(List<Long> numberOfWaysToBeatRecord) {
            this.numberOfWaysToBeatRecord = numberOfWaysToBeatRecord;
            this.solution = numberOfWaysToBeatRecord.stream()
                .mapToLong(l -> l)
                .reduce(1L, (l1, l2) -> l1 * l2);
        }
    }

    public static class Race {
        public static final int TIME_LINE_INDEX = 0;
        public static final int DISTANCE_LINE_INDEX = 1;
        public static final Function<String, List<Long>> MAP_RACES_FIRST_PART = line -> Stream.of(line.split("\\s+"))
            .skip(1)
            .map(Long::parseLong)
            .collect(Collectors.toList());
        public static final Function<String, List<Long>> MAP_RACES_SECOND_PART = line -> Arrays.asList(
            Long.parseLong(Stream.of(line.split("\\s+"))
                .skip(1)
                .collect(Collectors.joining())
            ));
        final long time;
        final long distance;

        public Race(long time, long distance) {
            this.time = time;
            this.distance = distance;
        }

        public static List<Race> parse(List<String> configuration, Function<String, List<Long>> raceMapper) {
            List<List<Long>> parsedConfiguration = configuration.stream()
                .map(raceMapper)
                .collect(Collectors.toList());

            List<Race> races = new ArrayList<>();
            for (int raceIndex = 0; raceIndex < parsedConfiguration.get(TIME_LINE_INDEX).size(); raceIndex++) {
                races.add(new Race(parsedConfiguration.get(TIME_LINE_INDEX).get(raceIndex), parsedConfiguration.get(DISTANCE_LINE_INDEX).get(raceIndex)));
            }
            return races;
        }
    }

}
