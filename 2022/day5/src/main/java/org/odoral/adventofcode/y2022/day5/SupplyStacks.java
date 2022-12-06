package org.odoral.adventofcode.y2022.day5;

import org.apache.commons.lang3.StringUtils;
import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.exception.AdventOfCodeException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Function;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SupplyStacks {

    public static final char NO_CRATE = ' ';

    public static void main(String[] args) {
        SupplyStacks supplyStacks = new SupplyStacks();
        Input input = supplyStacks.loadInput("/input.txt");
        Result result = supplyStacks.move(input.stacks, input.movements, supplyStacks.moveUsingCrateMover9000(input.stacks));
        log.info("Crates on top using crate mover 9000: {}", result.cratesOnTop);

        input = supplyStacks.loadInput("/input.txt");
        result = supplyStacks.move(input.stacks, input.movements, supplyStacks.moveUsingCrateMover9001(input.stacks));
        log.info("Crates on top using crate mover 9001: {}", result.cratesOnTop);
    }

    protected Input loadInput(String input) {
        Input result = new Input();
        result.stacks = new ArrayList<>();
        result.movements = new ArrayList<>();

        List<List<Character>> mappedStacks = new ArrayList<>();
        try {
            CommonUtils.loadResource(input, Function.identity())
                .stream()
                .filter(StringUtils::isNotBlank)
                .forEach(line -> {
                    if (line.startsWith("move")) {
                        result.movements.add(Movement.map(line));
                    } else {
                        mappedStacks.add(Input.mapStacks(line));
                    }
                });
        } catch (IOException e) {
            throw new AdventOfCodeException(e);
        }

        for (int rowIndex = mappedStacks.size() - 1; rowIndex >= 0; rowIndex--) {
            List<Character> crates = mappedStacks.get(rowIndex);
            for (int crateIndex = 0; crateIndex < crates.size(); crateIndex++) {
                Character crate = crates.get(crateIndex);
                if (result.stacks.size() <= crateIndex) {
                    result.stacks.add(new Stack<>());
                }
                if (crate == NO_CRATE) {
                    continue;
                }
                Stack<Character> stack = result.stacks.get(crateIndex);
                stack.add(crate);

            }
        }

        return result;
    }

    protected Result move(List<Stack<Character>> stacks, List<Movement> movements, Consumer<Movement> moveUsingCrateMover) {
        movements.forEach(moveUsingCrateMover);

        return new Result(getCratesOnTop(stacks));
    }

    protected String getCratesOnTop(List<Stack<Character>> stacks) {
        return stacks.stream()
            .map(Stack::peek)
            .map(String::valueOf)
            .reduce("", String::concat);
    }

    protected Consumer<Movement> moveUsingCrateMover9000(List<Stack<Character>> stacks) {
        return movement -> {
            Stack<Character> from = stacks.get(movement.from - 1);
            Stack<Character> to = stacks.get(movement.to - 1);
            for (int i = 0; i < movement.number; i++) {
                to.push(from.pop());
            }
        };
    }

    protected Consumer<Movement> moveUsingCrateMover9001(List<Stack<Character>> stacks) {
        return movement -> {
            Stack<Character> from = stacks.get(movement.from - 1);
            Stack<Character> to = stacks.get(movement.to - 1);
            Stack<Character> moveStack = new Stack<>();
            for (int i = 0; i < movement.number; i++) {
                moveStack.push(from.pop());
            }

            for (int i = 0; i < movement.number; i++) {
                to.push(moveStack.pop());
            }
        };
    }

    public static class Input {
        List<Stack<Character>> stacks;
        List<Movement> movements;

        public static List<Character> mapStacks(String line) {
            List<Character> result = new ArrayList<>();
            int stackCount = (line.length() + 1) / 4;

            for (int i = 0; i < stackCount; i++) {
                result.add(line.charAt((i * 4) + 1));
            }

            if (result.stream().allMatch(Character::isDigit)) {
                return Collections.emptyList();
            }

            return result;
        }
    }

    public static class Movement {
        final int number;
        final int from;
        final int to;

        private Movement(int number, int from, int to) {
            this.number = number;
            this.from = from;
            this.to = to;
        }

        public static Movement map(String line) {
            String[] fields = line.split("\\s");
            return new Movement(
                Integer.parseInt(fields[1]),
                Integer.parseInt(fields[3]),
                Integer.parseInt(fields[5])
            );
        }
    }

    public static class Result {

        final String cratesOnTop;

        public Result(String cratesOnTop) {
            this.cratesOnTop = cratesOnTop;
        }

    }
}