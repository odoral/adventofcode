package org.odoral.adventofcode.y2021.day1;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SonarSweepTest {

    protected SonarSweep sonarSweep;

    @Before public void setUp() {
        sonarSweep = new SonarSweep();
    }

    @Test public void test_scenario1() throws IOException {
        List<Integer> numbers = CommonUtils.loadResource("/input.txt", Integer::parseInt);
        SonarSweep.Result result = sonarSweep.getLargerMeasurements(numbers);
        assertEquals(7, result.largerMeasurements.length);
        assertArrayEquals(new Integer[]{200, 208, 210, 207, 240, 269, 263}, result.largerMeasurements);
    }

    @Test public void test_scenario2() throws IOException {
        List<Integer> numbers = CommonUtils.loadResource("/input.txt", Integer::parseInt);
        SonarSweep.Result result = sonarSweep.getLargerMeasurementsSlidingWindowMethod(numbers, SonarSweep.SLIDING_WINDOW);
        assertEquals(5, result.largerMeasurements.length);
        assertArrayEquals(new Integer[]{618, 647, 716, 769, 792}, result.largerMeasurements);
    }

}