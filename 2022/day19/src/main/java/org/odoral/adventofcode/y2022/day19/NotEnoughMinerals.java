package org.odoral.adventofcode.y2022.day19;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotEnoughMinerals {

    public static void main(String[] args) throws IOException {
        NotEnoughMinerals notEnoughMinerals = new NotEnoughMinerals();
        List<NotEnoughMinerals.BluePrint> bluePrints = NotEnoughMinerals.BluePrint.parse();
        int qualityLevel = notEnoughMinerals.calculateQualityLevel(bluePrints, 24);
        log.info("Total part1: {}", qualityLevel);

        long first3BlueprintsMultiply = notEnoughMinerals.calculateFirstThreeBlueprintsMultiply(bluePrints, 32);
        log.info("Total part2: {}", first3BlueprintsMultiply);
    }

    public int calculateQualityLevel(List<BluePrint> bluePrints, int minutes) {
        return bluePrints.stream()
            .mapToInt(bluePrint -> extractGeodes(bluePrint, minutes) * bluePrint.number)
            .sum();
    }

    public long calculateFirstThreeBlueprintsMultiply(List<BluePrint> bluePrints, int minutes) {
        return bluePrints.stream()
            .limit(3)
            .map(bluePrint -> extractGeodes(bluePrint, minutes))
            .mapToLong(i -> i)
            .reduce(1, Math::multiplyExact);

    }

    public int extractGeodes(BluePrint bluePrint, int minutes) {
        Set<State> visited = new HashSet<>();
        Map<Integer, State> bestOptionPerMinute = new HashMap<>();
        Map<Integer, Map<Integer, Queue<State>>> queuedStatesPerGeodesAndMinutesLeft = new HashMap<>();
        queuedStatesPerGeodesAndMinutesLeft.computeIfAbsent(minutes, (m) -> new HashMap<>())
            .computeIfAbsent(0, (g) -> new ArrayDeque<>())
            .add(State.build(minutes));

        for (int currentMinute = minutes; currentMinute >= 0; currentMinute--) {
            Map<Integer, Queue<State>> currentMinuteStates = queuedStatesPerGeodesAndMinutesLeft.remove(currentMinute);
            final int currentMinuteFinal = currentMinute;

            log.info("Checking blueprint {}, {} group states for minute: {}", bluePrint.number, currentMinuteStates.size(), currentMinuteFinal);
            currentMinuteStates.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getKey().compareTo(e1.getKey()))
                .limit(Math.max(4, currentMinuteStates.size() / 2))
                .forEach(e -> {
                    Integer key = e.getKey();
                    Queue<State> queuedStates = e.getValue();
                    log.info("Checking blueprint {} queued states for: {} -> {} ({})", bluePrint.number, currentMinuteFinal, key, queuedStates.size());
                    while (!queuedStates.isEmpty()) {
                        State state = queuedStates.poll();

                        List<State> potentialStates = getScenarios(state, bluePrint);

                        for (State potentialState : potentialStates) {
                            if (!visited.contains(potentialState)) {
                                if (potentialState.minutesLeft >= 0 &&
                                    isGoodOption(Optional.ofNullable(bestOptionPerMinute.get(potentialState.minutesLeft))
                                        .map(s -> s.wallet[Resource.GEODE.ordinal()])
                                        .orElse(0), potentialState)) {
                                    queuedStatesPerGeodesAndMinutesLeft.computeIfAbsent(potentialState.minutesLeft, (m) -> new HashMap<>())
                                        .computeIfAbsent((potentialState.wallet[Resource.GEODE.ordinal()] +
                                                (potentialState.robots[Resource.GEODE.ordinal()] * potentialState.minutesLeft) +
                                                bluePrint.getRobot(Resource.GEODE).costs
                                                    .stream()
                                                    .mapToInt(r -> state.wallet[r.resource.ordinal()] / r.number)
                                                    .min()
                                                    .orElse(0))
                                            , (g) -> new ArrayDeque<>())
                                        .add(potentialState);
                                    bestOptionPerMinute.put(potentialState.minutesLeft, potentialState);
                                }
                                visited.add(potentialState);
                            }
                        }
                    }
                });
        }

        State bestOption = bestOptionPerMinute.get(0);
        log.info("Best option: {}", bestOption);

        return bestOption.wallet[Resource.GEODE.ordinal()];
    }

    protected boolean isGoodOption(Integer numGeodes, State potentialState) {
        return numGeodes == null ||
            (potentialState.wallet[Resource.GEODE.ordinal()] + potentialState.minutesLeft) >= numGeodes;
    }

    protected List<State> getScenarios(State state, BluePrint bluePrint) {
        Robot geodeRobot = bluePrint.robots.get(Resource.GEODE);
        if (haveEnoughResourcesToBeBuilt(geodeRobot, state)) {
            State newState = state.collect();
            geodeRobot.costs.forEach(cost -> newState.wallet[cost.resource.ordinal()] -= cost.number);
            newState.robots[geodeRobot.resourceGenerate.ordinal()]++;
            return Collections.singletonList(newState);
        }
        List<State> newStates = bluePrint.robots.values()
            .stream()
            .filter(robot -> haveEnoughResourcesToBeBuilt(robot, state))
            .filter(robot -> !enoughRobots(state, bluePrint, robot))
            .map(robot -> {
                State newState = state.collect();
                robot.costs.forEach(cost -> newState.wallet[cost.resource.ordinal()] -= cost.number);
                newState.robots[robot.resourceGenerate.ordinal()]++;
                return newState;
            }).collect(Collectors.toList());

        newStates.add(state.collect());
        return newStates;
    }

    protected boolean enoughRobots(State state, BluePrint bluePrint, Robot robot) {
        int robotCount = state.robots[robot.resourceGenerate.ordinal()];
        return robotCount > 0 && robotCount >= Optional.ofNullable(bluePrint.maxCost.get(robot.resourceGenerate)).orElse(0);
    }

    protected boolean haveEnoughResourcesToBeBuilt(Robot robot, State state) {
        return robot.costs
            .stream()
            .allMatch(cost -> state.wallet[cost.resource.ordinal()] >= cost.number);
    }

    public static class State {
        int[] wallet;
        int[] robots;
        int minutesLeft;

        private State(int minutesLeft) {
            this.wallet = new int[]{0, 0, 0, 0};
            this.robots = new int[]{1, 0, 0, 0};
            this.minutesLeft = minutesLeft;
        }

        public static State build(int minutesLeft) {
            return new State(minutesLeft);
        }

        public State collect() {
            State state = copy();
            state.minutesLeft--;
            for (int robotIndex = 0; robotIndex < state.robots.length; robotIndex++) {
                state.wallet[robotIndex] += state.robots[robotIndex];
            }
            return state;
        }

        public State copy() {
            State state = new State(minutesLeft);
            state.wallet = Arrays.copyOf(this.wallet, wallet.length);
            state.robots = Arrays.copyOf(this.robots, robots.length);
            return state;
        }

        @Override public String toString() {
            return new StringJoiner(", ", State.class.getSimpleName() + "[", "]")
                .add("wallet=" + Arrays.toString(wallet))
                .add("robots=" + Arrays.toString(robots))
                .add("minutesLeft=" + minutesLeft)
                .toString();
        }

        @Override public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            State state = (State) o;

            return new EqualsBuilder().append(wallet, state.wallet).append(robots, state.robots).isEquals();
        }

        @Override public int hashCode() {
            return new HashCodeBuilder(17, 37).append(wallet).append(robots).toHashCode();
        }
    }

    public static class BluePrint {
        public static final String BLUEPRINT_ID = "blueprintid";
        public static final String ORE_ORE = "oreore";
        public static final String CLAY_ORE = "clayore";
        public static final String OBSIDIAN_ORE = "obsidianore";
        public static final String OBSIDIAN_CLAY = "obsidianclay";
        public static final String GEODE_ORE = "geodeore";
        public static final String GEODE_OBSIDIAN = "geodeobsidian";
        private static final String BLUEPRINT_REGEX = "Blueprint (?<" + BLUEPRINT_ID + ">[0-9]+)\\: " +
            "Each ore robot costs (?<" + ORE_ORE + ">\\d+) ore\\. " +
            "Each clay robot costs (?<" + CLAY_ORE + ">\\d+) ore\\. " +
            "Each obsidian robot costs (?<" + OBSIDIAN_ORE + ">\\d+) ore and (?<" + OBSIDIAN_CLAY + ">\\d+) clay\\. " +
            "Each geode robot costs (?<" + GEODE_ORE + ">\\d+) ore and (?<" + GEODE_OBSIDIAN + ">\\d+) obsidian\\.";
        private static final Pattern BLUEPRINT_PATTERN = Pattern.compile(BLUEPRINT_REGEX);

        int number;
        Map<Resource, Robot> robots;
        Map<Resource, Integer> maxCost;

        public static List<BluePrint> parse() throws IOException {
            return CommonUtils.loadResource("/input.txt", Function.identity())
                .stream()
                .map(line -> {
                    BluePrint bluePrint = new BluePrint();
                    Matcher matcher = BLUEPRINT_PATTERN.matcher(line);
                    if (matcher.find()) {
                        bluePrint.number = Integer.parseInt(matcher.group(BLUEPRINT_ID));
                        bluePrint.robots = Robot.map(line).stream().collect(Collectors.toMap(r -> r.resourceGenerate, Function.identity()));

                        bluePrint.maxCost = bluePrint.robots
                            .values()
                            .stream()
                            .flatMap(r -> r.costs.stream())
                            .collect(Collectors.toMap(c -> c.resource, c -> c.number, Math::max));
                    }
                    return bluePrint;
                })
                .collect(Collectors.toList());
        }

        public Robot getRobot(Resource resource) {
            return robots.get(resource);
        }

    }

    public static class Robot {
        List<Cost> costs;
        Resource resourceGenerate;

        public Robot(Resource resourceGenerate) {
            this.resourceGenerate = resourceGenerate;
        }

        @Override public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Robot robot = (Robot) o;

            return new EqualsBuilder().append(resourceGenerate, robot.resourceGenerate).isEquals();
        }

        @Override public int hashCode() {
            return new HashCodeBuilder(17, 37).append(resourceGenerate).toHashCode();
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Robot.class.getSimpleName() + "[", "]")
                .add("costs=" + costs)
                .add("resourceGenerate=" + resourceGenerate)
                .toString();
        }

        public static List<Robot> map(String line) {
            List<Robot> robots = new ArrayList<>();
            Matcher matcher = BluePrint.BLUEPRINT_PATTERN.matcher(line);

            if (matcher.find()) {
                Robot oreRobot = new Robot(Resource.ORE);
                oreRobot.costs = Arrays.asList(new Cost(Resource.ORE, Integer.parseInt(matcher.group(BluePrint.ORE_ORE))));
                robots.add(oreRobot);

                Robot clayRobot = new Robot(Resource.CLAY);
                clayRobot.costs = Arrays.asList(new Cost(Resource.ORE, Integer.parseInt(matcher.group(BluePrint.CLAY_ORE))));
                robots.add(clayRobot);

                Robot obsidianRobot = new Robot(Resource.OBSIDIAN);
                obsidianRobot.costs = Arrays.asList(
                    new Cost(Resource.ORE, Integer.parseInt(matcher.group(BluePrint.OBSIDIAN_ORE))),
                    new Cost(Resource.CLAY, Integer.parseInt(matcher.group(BluePrint.OBSIDIAN_CLAY)))
                );
                robots.add(obsidianRobot);

                Robot geodeRobot = new Robot(Resource.GEODE);
                geodeRobot.costs = Arrays.asList(
                    new Cost(Resource.ORE, Integer.parseInt(matcher.group(BluePrint.GEODE_ORE))),
                    new Cost(Resource.OBSIDIAN, Integer.parseInt(matcher.group(BluePrint.GEODE_OBSIDIAN)))
                );
                robots.add(geodeRobot);
            }
            return robots;
        }
    }

    public enum Resource {
        ORE,
        CLAY,
        OBSIDIAN,
        GEODE
    }

    public static class Cost {
        Resource resource;
        int number;

        public Cost(Resource resource, int number) {
            this.resource = resource;
            this.number = number;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Cost.class.getSimpleName() + "[", "]")
                .add("resource=" + resource)
                .add("number=" + number)
                .toString();
        }
    }

}