package org.odoral.adventofcode.y2023.day3;

import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.model.KeyValue;
import org.odoral.adventofcode.common.model.ValuedPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GearRatios {

    public static void main(String[] args) throws IOException {
        GearRatios gearRatios = new GearRatios();
        ValuedPoint<KeyValue<Character, List<Long>>>[][] engineSchematic = gearRatios.loadInputMap("/input.txt");
        Result result = gearRatios.getNumberAdjacentToSymbolSum(engineSchematic);
        log.info("There are {} valid parts.\nSum: {}", result.validParts.size(), result.sum);

        log.info("Gear ratios sum: {}", result.gearRatiosSum);
    }

    protected ValuedPoint<KeyValue<Character, List<Long>>>[][] loadInputMap(String resource) throws IOException {
        return CommonUtils.loadMatrixResource(resource, (xPos, content) -> {
            AtomicInteger yPos = new AtomicInteger();

            return Arrays.stream(CommonUtils.toCharacterArray(content))
                .map(value -> new ValuedPoint<>(xPos, yPos.getAndIncrement(),
                    KeyValue.builder()
                        .key(value)
                        .value(new ArrayList<Long>())
                        .build()))
                .toArray(ValuedPoint[]::new);
        }, ValuedPoint.class);
    }

    protected Result getNumberAdjacentToSymbolSum(ValuedPoint<KeyValue<Character, List<Long>>>[][] engineSchematic) {
        List<Long> validPartNumbers = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < engineSchematic.length; rowIndex++) {
            long currentNumber = 0L;
            boolean isPartNumber = false;
            int columnIndexPartNumberStart = Integer.MAX_VALUE;
            List<Long> validPartNumbersInThisLine = new ArrayList<>();
            for (int columnIndex = 0; columnIndex < engineSchematic[rowIndex].length; columnIndex++) {
                if (Character.isDigit(engineSchematic[rowIndex][columnIndex].value.getKey())) {
                    currentNumber = currentNumber * 10 + Integer.parseInt(String.valueOf(engineSchematic[rowIndex][columnIndex].value.getKey()));
                    isPartNumber = isPartNumber || isPartNumber(engineSchematic, rowIndex, columnIndex);
                    columnIndexPartNumberStart = Math.min(columnIndexPartNumberStart, columnIndex);
                } else {
                    if (currentNumber > 0L && isPartNumber) {
                        validPartNumbersInThisLine.add(currentNumber);
                        assignPartNumber(currentNumber, rowIndex, columnIndexPartNumberStart, columnIndex, engineSchematic);
                    }
                    currentNumber = 0L;
                    isPartNumber = false;
                    columnIndexPartNumberStart = Integer.MAX_VALUE;
                }
            }
            if (currentNumber > 0L && isPartNumber) {
                validPartNumbersInThisLine.add(currentNumber);
                assignPartNumber(currentNumber, rowIndex, columnIndexPartNumberStart, engineSchematic[rowIndex].length, engineSchematic);
            }
            log.info("Valid part numbers in row: {} > {}", rowIndex, validPartNumbersInThisLine);
            validPartNumbers.addAll(validPartNumbersInThisLine);
        }

        return new Result(validPartNumbers, Arrays.stream(engineSchematic)
            .flatMap(Arrays::stream)
            .filter(keyValueValuedPoint -> keyValueValuedPoint.value.getKey() == '*' && keyValueValuedPoint.value.getValue().size() == 2)
            .peek(keyValueValuedPoint -> log.info("Gear detected at: {}", keyValueValuedPoint))
            .mapToLong(keyValueValuedPoint -> keyValueValuedPoint.value.getValue().stream().mapToLong(l -> l).reduce((l1, l2) -> l1 * l2).orElse(0L))
            .sum()
        );
    }

    protected void assignPartNumber(long partNumber, int rowIndex, int columnIndexPartNumberStart, int columnIndexPartNumberEnds, ValuedPoint<KeyValue<Character, List<Long>>>[][] engineSchematic) {
        IntStream.range(columnIndexPartNumberStart, columnIndexPartNumberEnds)
            .mapToObj(column -> CommonUtils.findNeighbours(engineSchematic, rowIndex, column, true))
            .flatMap(List::stream)
            .filter(valuedPointValuedPoint -> valuedPointValuedPoint.value.value.getKey() == '*')
            .distinct()
            .forEach(valuedPointValuedPoint -> valuedPointValuedPoint.value.value.getValue().add(partNumber));
    }

    protected boolean isPartNumber(ValuedPoint<KeyValue<Character, List<Long>>>[][] engineSchematic, int rowIndex, int columnIndex) {
        return CommonUtils.findNeighbours(engineSchematic, rowIndex, columnIndex, true).stream()
            .anyMatch(valuedPoint -> isSymbol(valuedPoint.value.value.getKey()));
    }

    protected boolean isSymbol(Character value) {
        return !Character.isDigit(value) && value != '.';
    }

    public static class Result {
        final List<Long> validParts;
        final Long sum;
        final Long gearRatiosSum;

        public Result(List<Long> validParts, long gearRatiosSum) {
            this.validParts = validParts;
            this.sum = validParts.stream().mapToLong(l -> l).sum();
            this.gearRatiosSum = gearRatiosSum;
        }
    }

}
