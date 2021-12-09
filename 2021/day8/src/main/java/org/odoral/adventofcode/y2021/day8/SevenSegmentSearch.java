package org.odoral.adventofcode.y2021.day8;

import org.apache.commons.lang3.StringUtils;
import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SevenSegmentSearch {

    public static final Map<Integer, Integer> SIGNALS_PER_NUMBER = new HashMap<Integer, Integer>() {{
        put(1, 2);
        put(4, 4);
        put(7, 3);
        put(8, 7);
    }};

    public static void main(String[] args) throws IOException {
        SevenSegmentSearch sevenSegmentSearch = new SevenSegmentSearch();
        List<SevenSegmentSearch.Input> inputs = CommonUtils.loadResource("/input.txt", Function.identity())
            .stream()
            .map(SevenSegmentSearch.Input::mapInput)
            .collect(Collectors.toList());
        List<Integer> numbersToCount = Arrays.asList(1, 4, 7, 8);
        SevenSegmentSearch.Result result = sevenSegmentSearch.countInstances(inputs, numbersToCount);
        log.info("Number of occurrences for {}: {}", numbersToCount, result.numInstances);

        result = sevenSegmentSearch.sum(inputs);
        log.info("Sum of numbers is: {}", result.sum);
    }

    public Result countInstances(List<Input> inputs, List<Integer> numbersToCount) {
        List<Integer> validSignals = numbersToCount.stream()
            .map(SIGNALS_PER_NUMBER::get)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        return new Result(inputs.stream()
            .flatMap(i -> Stream.of(i.fourDigitValue))
            .filter(s -> validSignals.contains(s.length()))
            .count(), -1);
    }

    public Result sum(List<Input> inputs) {
        return new Result(-1, inputs.stream()
            .mapToInt(input -> {
                Map<String, Integer> digitPerSignal = loadSignals(input);
                return Integer.parseInt(Stream.of(input.fourDigitValue)
                    .map(this::sortSignal)
                    .map(digitPerSignal::get)
                    .map(String::valueOf)
                    .collect(Collectors.joining()));
            }).sum());
    }

    protected String sortSignal(String signal) {
        return Stream.of(CommonUtils.toCharacterArray(signal))
            .map(String::valueOf)
            .sorted()
            .distinct()
            .collect(Collectors.joining());
    }

    protected Map<String, Integer> loadSignals(Input input) {
        Map<Integer, String> signalMap = new HashMap<>();

        signalMap.put(1, sortSignal(findSignalBy(input, filterByLength(2))));
        signalMap.put(3, sortSignal(findSignalBy(input, filterByLength(5).and(filterByNumMatches(signalMap.get(1), 2)))));
        signalMap.put(4, sortSignal(findSignalBy(input, filterByLength(4))));
        signalMap.put(2, sortSignal(findSignalBy(input, filterByLength(5).and(filterByNumMatches(signalMap.get(4), 2)))));
        signalMap.put(5, sortSignal(findSignalBy(input, filterByLength(5).and(filterByNumMatches(signalMap.get(2), 3)))));
        signalMap.put(6, sortSignal(findSignalBy(input, filterByLength(6).and(filterByNumMatches(signalMap.get(1), 1)))));
        signalMap.put(7, sortSignal(findSignalBy(input, filterByLength(3))));
        signalMap.put(8, sortSignal(findSignalBy(input, filterByLength(7))));
        signalMap.put(9, sortSignal(findSignalBy(input, filterByLength(6).and(filterByNumMatches(signalMap.get(3), 5)))));
        signalMap.put(0, sortSignal(findSignalBy(input, filterByLength(6).and(filterByNumMatches(signalMap.get(3), 4)).and(filterByNumMatches(signalMap.get(1), 2)))));

        log.debug("{} -> {}", input.signalPattern, signalMap);
        return signalMap.entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    protected String findSignalBy(Input inputs, Predicate<String> filter) {
        return Stream.of(inputs.signalPattern)
            .filter(filter)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("There is no signal for filter: " + filter));
    }

    protected Predicate<String> filterByLength(int length) {
        return signal -> signal.length() == length;
    }

    protected Predicate<String> filterByNumMatches(String otherSignal, int numMatches) {
        return signal -> matches(signal, otherSignal, numMatches);
    }

    protected boolean matches(String signalA, String signalB, int numMatches) {
        return Stream.of(CommonUtils.toCharacterArray(signalB))
            .filter(c -> StringUtils.countMatches(signalA, c) == 1)
            .count() == numMatches;
    }

    public static class Input {
        public final String[] signalPattern;
        public final String[] fourDigitValue;

        public Input(String[] signalPattern, String[] fourDigitValue) {
            this.signalPattern = signalPattern;
            this.fourDigitValue = fourDigitValue;
        }

        public static Input mapInput(String line) {
            String[] fields = line.split(" \\| ");

            return new Input(fields[0].split(" "), fields[1].split(" "));
        }
    }

    public static class Result {
        public final long numInstances;
        public final int sum;

        public Result(long numInstances, int sum) {
            this.numInstances = numInstances;
            this.sum = sum;
        }
    }

}