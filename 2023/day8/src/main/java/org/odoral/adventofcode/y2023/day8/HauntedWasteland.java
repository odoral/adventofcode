package org.odoral.adventofcode.y2023.day8;

import org.apache.commons.lang3.StringUtils;
import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.exception.AdventOfCodeException;
import org.odoral.adventofcode.common.model.KeyValue;
import org.odoral.adventofcode.common.model.LeftRight;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HauntedWasteland {

    public static void main(String[] args) throws IOException {
        HauntedWasteland hauntedWasteland = new HauntedWasteland();
        Input input = Input.load("/input.txt");
        Result result = hauntedWasteland.getStepsToReachZZZ("AAA", input, "ZZZ");
        log.info("Steps to reach ZZZ: {}", result.steps);

        result = hauntedWasteland.getStepsToReachNodesEndingOnZ(input);
        log.info("Steps to reach all Z nodes: {}", result.steps);
    }

    protected Result getStepsToReachZZZ(String from, Input input, String to) {
        AtomicInteger instructionIndex = new AtomicInteger();
        LinkedList<String> path = new LinkedList<>();
        path.add(from);

        do {
            String currentNode = path.getLast();
            String nextNode;
            Character instruction = getNextInstruction(input, instructionIndex.getAndIncrement());
            nextNode = getNextNode(input, instruction, currentNode);
            path.add(nextNode);
        } while (!path.getLast().equals(to));

        return new Result(path);
    }

    protected String getNextNode(Input input, Character instruction, String currentNode) {
        String nextNode;
        switch (instruction) {
            case 'L':
                nextNode = input.nodes.get(currentNode).getLeft();
                break;
            case 'R':
                nextNode = input.nodes.get(currentNode).getRight();
                break;
            default:
                throw new AdventOfCodeException("Unsupported instruction: " + instruction);
        }
        return nextNode;
    }

    protected Character getNextInstruction(Input input, long step) {
        return input.instructions[(int) (step % input.instructions.length)];
    }

    protected Result getStepsToReachNodesEndingOnZ(Input input) {
        List<String> startingNodes = input.nodes.keySet()
            .stream()
            .filter(node -> StringUtils.endsWith(node, "A"))
            .collect(Collectors.toList());
        List<String> currentNodes = new ArrayList<>(startingNodes);
        Long[] cycles = new Long[currentNodes.size()];
        long stepCounter = 0;
        do {
            Character instruction = getNextInstruction(input, stepCounter++);
            for (int nodeIndex = 0; nodeIndex < currentNodes.size(); nodeIndex++) {
                String currentNode = currentNodes.get(nodeIndex);
                String startingNode = startingNodes.get(nodeIndex);
                String nextNode = getNextNode(input, instruction, currentNode);

                currentNodes.set(nodeIndex, nextNode);

                if (nextNode.endsWith("Z") && cycles[nodeIndex] == null) {
                    log.info("Path starting on {} pass through {} step: {}", startingNode, nextNode, stepCounter);
                    cycles[nodeIndex] = stepCounter;
                }
            }
        } while (
            !currentNodes.stream().allMatch(node -> StringUtils.endsWith(node, "Z")) &&
                Stream.of(cycles).anyMatch(Objects::isNull)
        );

        if (Stream.of(cycles)
            .filter(Objects::nonNull)
            .count() == currentNodes.size()) {
            return new Result(CommonUtils.lcm(Stream.of(cycles).collect(Collectors.toList())));
        } else {
            return new Result(stepCounter);
        }

    }

    public static class Input {

        private static final String NODE_NAME = "nodename";
        private static final String LEFT = "left";
        private static final String RIGHT = "right";
        private static final String NODE_CONFIGURATION_REGEX = "(?<" + NODE_NAME + ">[A-Z1-9]+)\\s+=\\s+\\((?<" + LEFT + ">[A-Z1-9]+),\\s+(?<" + RIGHT + ">[A-Z1-9]+)\\)";

        final Character[] instructions;
        final Map<String, LeftRight<String, String>> nodes;

        private Input(Character[] instructions, Map<String, LeftRight<String, String>> nodes) {
            this.instructions = instructions;
            this.nodes = nodes;
        }

        public static Input load(String resource) throws IOException {
            List<String> instructions = CommonUtils.loadResource(resource, Function.identity());

            Pattern nodeConfigurationPattern = Pattern.compile(NODE_CONFIGURATION_REGEX);

            Map<String, LeftRight<String, String>> nodes = instructions.stream()
                .skip(1)
                .filter(StringUtils::isNotBlank)
                .map(nodeConfiguration -> {
                    Matcher matcher = nodeConfigurationPattern.matcher(nodeConfiguration);

                    if (!matcher.matches()) {
                        throw new AdventOfCodeException(nodeConfiguration + " doesn't match.");
                    }

                    return KeyValue.<String, LeftRight<String, String>>builder()
                        .key(matcher.group(NODE_NAME))
                        .value(LeftRight.<String, String>builder()
                            .left(matcher.group(LEFT))
                            .right(matcher.group(RIGHT))
                            .build())
                        .build();
                })
                .collect(Collectors.toMap(KeyValue::getKey, KeyValue::getValue));
            return new Input(
                CommonUtils.toCharacterArray(instructions.get(0)),
                nodes
            );
        }
    }

    public static class Result {

        final List<String> path;
        final long steps;

        public Result(long steps) {
            this.steps = steps;
            path = null;
        }

        public Result(List<String> path) {
            this.path = path;
            this.steps = path.size() - 1L;
        }
    }

}
