package org.odoral.adventofcode.y2021.day10;

import lombok.extern.slf4j.Slf4j;
import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class SyntaxScoring {

    public static final List<Character> STARTING_CHARACTERS = Arrays.asList('(', '[', '{', '<');
    public static final List<Character> ENDING_CHARACTERS = Arrays.asList(')', ']', '}', '>');

    public static void main(String[] args) throws IOException {
        SyntaxScoring syntaxScoring = new SyntaxScoring();
        List<String> lines = CommonUtils.loadResource("/input.txt", Function.identity());
        ResultScenario1 resultScenario1 = syntaxScoring.findIncorrectClosingCharacters(lines);
        log.info("There are {} wrong lines with a total syntax error score of: {}", resultScenario1.incorrectClosingCharacters.size(), resultScenario1.calculateSyntaxErrorScore());

        SyntaxScoring.ResultScenario2 resultScenario2 = syntaxScoring.completeIncompleteLines(lines);
        log.info("Middle score is: {}", resultScenario2.calculateMiddleScore());
    }

    public ResultScenario1 findIncorrectClosingCharacters(List<String> lines) {
        return new ResultScenario1(lines.stream()
                .map(this::findFirstWrongEnclosingCharacter)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList()));
    }

    public Optional<Character> findFirstWrongEnclosingCharacter(String line) {
        ArrayDeque<Character> stack = new ArrayDeque<>();
        return Stream.of(CommonUtils.toCharacterArray(line))
                .filter(c -> {
                    switch (c) {
                        case '(':
                        case '[':
                        case '{':
                        case '<':
                            stack.push(c);
                            return false;

                        case ')':
                            return !stack.pop().equals('(');
                        case ']':
                            return !stack.pop().equals('[');
                        case '}':
                            return !stack.pop().equals('{');
                        case '>':
                            return !stack.pop().equals('<');

                        default:
                            throw new IllegalArgumentException(c + " is not a valid character.");
                    }
                }).findFirst();
    }

    public ResultScenario2 completeIncompleteLines(List<String> lines) {
        return new ResultScenario2(lines.stream()
                .filter(line -> !findFirstWrongEnclosingCharacter(line).isPresent())
                .map(this::completeLine)
                .collect(Collectors.toList()));
    }

    protected String completeLine(String line) {
        Deque<Character> stack = new ConcurrentLinkedDeque<>();
        Arrays.stream(CommonUtils.toCharacterArray(line))
                .forEach(c -> {
                    if (STARTING_CHARACTERS.contains(c)) {
                        stack.push(c);
                    } else {
                        stack.pop();
                    }
                });
        return stack.stream()
                .map(c -> ENDING_CHARACTERS.get(STARTING_CHARACTERS.indexOf(c)))
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    public static class ResultScenario1 {
        public final List<Character> incorrectClosingCharacters;

        public ResultScenario1(List<Character> incorrectClosingCharacters) {
            this.incorrectClosingCharacters = incorrectClosingCharacters;
        }

        public int calculateSyntaxErrorScore() {
            return incorrectClosingCharacters.stream()
                    .mapToInt(this::score)
                    .sum();
        }

        protected int score(Character character) {
            switch (character) {
                case ')':
                    return 3;
                case ']':
                    return 57;
                case '}':
                    return 1197;
                case '>':
                    return 25137;
                default:
                    throw new IllegalArgumentException(character + " is not a valid enclosing character.");
            }
        }
    }

    public static class ResultScenario2 {
        public final List<String> incompleteLines;

        public ResultScenario2(List<String> incompleteLines) {
            this.incompleteLines = incompleteLines;
        }

        public long calculateMiddleScore() {
            List<Long> scores = incompleteLines.stream()
                    .map(this::calculateMiddleScore)
                    .sorted()
                    .collect(Collectors.toList());

            return scores.get((scores.size() / 2));
        }

        protected long calculateMiddleScore(String line) {
            long totalScore = 0;

            char[] chars = line.toCharArray();
            for (int index = 0; index < chars.length; index++) {
                char c = chars[index];

                totalScore *= 5;
                totalScore += ENDING_CHARACTERS.indexOf(c) + 1;
            }

            return totalScore;
        }
    }
}