package org.odoral.adventofcode.y2021.day2;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class DiveTest {

    protected Dive dive;

    @Before public void setUp() {
        dive = new Dive();
    }

    @Test public void test_scenario1() throws IOException {
        List<String> plannedRoute = CommonUtils.loadResource("/input.txt", Function.identity());
        Dive.Result result = dive.processPlannedRoute(plannedRoute);
        assertEquals(15, result.destination.x);
        assertEquals(10, result.destination.y);
        assertEquals(150, result.getDestionationResult());
    }

    @Test public void test_scenario2() throws IOException {
        List<String> plannedRoute = CommonUtils.loadResource("/input.txt", Function.identity());
        Dive.Result result = dive.processPlannedRouteWithAimApproach(plannedRoute);
        assertEquals(15, result.destination.x);
        assertEquals(60, result.destination.y);
        assertEquals(900, result.getDestionationResult());
    }

}