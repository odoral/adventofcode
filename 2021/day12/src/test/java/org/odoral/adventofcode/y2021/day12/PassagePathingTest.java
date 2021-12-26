package org.odoral.adventofcode.y2021.day12;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class PassagePathingTest {

    protected PassagePathing passagePathing;

    @Before public void setUp() {
        passagePathing = new PassagePathing();
    }

    @Test public void test_scenario1() throws IOException {
        Map<String, Set<String>> caveMap = PassagePathing.loadCaveMap("/input.txt");
        PassagePathing.Result result = passagePathing.calculatePaths(caveMap, PassagePathing.SKIP_IF_SMALL_AND_ALREADY_IN_PATH);
        assertEquals(10, result.paths.size());
    }

    @Test public void test_scenario2() throws IOException {
        Map<String, Set<String>> caveMap = PassagePathing.loadCaveMap("/input.txt");
        PassagePathing.Result result = passagePathing.calculatePaths(caveMap, PassagePathing.SKIP_IF_SMALL_AND_VISITED_TWICE_IN_PATH);
        assertEquals(36, result.paths.size());
    }

}