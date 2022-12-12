package org.odoral.adventofcode.y2022.day11;

import org.mvel2.MVEL;
import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MonkeyInTheMiddle {

    public static void main(String[] args) throws IOException {
        MonkeyInTheMiddle monkeyInTheMiddle = new MonkeyInTheMiddle();
        Input input = Input.loadInput("/input.txt");
        Result result = monkeyInTheMiddle.doRounds(input, 20, 3);
        log.info("Monkey business level is: {}", result.getMonkeyBusinessLevel());

        input = Input.loadInput("/input.txt");
        result = monkeyInTheMiddle.doRounds(input, 10_000, 1);
        log.info("Monkey business level is: {}", result.getMonkeyBusinessLevel());
    }

    protected Result doRounds(Input input, int roundCount, int reliefDivisor) {
        for (int round = 1; round <= roundCount; round++) {
            input.monkeys.forEach((key, monkey) -> doRound(monkey, reliefDivisor, input.getDividers()));
        }

        return new Result(new ArrayList<>(input.monkeys.values()));
    }

    protected void doRound(Monkey monkey, int reliefDivisor, List<Integer> dividers) {
        new ArrayList<>(monkey.items).forEach(oldWorryLevel -> {
            long newWorryLevel = monkey.operation.apply(oldWorryLevel) / reliefDivisor;

            long reducedNewWorryLevel = newWorryLevel % dividers.stream()
                .reduce(1, Math::multiplyExact);

            if (monkey.test.test(reducedNewWorryLevel)) {
                monkey.testIsTrue.accept(oldWorryLevel, reducedNewWorryLevel);
            } else {
                monkey.testIsFalse.accept(oldWorryLevel, reducedNewWorryLevel);
            }
        });
    }

    public static class Input {
        static final String NEW_MONKEY_REGEX = "^Monkey (?<monkeyNumber>\\d+):$";
        public static final Pattern NEW_MONKEY_PATTERN = Pattern.compile(NEW_MONKEY_REGEX);
        static final String STARTING_ITEMS_REGEX = "^\\s+Starting items: (?<startingNumbers>[\\d, ]+)$";
        public static final Pattern STARTING_ITEMS_PATTERN = Pattern.compile(STARTING_ITEMS_REGEX);
        static final String OPERATION_REGEX = "^\\s+Operation: (?<operation>.*)$";
        public static final Pattern OPERATION_PATTERN = Pattern.compile(OPERATION_REGEX);
        static final String TEST_REGEX = "^\\s+Test: divisible by (?<divider>\\d+)$";
        public static final Pattern TEST_PATTERN = Pattern.compile(TEST_REGEX);
        static final String IF_TRUE_REGEX = "^\\s+If true: throw to monkey (?<throwTo>\\d+)$";
        public static final Pattern IF_TRUE_PATTERN = Pattern.compile(IF_TRUE_REGEX);
        static final String IF_FALSE_REGEX = "^\\s+If false: throw to monkey (?<throwTo>\\d+)$";
        public static final Pattern IF_FALSE_PATTERN = Pattern.compile(IF_FALSE_REGEX);

        Map<Integer, Monkey> monkeys = new HashMap<>();

        public static Input loadInput(String input) throws IOException {
            Input inputResult = new Input();
            AtomicInteger currentMonkey = new AtomicInteger(-1);
            CommonUtils.loadResource(input, Function.identity())
                .forEach(line -> processInputLine(line, inputResult, currentMonkey));

            return inputResult;
        }

        protected static void processInputLine(String line, Input inputResult, AtomicInteger currentMonkey) {
            Monkey monkey = inputResult.monkeys.get(currentMonkey.get());
            Matcher matcher;
            if ((matcher = NEW_MONKEY_PATTERN.matcher(line)).matches()) {
                monkey = new Monkey();
                monkey.number = Integer.parseInt(matcher.group("monkeyNumber"));
                currentMonkey.set(monkey.number);
                inputResult.monkeys.put(monkey.number, monkey);
            } else if ((matcher = STARTING_ITEMS_PATTERN.matcher(line)).matches()) {
                monkey.items = Stream.of(matcher.group("startingNumbers").split(","))
                    .map(String::trim)
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            } else if ((matcher = OPERATION_PATTERN.matcher(line)).matches()) {
                final Matcher fMatcher = matcher;
                final Monkey fMonkey = monkey;
                fMonkey.operation = (worryLevel) -> {
                    fMonkey.inspectCount++;
                    Map<Object, Object> context = new HashMap<>();
                    context.put("old", worryLevel);
                    MVEL.setProperty(context, "old", worryLevel);
                    return (Long) MVEL.eval(fMatcher.group("operation").split("=")[1], context);
                };
            } else if ((matcher = TEST_PATTERN.matcher(line)).matches()) {
                int divider = Integer.parseInt(matcher.group("divider"));
                monkey.divider = divider;
                monkey.test = (worryLevel) -> worryLevel % divider == 0;
            } else if ((matcher = IF_TRUE_PATTERN.matcher(line)).matches()) {
                int throwToMonkey = Integer.parseInt(matcher.group("throwTo"));
                int currentMonkeyIndex = currentMonkey.get();
                monkey.testIsTrue = (oldWorryLevel, newWorryLevel) -> {
                    inputResult.monkeys.get(currentMonkeyIndex).items.remove(oldWorryLevel);
                    inputResult.monkeys.get(throwToMonkey).items.add(newWorryLevel);
                };
            } else if ((matcher = IF_FALSE_PATTERN.matcher(line)).matches()) {
                int throwToMonkey = Integer.parseInt(matcher.group("throwTo"));
                int currentMonkeyIndex = currentMonkey.get();
                monkey.testIsFalse = (oldWorryLevel, newWorryLevel) -> {
                    inputResult.monkeys.get(currentMonkeyIndex).items.remove(oldWorryLevel);
                    inputResult.monkeys.get(throwToMonkey).items.add(newWorryLevel);
                };
            }
        }

        public List<Integer> getDividers() {
            return monkeys.values()
                .stream()
                .map(monkey -> monkey.divider)
                .collect(Collectors.toList());
        }
    }

    public static class Monkey {
        int number;
        long inspectCount;
        List<Long> items = new ArrayList<>();
        Function<Long, Long> operation;
        Predicate<Long> test;
        int divider;
        BiConsumer<Long, Long> testIsTrue;
        BiConsumer<Long, Long> testIsFalse;
    }

    public static class Result {

        final List<Monkey> monkeys;

        public Result(List<Monkey> monkeys) {
            this.monkeys = monkeys;
        }

        public long getMonkeyBusinessLevel() {
            return monkeys.stream()
                .sorted((m1, m2) -> Math.toIntExact(m2.inspectCount - m1.inspectCount))
                .limit(2)
                .mapToLong(monkey -> monkey.inspectCount)
                .reduce(1, Math::multiplyExact);
        }
    }

}