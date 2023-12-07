package org.odoral.adventofcode.y2023.day3;

import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.model.ValuedPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GearRatios {

    public static void main(String[] args) throws IOException {
        GearRatios gearRatios = new GearRatios();
        ValuedPoint<Character>[][] engineSchematic = gearRatios.loadInputMap("/input.txt");
        Result result = gearRatios.getNumberAdjacentToSymbolSum(engineSchematic);
        log.info("There are {} valid parts.\nSum: {}", result.validParts.size(), result.sum);
    }

    protected ValuedPoint<Character>[][] loadInputMap(String resource) throws IOException {
        return CommonUtils.loadMatrixResource(resource, (xPos, content) -> {
            AtomicInteger yPos = new AtomicInteger();

            return Arrays.stream(CommonUtils.toCharacterArray(content))
                .map(value -> new ValuedPoint<>(xPos, yPos.getAndIncrement(), value))
                .toArray(ValuedPoint[]::new);
        }, ValuedPoint.class);
    }

    protected Result getNumberAdjacentToSymbolSum(ValuedPoint<Character>[][] engineSchematic) {
        List<Long> validPartNumbers = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < engineSchematic.length; rowIndex++) {
            long currentNumber = 0L;
            boolean isPartNumber = false;
            List<Long> validPartNumbersInThisLine = new ArrayList<>();
            for (int columnIndex = 0; columnIndex < engineSchematic[rowIndex].length; columnIndex++) {
                if (Character.isDigit(engineSchematic[rowIndex][columnIndex].value)) {
                    currentNumber = currentNumber * 10 + Integer.parseInt(String.valueOf(engineSchematic[rowIndex][columnIndex].value));
                    isPartNumber = isPartNumber || isPartNumber(engineSchematic, rowIndex, columnIndex);
                } else {
                    if (currentNumber > 0L && isPartNumber) {
                        validPartNumbersInThisLine.add(currentNumber);
                    }
                    currentNumber = 0L;
                    isPartNumber = false;
                }
            }
            if (currentNumber > 0L && isPartNumber) {
                validPartNumbersInThisLine.add(currentNumber);
            }
            log.info("Valid part numbers in row: {} > {}", rowIndex, validPartNumbersInThisLine);
            validPartNumbers.addAll(validPartNumbersInThisLine);
        }

        return new Result(validPartNumbers);
    }

    protected boolean isPartNumber(ValuedPoint<Character>[][] engineSchematic, int rowIndex, int columnIndex) {
        return CommonUtils.findNeighbours(engineSchematic, rowIndex, columnIndex, true).stream()
            .anyMatch(valuedPoint -> isSymbol(valuedPoint.value.value));
    }

    protected boolean isSymbol(Character value) {
        return !Character.isDigit(value) && value != '.';
    }

    public static class Result {
        final List<Long> validParts;
        final Long sum;

        public Result(List<Long> validParts) {
            this.validParts = validParts;
            this.sum = validParts.stream().mapToLong(l -> l).sum();
        }
    }
}
