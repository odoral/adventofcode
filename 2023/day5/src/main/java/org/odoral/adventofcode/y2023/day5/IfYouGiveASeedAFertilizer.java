package org.odoral.adventofcode.y2023.day5;

import org.apache.commons.lang3.StringUtils;
import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.model.KeyValue;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IfYouGiveASeedAFertilizer {

    public static void main(String[] args) throws IOException {
        IfYouGiveASeedAFertilizer ifYouGiveASeedAFertilizer = new IfYouGiveASeedAFertilizer();
        Input input = ifYouGiveASeedAFertilizer.getLoadResource();
        Result result = ifYouGiveASeedAFertilizer.calculateLocations(input);
        log.info("Lowest location: {}", result.lowestLocation);

        result = ifYouGiveASeedAFertilizer.calculateLocationsForRanges(input);
        log.info("Lowest location: {}", result.lowestLocation);
    }

    public Input getLoadResource() throws IOException {
        List<String> lines = CommonUtils.loadResource("/input.txt", Function.identity());
        return Input.of(lines);
    }

    public Result calculateLocations(Input input) {
        return new Result(input.seeds.stream()
            .parallel()
            .map(input::calculateDestination)
            .min(Long::compare)
            .orElse(-1L)
        );
    }

    public Result calculateLocationsForRanges(Input input) {
        long lowestLocation = Long.MAX_VALUE;
        List<KeyValue<Long, Long>> seedRanges = parseRanges(input);

        for (KeyValue<Long, Long> range : seedRanges) {
            long from = range.getKey();
            long length = range.getValue();
            long position = 0;
            do {
                long sourceLocation = from + position;
                long destination = input.calculateDestination(sourceLocation);
                lowestLocation = Math.min(lowestLocation, destination);

                position += input.getLengthToNextRangeLimit(sourceLocation);
            } while (position < length);
            log.info("Group finished, current lowest location: {}", lowestLocation);
        }

        return new Result(lowestLocation);
    }

    protected List<KeyValue<Long, Long>> parseRanges(Input input) {
        List<KeyValue<Long, Long>> ranges = new ArrayList<>();
        for (int index = 0; index < input.seeds.size(); index += 2) {
            ranges.add(KeyValue.<Long, Long>builder()
                .key(input.seeds.get(index))
                .value(input.seeds.get(index + 1))
                .build());
        }
        return ranges;
    }

    public static class Result {
        final Long lowestLocation;

        public Result(Long lowestLocation) {
            this.lowestLocation = lowestLocation;
        }
    }

    public static class Input {
        final List<Long> seeds;
        final Map<String, CategoryMap> categoryMaps;

        private Input(List<Long> seeds, Map<String, CategoryMap> categoryMaps) {
            this.seeds = seeds;
            this.categoryMaps = categoryMaps;
        }

        public static Input of(List<String> lines) {
            List<String> seeds = new ArrayList<>();
            List<List<String>> categoryConfigurations = new ArrayList<>();
            List<String> currentCategoryConfiguration = new ArrayList<>();

            for (String line : lines) {
                if (StringUtils.isBlank(line)) {
                    continue;
                }

                if (line.startsWith("seeds:")) {
                    seeds.addAll(Arrays.stream(line.split(":")[1].trim().split("\\s+")).collect(Collectors.toList()));
                } else if (line.endsWith("map:")) {
                    currentCategoryConfiguration = new ArrayList<>();
                    categoryConfigurations.add(currentCategoryConfiguration);
                    currentCategoryConfiguration.add(line);
                } else {
                    currentCategoryConfiguration.add(line);
                }
            }

            return new Input(
                seeds.stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toList()),
                categoryConfigurations.stream()
                    .map(CategoryMap::of)
                    .collect(Collectors.toMap(categoryMap -> categoryMap.name, Function.identity()))
            );
        }

        public Long calculateDestination(long sourceLocation) {
            return Optional.of(sourceLocation)
                .map(location -> categoryMaps.get("seed-to-soil").convert(location))
                .map(location -> categoryMaps.get("soil-to-fertilizer").convert(location))
                .map(location -> categoryMaps.get("fertilizer-to-water").convert(location))
                .map(location -> categoryMaps.get("water-to-light").convert(location))
                .map(location -> categoryMaps.get("light-to-temperature").convert(location))
                .map(location -> categoryMaps.get("temperature-to-humidity").convert(location))
                .map(location -> categoryMaps.get("humidity-to-location").convert(location))
                .orElse(-1L);
        }

        public long getLengthToNextRangeLimit(long sourceLocation) {
            long nextRangeLimit = Long.MAX_VALUE;
            for (String convertName : Arrays.asList("seed-to-soil", "soil-to-fertilizer", "fertilizer-to-water",
                "water-to-light", "light-to-temperature", "temperature-to-humidity", "humidity-to-location")) {
                nextRangeLimit = Math.min(nextRangeLimit, categoryMaps.get(convertName).getLengthToReachLimit(sourceLocation));
                sourceLocation = categoryMaps.get(convertName).convert(sourceLocation);
            }
            return nextRangeLimit;
        }
    }

    public static class CategoryMap {
        final String name;
        final List<Range> ranges;

        private CategoryMap(String name, List<Range> ranges) {
            this.name = name;
            this.ranges = ranges;
        }

        public static CategoryMap of(List<String> lines) {
            return new CategoryMap(
                lines.get(0).split("\\s+")[0],
                lines.stream().skip(1).map(Range::of).collect(Collectors.toList())
            );
        }

        public Long convert(Long location) {
            return getRangeFor(location)
                .orElse(Range.OUT_OF_RANGE)
                .getDestination(location);
        }

        protected Optional<Range> getRangeFor(Long location) {
            return ranges.stream()
                .filter(range -> range.isInRange(location))
                .findFirst();
        }

        protected Optional<Range> getNextRangeFor(Long location) {
            return ranges.stream()
                .filter(range -> range.sourceRangeStart > location)
                .min(Comparator.comparing(range -> range.sourceRangeStart));
        }

        public long getLengthToReachLimit(long location) {
            Optional<Range> optionalRange = getRangeFor(location);

            if (optionalRange.isPresent()) {
                Range range = optionalRange.get();
                return range.sourceRangeStart - location + range.length;
            } else {
                return getNextRangeFor(location)
                    .map(range -> range.sourceRangeStart - location)
                    .orElse(Long.MAX_VALUE);
            }
        }
    }

    public static class Range {
        static final Range OUT_OF_RANGE = new Range(null, null, null) {
            @Override public Long getDestination(Long location) {
                return location;
            }
        };
        final Long destinationRangeStart;
        final Long sourceRangeStart;
        final Long length;

        private Range(Long destinationRangeStart, Long sourceRangeStart, Long length) {
            this.destinationRangeStart = destinationRangeStart;
            this.sourceRangeStart = sourceRangeStart;
            this.length = length;
        }

        public static Range of(String rangeConfiguration) {
            String[] values = rangeConfiguration.split("\\s+");
            return new Range(
                Long.parseLong(values[0]),
                Long.parseLong(values[1]),
                Long.parseLong(values[2])
            );
        }

        public boolean isInRange(Long location) {
            return location >= sourceRangeStart && location < sourceRangeStart + length;
        }

        public Long getDestination(Long location) {
            return location - sourceRangeStart + destinationRangeStart;
        }
    }
}
