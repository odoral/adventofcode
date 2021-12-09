package org.odoral.adventofcode.y2021.day8;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class SevenSegmentSearchTest {

    protected SevenSegmentSearch sevenSegmentSearch;

    @Before public void setUp() {
        sevenSegmentSearch = new SevenSegmentSearch();
    }

    @Test public void test_scenario1() throws IOException {
        List<SevenSegmentSearch.Input> inputs = CommonUtils.loadResource("/input.txt", Function.identity())
            .stream()
            .map(SevenSegmentSearch.Input::mapInput)
            .collect(Collectors.toList());
        SevenSegmentSearch.Result result = sevenSegmentSearch.countInstances(inputs, Arrays.asList(1, 4, 7, 8));
        assertEquals(26, result.numInstances);
    }

    @Test public void test_scenario2() throws IOException {
        List<SevenSegmentSearch.Input> inputs = CommonUtils.loadResource("/input.txt", Function.identity())
            .stream()
            .map(SevenSegmentSearch.Input::mapInput)
            .collect(Collectors.toList());
        SevenSegmentSearch.Result result = sevenSegmentSearch.sum(inputs);
        assertEquals(61229, result.sum);
    }

}