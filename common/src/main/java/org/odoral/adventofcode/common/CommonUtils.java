package org.odoral.adventofcode.common;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.odoral.adventofcode.common.model.ValuedPoint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CommonUtils {

    private CommonUtils() {}

    public static <T> List<T> loadResource(String resource, Function<String, T> mapFunction) throws IOException {
        return loadResource(resource, mapFunction, Collectors.toList());
    }

    public static <T, R> R loadResource(String resource, Function<String, T> mapFunction, Collector<T, ?, R> collector) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(resource.getClass().getResourceAsStream(resource))))) {
            return bufferedReader.lines()
                .map(mapFunction)
                .collect(collector);
        }
    }

    public static <T> T[][] loadMatrixResource(String resource, BiFunction<Integer, String, T[]> mapFunction, Class<T> clazz) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(resource.getClass().getResourceAsStream(resource))))) {
            AtomicInteger row = new AtomicInteger();
            List<T[]> result = bufferedReader.lines()
                .map(string -> mapFunction.apply(row.getAndIncrement(), string))
                .collect(Collectors.toList());
            return result.toArray((T[][]) Array.newInstance(clazz, result.size(), result.get(0).length));
        }
    }

    public static Character[] toCharacterArray(String chain) {
        return ArrayUtils.toObject(chain.toCharArray());
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
            .map(Long::valueOf)
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
        return findNeighbours(map, rowIndex, columnIndex, considerDiagonal, Function.identity());
    }

    public static <T, R> List<R> findNeighbours(T[][] map, int rowIndex, int columnIndex, boolean considerDiagonal, Function<ValuedPoint<T>, R> mapFunction) {
        List<R> neighbours = new ArrayList<>();

        // Up
        if (rowIndex > 0) {
            neighbours.add(mapFunction.apply(new ValuedPoint<>(rowIndex - 1, columnIndex, map[rowIndex - 1][columnIndex])));
        }
        // Down
        if (rowIndex < map.length - 1) {
            neighbours.add(mapFunction.apply(new ValuedPoint<>(rowIndex + 1, columnIndex, map[rowIndex + 1][columnIndex])));
        }
        // Left
        if (columnIndex > 0) {
            neighbours.add(mapFunction.apply(new ValuedPoint<>(rowIndex, columnIndex - 1, map[rowIndex][columnIndex - 1])));
        }
        // Right
        if (columnIndex < map[rowIndex].length - 1) {
            neighbours.add(mapFunction.apply(new ValuedPoint<>(rowIndex, columnIndex + 1, map[rowIndex][columnIndex + 1])));
        }

        if (considerDiagonal) {
            neighbours.addAll(findDiagonalNeighbours(map, rowIndex, columnIndex, mapFunction));
        }

        return neighbours;
    }

    public static <T> List<ValuedPoint<T>> findDiagonalNeighbours(T[][] map, int rowIndex, int columnIndex) {
        return findDiagonalNeighbours(map, rowIndex, columnIndex, Function.identity());
    }

    public static <T, R> List<R> findDiagonalNeighbours(T[][] map, int rowIndex, int columnIndex, Function<ValuedPoint<T>, R> mapFunction) {
        List<R> neighbours = new ArrayList<>();
        // Top left corner
        if (rowIndex > 0 && columnIndex > 0) {
            neighbours.add(mapFunction.apply(new ValuedPoint<>(rowIndex - 1, columnIndex - 1, map[rowIndex - 1][columnIndex - 1])));
        }

        // Top right corner
        if (rowIndex > 0 && columnIndex < map[rowIndex].length - 1) {
            neighbours.add(mapFunction.apply(new ValuedPoint<>(rowIndex - 1, columnIndex + 1, map[rowIndex - 1][columnIndex + 1])));
        }

        // Bottom left corner
        if (rowIndex < map.length - 1 && columnIndex > 0) {
            neighbours.add(mapFunction.apply(new ValuedPoint<>(rowIndex + 1, columnIndex - 1, map[rowIndex + 1][columnIndex - 1])));
        }

        // Bottom right corner
        if (rowIndex < map.length - 1 && columnIndex < map[rowIndex].length - 1) {
            neighbours.add(mapFunction.apply(new ValuedPoint<>(rowIndex + 1, columnIndex + 1, map[rowIndex + 1][columnIndex + 1])));
        }
        return neighbours;
    }

    public static <T> T[] extractColumn(T[][] matrix, Class<T> clazz, int columnIndex) {
        T[] result = (T[]) Array.newInstance(clazz, matrix.length);

        for (int rowIndex = 0; rowIndex < matrix.length; rowIndex++) {
            result[rowIndex] = matrix[rowIndex][columnIndex];
        }

        return result;
    }
}
