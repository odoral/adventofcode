package org.odoral.adventofcode.y2023.day1;

import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Trebuchet {

    private static final List<String> DIGITS_AS_LETTERS = Arrays.asList(
        "zero",
        "one",
        "two",
        "three",
        "four",
        "five",
        "six",
        "seven",
        "eight",
        "nine"
    );

    public static void main(String[] args) throws IOException {
        Trebuchet trebuchet = new Trebuchet();
        List<String> instructionLines = CommonUtils.loadResource("/input.txt", Function.identity());
        Result result = trebuchet.getSumOfCalibrationValues(instructionLines);
        log.info("Calibration values are {}.\nSum: {}", result.values, result.sum);

        result = trebuchet.getSumOfCalibrationValues(trebuchet.fromLetterToDigit(instructionLines));
        log.info("Calibration values are {}.\nSum: {}", result.values, result.sum);
    }

    public Result getSumOfCalibrationValues(List<String> instructionLines) {
        List<Long> values = instructionLines.stream()
            .map(this::extractCalibrationValue)
            .collect(Collectors.toList());
        return new Result(values, values.stream().mapToLong(l -> l).sum());
    }

    protected Long extractCalibrationValue(String instructionLine) {
        Integer firstNumber = null;
        Integer lastNumber = null;

        for (char character : instructionLine.toCharArray()) {
            if (Character.isDigit(character)) {
                lastNumber = Integer.valueOf(String.valueOf(character));
                if (firstNumber == null) {
                    firstNumber = lastNumber;
                }
            }
        }

        return Optional.ofNullable(firstNumber).orElse(0) * 10L + Optional.ofNullable(lastNumber).orElse(0);
    }

    protected List<String> fromLetterToDigit(List<String> instructionLines) {
        return instructionLines.stream()
            .map(this::fromLetterToDigit)
            .collect(Collectors.toList());
    }

    protected String fromLetterToDigit(String instructionLine) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int lineIndex = 0; lineIndex < instructionLine.length(); lineIndex++) {
            boolean replaced = false;
            for (int digitAsLettersIndex = 0; digitAsLettersIndex < DIGITS_AS_LETTERS.size(); digitAsLettersIndex++) {
                String digitAsLetters = DIGITS_AS_LETTERS.get(digitAsLettersIndex);
                if (instructionLine.substring(lineIndex).startsWith(digitAsLetters)) {
                    log.debug("{} -> {}", digitAsLettersIndex, instructionLine.substring(lineIndex));
                    replaced = true;
                    stringBuilder.append(digitAsLettersIndex);
                    lineIndex += digitAsLetters.length() - 1;
                    break;
                }
            }
            if (!replaced) {
                stringBuilder.append(instructionLine.charAt(lineIndex));
            }
        }
        if (instructionLine.length() != stringBuilder.length()) {
            log.debug("{} -> {} -> {}", instructionLine, stringBuilder, extractCalibrationValue(stringBuilder.toString()));
        } else {
            log.debug("*{} -> {}", instructionLine, extractCalibrationValue(stringBuilder.toString()));
        }
        return stringBuilder.toString();
    }

    public static class Result {
        final List<Long> values;
        final Long sum;


        public Result(List<Long> values, Long sum) {
            this.values = values;
            this.sum = sum;
        }
    }
}
