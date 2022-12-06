package org.odoral.adventofcode.y2022.day4;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.model.KeyValue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CampCleanup {

    public static void main(String[] args) throws IOException {
        CampCleanup campCleanup = new CampCleanup();
        List<List<Range>> ranges = CommonUtils.loadResource("/input.txt", campCleanup::load);
        Result result = campCleanup.getOverlappedRanges(ranges, Range::fullyOverlap);
        log.info("Total fully overlapped ranges: {}", result.overlappedRanges.size());

        result = campCleanup.getOverlappedRanges(ranges, Range::overlap);
        log.info("Total overlapped ranges: {}", result.overlappedRanges.size());
    }

    protected List<Range> load(String ranges) {
        return Arrays.stream(ranges.split(","))
            .map(Range::map)
            .collect(Collectors.toList());
    }

    protected Result getOverlappedRanges(List<List<Range>> ranges, BiPredicate<Range, Range> overlapFunction) {
        return new Result(ranges.stream()
            .map(assignations -> {
                Range range1 = assignations.get(0);
                Range range2 = assignations.get(1);
                if (overlapFunction.test(range1, range2)) {
                    return KeyValue.<Range, Range>builder().key(range1).value(range2).build();
                } else if (overlapFunction.test(range2, range1)) {
                    return KeyValue.<Range, Range>builder().key(range2).value(range1).build();
                }
                return null;
            }).filter(Objects::nonNull)
            .collect(Collectors.toList()));
    }

    public static class Range {
        int from;
        int to;

        private Range(int from, int to) {
            this.from = from;
            this.to = to;
        }

        public static Range map(String range) {
            String[] fields = range.split("-");
            return new Range(Integer.parseInt(fields[0]), Integer.parseInt(fields[1]));
        }

        public boolean fullyOverlap(Range other) {
            return from <= other.from && to >= other.to;
        }

        public boolean overlap(Range other) {
            return to >= other.from && to <= other.to;
        }

        @Override public String toString() {
            return new ToStringBuilder(this)
                .append("from", from)
                .append("to", to)
                .toString();
        }
    }

    public static class Result {
        final List<KeyValue<Range, Range>> overlappedRanges;

        public Result(List<KeyValue<Range, Range>> overlappedRanges) {
            this.overlappedRanges = overlappedRanges;
        }

    }
}