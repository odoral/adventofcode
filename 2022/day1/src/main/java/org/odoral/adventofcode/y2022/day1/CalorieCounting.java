package org.odoral.adventofcode.y2022.day1;

import org.apache.commons.lang3.StringUtils;
import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CalorieCounting {

    public static void main(String[] args) throws IOException {
        CalorieCounting calorieCounting = new CalorieCounting();
        List<String> calories = CommonUtils.loadResource("/input.txt", Function.identity());
        Result result = calorieCounting.getTopCaloriesElves(calories, 1);
        log.info("Elf with more calories carry: {}", result.calories);

        result = calorieCounting.getTopCaloriesElves(calories, 3);
        log.info("Top 3 calories elves carry: {}", result.calories);
    }

    protected Result getTopCaloriesElves(List<String> calories, int topElvesNumber) {
        List<Elf> caloriesPerElf = readElves(calories);

        return new Result(caloriesPerElf.stream()
            .sorted(Comparator.comparing(Elf::getCalories).reversed())
            .mapToLong(Elf::getCalories)
            .limit(topElvesNumber)
            .sum());
    }

    protected List<Elf> readElves(List<String> calories) {
        List<Elf> caloriesPerElf = new ArrayList<>();
        caloriesPerElf.add(new Elf());

        calories.forEach(value -> {
            if (StringUtils.isBlank(value)) {
                caloriesPerElf.add(new Elf());
            } else {
                caloriesPerElf.get(caloriesPerElf.size() - 1).calories += Long.parseLong(value);
            }
        });
        return caloriesPerElf;
    }

    public static class Elf {
        long calories = 0L;

        public long getCalories() {
            return calories;
        }
    }

    public static class Result {
        final long calories;

        public Result(long calories) {
            this.calories = calories;
        }

    }
}