package org.odoral.adventofcode.y2020.day12;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@Slf4j
public class RainRiskTest {

    protected RainRisk rainRisk;

    @Before public void setUp() {
        rainRisk = new RainRisk();
    }
    
    @Test public void test_partOne() throws IOException {
        List<RainRisk.NavigationInstruction> navigationInstructions = rainRisk.loadNavigationInstructions("/scenario1.txt");
        int[] origin = new int[]{0, 0};
        int[] target = rainRisk.moveForPartOne(origin, RainRisk.Facing.E, navigationInstructions);
        assertArrayEquals(new int[]{17, -8}, target);
        assertEquals(25, rainRisk.calculateManhattanDistance(target));
    }
    
    @Test public void test_partTwo() throws IOException {
        List<RainRisk.NavigationInstruction> navigationInstructions = rainRisk.loadNavigationInstructions("/scenario1.txt");
        int[] origin = new int[]{0, 0};
        int[] initialWaypoint = new int[]{10, 1};
        int[] target = rainRisk.moveForPartTwo(origin, initialWaypoint, navigationInstructions);
        assertArrayEquals(new int[]{214, -72}, target);
        assertEquals(286, rainRisk.calculateManhattanDistance(target));
    }
}