package org.odoral.adventofcode.common;

import org.apache.commons.lang3.StringUtils;
import org.odoral.adventofcode.common.model.ValuedPoint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CommonUtils {

    public static <T> List<T> loadResource(String resource, Function<String, T> mapFunction) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(resource.getClass().getResourceAsStream(resource))))) {
            return bufferedReader.lines()
                .map(mapFunction)
                .collect(Collectors.toList());
        }
    }

    public static Character[] toCharacterArray(String chain) {
        char[] chars = chain.toCharArray();
        Character[] character = new Character[chars.length];
        for (int i = 0; i < chars.length; i++) {
            character[i] = chars[i];
        }
        return character;
    }

    public static Integer[] toIntegerArray(String chain) {
        return toIntegerArray(chain, "|");
    }

    public static Integer[] toIntegerArray(String numbers, String separator) {
        return Arrays.stream(numbers.split(separator))
            .map(Integer::parseInt)
            .toArray(Integer[]::new);
    }

    public static long lcm(List<Integer> rates) {
        return rates.stream()
            .map(Long::new)
            .reduce(1L, CommonUtils::lcm);
    }

    public static long lcm(long a, long b) {
        return (a * b) / gcf(a, b);
    }

    public static long gcf(long a, long b) {
        if (b == 0) {
            return a;
        } else {
            return gcf(b, a % b);
        }
    }

    public static String toBinary(Long memValue, int zeroPadding) {
        return StringUtils.leftPad(Long.toBinaryString(memValue), zeroPadding, '0');
    }

    public static Long fromBinary(String binaryNumber) {
        return new BigInteger(binaryNumber, 2).longValue();
    }

    public static <T> List<ValuedPoint<T>> findNeighbours(T[][] map, int rowIndex, int columnIndex, boolean considerDiagonal) {
        List<ValuedPoint<T>> neighbours = new ArrayList<>();

        // Up
        if (rowIndex > 0) {
            neighbours.add(new ValuedPoint<>(rowIndex - 1, columnIndex, map[rowIndex - 1][columnIndex]));
        }
        // Down
        if (rowIndex < map.length - 1) {
            neighbours.add(new ValuedPoint<>(rowIndex + 1, columnIndex, map[rowIndex + 1][columnIndex]));
        }
        // Left
        if (columnIndex > 0) {
            neighbours.add(new ValuedPoint<>(rowIndex, columnIndex - 1, map[rowIndex][columnIndex - 1]));
        }
        // Right
        if (columnIndex < map[rowIndex].length - 1) {
            neighbours.add(new ValuedPoint<>(rowIndex, columnIndex + 1, map[rowIndex][columnIndex + 1]));
        }

        if (considerDiagonal) {
            neighbours.addAll(findDiagonalNeighbours(map, rowIndex, columnIndex));
        }

        return neighbours;
    }

    public static <T> List<ValuedPoint<T>> findDiagonalNeighbours(T[][] map, int rowIndex, int columnIndex) {
        List<ValuedPoint<T>> neighbours = new ArrayList<>();
        // Top left corner
        if (rowIndex > 0 && columnIndex > 0) {
            neighbours.add(new ValuedPoint<>(rowIndex - 1, columnIndex - 1, map[rowIndex - 1][columnIndex - 1]));
        }

        // Top right corner
        if (rowIndex > 0 && columnIndex < map[rowIndex].length - 1) {
            neighbours.add(new ValuedPoint<>(rowIndex - 1, columnIndex + 1, map[rowIndex - 1][columnIndex + 1]));
        }

        // Bottom left corner
        if (rowIndex < map.length - 1 && columnIndex > 0) {
            neighbours.add(new ValuedPoint<>(rowIndex + 1, columnIndex - 1, map[rowIndex + 1][columnIndex - 1]));
        }

        // Bottom right corner
        if (rowIndex < map.length - 1 && columnIndex < map[rowIndex].length - 1) {
            neighbours.add(new ValuedPoint<>(rowIndex + 1, columnIndex + 1, map[rowIndex + 1][columnIndex + 1]));
        }
        return neighbours;
    }
}
