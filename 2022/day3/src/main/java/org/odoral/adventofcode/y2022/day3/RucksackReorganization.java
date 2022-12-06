package org.odoral.adventofcode.y2022.day3;

import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.exception.AdventOfCodeException;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RucksackReorganization {

    public static final String NOT_FOUND = "Not found";

    public static void main(String[] args) throws IOException {
        RucksackReorganization rucksackReorganization = new RucksackReorganization();
        List<String> rucksacks = CommonUtils.loadResource("/input.txt", Function.identity());
        Result result = rucksackReorganization.getTotal(rucksacks, rucksackReorganization::rucksackPriorityPart1, 1);
        log.info("Total score scenario1: {}", result.total);

        result = rucksackReorganization.getTotal(rucksacks, rucksackReorganization::rucksackPriorityPart2, 3);
        log.info("Total score scenario2: {}", result.total);

    }

    protected Result getTotal(List<String> rucksacks, ToLongFunction<List<String>> toLongFunction, int groupSize) {
        AtomicInteger counter = new AtomicInteger();
        return new Result(rucksacks.stream()
            .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / groupSize))
            .values()
            .stream()
            .mapToLong(toLongFunction)
            .sum());
    }

    protected long rucksackPriorityPart1(List<String> rucksacks) {
        return rucksacks.stream()
            .mapToLong(this::rucksackPriorityPart1)
            .sum();

    }

    protected long rucksackPriorityPart1(String rucksack) {
        char itemOnBothCompartiments = getItemOnBothCompartiments(rucksack);
        return calculatePriority(itemOnBothCompartiments);
    }

    protected char getItemOnBothCompartiments(String rucksack) {
        String secondCompartiment = rucksack.substring(rucksack.length() / 2);
        for (int i = 0; i < rucksack.length() / 2; i++) {
            if (secondCompartiment.contains(rucksack.substring(i, i + 1))) {
                return rucksack.charAt(i);
            }
        }

        throw new AdventOfCodeException(NOT_FOUND);
    }

    protected long rucksackPriorityPart2(List<String> rucksacks) {
        Map<Character, Integer> characters = new HashMap<>();
        rucksacks.stream()
            .map(rucksack -> new HashSet<>(rucksack.chars().mapToObj(i -> (char) i).collect(Collectors.toList())))
            .flatMap(Collection::stream)
            .forEach(c -> characters.merge(c, 1, Integer::sum));

        Character itemOnThreeGroups = characters.entrySet()
            .stream()
            .filter(e -> e.getValue() == 3)
            .findFirst()
            .map(Map.Entry::getKey)
            .orElseThrow(() -> new AdventOfCodeException(NOT_FOUND));

        return calculatePriority(itemOnThreeGroups);
    }

    protected long calculatePriority(char itemOnBothCompartiments) {
        if (Character.isUpperCase(itemOnBothCompartiments)) {
            return ((int) itemOnBothCompartiments) - ((int) 'A') + 27L;
        } else {
            return ((int) itemOnBothCompartiments) - ((int) 'a') + 1L;
        }
    }

    public static class Result {
        final long total;

        public Result(long total) {
            this.total = total;
        }

    }
}