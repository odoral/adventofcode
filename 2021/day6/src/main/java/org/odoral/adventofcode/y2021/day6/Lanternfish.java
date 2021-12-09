package org.odoral.adventofcode.y2021.day6;

import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Lanternfish {

    public static void main(String[] args) throws IOException {
        Lanternfish lanternfish = new Lanternfish();
        List<Integer> initialState = CommonUtils.loadResource("/input.txt", Function.identity())
            .stream()
            .flatMap(line -> Stream.of(line.split(",")))
            .map(Integer::parseInt)
            .collect(Collectors.toList());
        Lanternfish.Result result = lanternfish.simulate(initialState, 80);
        log.info("Result after {} days: {}", 80, result.count);

        result = lanternfish.simulate(initialState, 256);
        log.info("Result after {} days: {}", 256, result.count);
    }

    public Result simulate(List<Integer> initialState, int daysToSimulate) {
        Map<Integer, Long> generatedFishes = new HashMap<>();
        return new Result(initialState.stream()
            .mapToLong(i -> {
                Long fishesGenerated = generatedFishes.computeIfAbsent(i, key -> calculateFishesGenerated(key, daysToSimulate));
                log.info("{} fishes generated for {}", fishesGenerated, i);
                return fishesGenerated;
            })
            .sum());
    }

    public long calculateFishesGenerated(int timer, int daysToSimulate) {
        long count = 1;

        for (int day = timer + 1; day <= daysToSimulate; day += 7) {
            count += calculateFishesGenerated(8, daysToSimulate - day);
        }

        return count;
    }

    public static class Result {
        public final long count;

        public Result(long count) {
            this.count = count;
        }
    }

}