package org.odoral.adventofcode.y2022.day12;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.model.ValuedPoint;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class HillClimbingAlgorithmTest {

    protected HillClimbingAlgorithm hillClimbingAlgorithm;

    @Before public void setUp() {
        hillClimbingAlgorithm = new HillClimbingAlgorithm();
    }

    @Test public void test_scenario1() throws IOException {
        HillClimbingAlgorithm hillClimbingAlgorithm = new HillClimbingAlgorithm();
        ValuedPoint<HillClimbingAlgorithm.Node>[][] heightMap = HillClimbingAlgorithm.Input.loadHeightMap("/input.txt");
        int steps = hillClimbingAlgorithm.lookForMinimumPath(heightMap, hillClimbingAlgorithm.findSpecialPoint(heightMap, HillClimbingAlgorithm.STARTING_POINT).get(0));
        assertEquals(31, steps);
    }

    @Test public void test_scenario2() throws IOException {
        HillClimbingAlgorithm hillClimbingAlgorithm = new HillClimbingAlgorithm();
        ValuedPoint<HillClimbingAlgorithm.Node>[][] heightMap = HillClimbingAlgorithm.Input.loadHeightMap("/input.txt");
        int steps = hillClimbingAlgorithm.lookForMinimumPath(heightMap, hillClimbingAlgorithm.findSpecialPoint(heightMap, 'a'));
        assertEquals(29, steps);
    }

}