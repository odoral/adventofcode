package org.odoral.adventofcode.y2023.day3;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.model.ValuedPoint;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GearRatiosTest {

    protected GearRatios gearRatios;

    @Before public void setUp() {
        gearRatios = new GearRatios();
    }

    @Test public void test_scenario1() throws IOException {
        ValuedPoint<Character>[][] engineSchematic = gearRatios.loadInputMap("/input.txt");
        GearRatios.Result result = gearRatios.getNumberAdjacentToSymbolSum(engineSchematic);
        assertEquals(8, result.validParts.size());
        assertEquals((Long) 4361L, result.sum);
    }
}
