package org.odoral.adventofcode.y2023.day6;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class WaitForItTest {

    protected WaitForIt waitForIt;

    @Before public void setUp() {
        waitForIt = new WaitForIt();
    }

    @Test public void test_scenario1() throws IOException {
        List<WaitForIt.Race> races = WaitForIt.Race.parse(CommonUtils.loadResource("/input.txt", Function.identity()), WaitForIt.Race.MAP_RACES_FIRST_PART);
        WaitForIt.Result result = waitForIt.getNumberOfWaysToBeatRecord(races);
        assertEquals(Arrays.asList(4L, 8L, 9L), result.numberOfWaysToBeatRecord);
        assertEquals(288L, result.solution);
    }

    @Test public void test_scenario2() throws IOException {
        List<WaitForIt.Race> races = WaitForIt.Race.parse(CommonUtils.loadResource("/input.txt", Function.identity()), WaitForIt.Race.MAP_RACES_SECOND_PART);
        WaitForIt.Result result = waitForIt.getNumberOfWaysToBeatRecord(races);
        assertEquals(Collections.singletonList(71503L), result.numberOfWaysToBeatRecord);
        assertEquals(71503L, result.solution);
    }
}
