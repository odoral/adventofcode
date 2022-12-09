package org.odoral.adventofcode.y2022.day9;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.model.Point;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class RopeBridgeTest {

    protected RopeBridge ropeBridge;

    @Before public void setUp() {
        ropeBridge = new RopeBridge();
    }

    @Test public void test_scenario1() throws IOException {
        RopeBridge ropeBridge = new RopeBridge();
        List<String> instructions = CommonUtils.loadResource("/input_1.txt", Function.identity());
        Set<Point> positions = ropeBridge.calculateVisitedPositions(instructions, 2);
        assertEquals(13, positions.size());
    }

    @Test public void test_scenario2_1() throws IOException {
        RopeBridge ropeBridge = new RopeBridge();
        List<String> instructions = CommonUtils.loadResource("/input_1.txt", Function.identity());
        Set<Point> positions = ropeBridge.calculateVisitedPositions(instructions, 10);
        assertEquals(1, positions.size());
    }

    @Test public void test_scenario2_2() throws IOException {
        RopeBridge ropeBridge = new RopeBridge();
        List<String> instructions = CommonUtils.loadResource("/input_2.txt", Function.identity());
        Set<Point> positions = ropeBridge.calculateVisitedPositions(instructions, 10);
        assertEquals(36, positions.size());
    }

}