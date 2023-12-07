package org.odoral.adventofcode.y2023.day3;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.model.KeyValue;
import org.odoral.adventofcode.common.model.ValuedPoint;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GearRatiosTest {

    protected GearRatios gearRatios;

    @Before public void setUp() {
        gearRatios = new GearRatios();
    }

    @Test public void test_scenario1() throws IOException {
        ValuedPoint<KeyValue<Character, List<Long>>>[][] engineSchematic = gearRatios.loadInputMap("/input.txt");
        GearRatios.Result result = gearRatios.getNumberAdjacentToSymbolSum(engineSchematic);
        assertEquals(8, result.validParts.size());
        assertEquals((Long) 4361L, result.sum);
    }

    @Test public void test_scenario2() throws IOException {
        ValuedPoint<KeyValue<Character, List<Long>>>[][] engineSchematic = gearRatios.loadInputMap("/input.txt");
        GearRatios.Result result = gearRatios.getNumberAdjacentToSymbolSum(engineSchematic);
        assertEquals(8, result.validParts.size());
        assertEquals((Long) 467835L, result.gearRatiosSum);
    }
}
