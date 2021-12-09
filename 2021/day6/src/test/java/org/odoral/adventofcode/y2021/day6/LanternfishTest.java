package org.odoral.adventofcode.y2021.day6;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class LanternfishTest {

    protected Lanternfish lanternfish;

    @Before public void setUp() {
        lanternfish = new Lanternfish();
    }

    @Test public void test_scenario1() throws IOException {
        List<Integer> initialState = CommonUtils.loadResource("/input.txt", Function.identity())
            .stream()
            .flatMap(line -> Stream.of(line.split(",")))
            .map(Integer::parseInt)
            .collect(Collectors.toList());
        Lanternfish.Result result = lanternfish.simulate(initialState, 80);
        assertEquals(5934, result.count);
    }

    @Test public void test_scenario2() throws IOException {
        List<Integer> initialState = CommonUtils.loadResource("/input.txt", Function.identity())
            .stream()
            .flatMap(line -> Stream.of(line.split(",")))
            .map(Integer::parseInt)
            .collect(Collectors.toList());
        Lanternfish.Result result = lanternfish.simulate(initialState, 256);
        assertEquals(26984457539L, result.count);
    }

}