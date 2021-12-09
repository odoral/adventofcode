package org.odoral.adventofcode.y2021.day7;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class TheTreacheryOfWhalesTest {

    protected TheTreacheryOfWhales theTreacheryOfWhales;

    @Before public void setUp() {
        theTreacheryOfWhales = new TheTreacheryOfWhales();
    }

    @Test public void test_scenario1() throws IOException {
        List<Integer> crabPositions = CommonUtils.loadResource("/input.txt", Function.identity())
            .stream()
            .flatMap(line -> Stream.of(line.split(",")))
            .map(Integer::parseInt)
            .collect(Collectors.toList());
        TheTreacheryOfWhales.Result result = theTreacheryOfWhales.cheapestAlignment(crabPositions, TheTreacheryOfWhales.CONSTANT_RATE);
        assertEquals(2, result.cheapestPosition);
        assertEquals(37, result.fuelCost);
    }

    @Test public void test_scenario2() throws IOException {
        List<Integer> crabPositions = CommonUtils.loadResource("/input.txt", Function.identity())
            .stream()
            .flatMap(line -> Stream.of(line.split(",")))
            .map(Integer::parseInt)
            .collect(Collectors.toList());
        TheTreacheryOfWhales.Result result = theTreacheryOfWhales.cheapestAlignment(crabPositions, TheTreacheryOfWhales.INCREMENTAL_RATE);
        assertEquals(5, result.cheapestPosition);
        assertEquals(168, result.fuelCost);
    }

}