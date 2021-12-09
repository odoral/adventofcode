package org.odoral.adventofcode.y2021.day7;

import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TheTreacheryOfWhales {

    public static final BiFunction<List<Integer>, Integer, Integer> CONSTANT_RATE = TheTreacheryOfWhales::constantCalculation;
    public static final BiFunction<List<Integer>, Integer, Integer> INCREMENTAL_RATE = TheTreacheryOfWhales::incrementalCalculation;

    public static void main(String[] args) throws IOException {
        TheTreacheryOfWhales theTreacheryOfWhales = new TheTreacheryOfWhales();
        List<Integer> initialState = CommonUtils.loadResource("/input.txt", Function.identity())
            .stream()
            .flatMap(line -> Stream.of(line.split(",")))
            .map(Integer::parseInt)
            .collect(Collectors.toList());
        TheTreacheryOfWhales.Result result = theTreacheryOfWhales.cheapestAlignment(initialState, CONSTANT_RATE);
        log.info("Cheapest horizontal position is {} [{}]", result.cheapestPosition, result.fuelCost);

        result = theTreacheryOfWhales.cheapestAlignment(initialState, INCREMENTAL_RATE);
        log.info("Cheapest horizontal position for new calculation is {} [{}]", result.cheapestPosition, result.fuelCost);
    }

    public static Integer constantCalculation(List<Integer> initialState, int horizontalPosition) {
        return initialState.stream()
            .mapToInt(i -> Math.abs(i - horizontalPosition))
            .sum();
    }

    public static Integer incrementalCalculation(List<Integer> initialState, int horizontalPosition) {
        Map<Integer, Integer> movementCost = new HashMap<>();
        return initialState.stream()
            .mapToInt(i -> movementCost.computeIfAbsent(Math.abs(i - horizontalPosition), TheTreacheryOfWhales::calculateIncrementalMovement))
            .sum();
    }

    public static Integer calculateIncrementalMovement(int moves) {
        int total = 0;

        for (int i = 1; i <= moves; i++) {
            total += i;
        }

        return total;
    }

    public Result cheapestAlignment(List<Integer> initialState, BiFunction<List<Integer>, Integer, Integer> fuelConsumptionCalculation) {
        Map<Integer, Integer> fuelCostPerPosition = IntStream.range(
                initialState.stream().min(Integer::compareTo).orElse(0),
                initialState.stream().max(Integer::compareTo).orElse(0))
            .boxed()
            .collect(Collectors.toMap(Function.identity(), i -> fuelConsumptionCalculation.apply(initialState, i)));

        return fuelCostPerPosition.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue())
            .limit(1)
            .map(e -> new Result(e.getKey(), e.getValue()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("There is no eligible position"));
    }

    public static class Result {
        public final int cheapestPosition;
        public final int fuelCost;

        public Result(int cheapestPosition, int fuelCost) {
            this.cheapestPosition = cheapestPosition;
            this.fuelCost = fuelCost;
        }
    }

}