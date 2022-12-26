package org.odoral.adventofcode.y2022.day14;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.model.Point;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RegolithReservoirTest {

    protected RegolithReservoir regolithReservoir;

    @Before public void setUp() {
        regolithReservoir = new RegolithReservoir();
    }

    @Test public void test_loadInput() throws IOException {
        Map<Point, Character> world = RegolithReservoir.Input.loadWorld();
        RegolithReservoir.Input.printWorld(world);
    }

    @Test public void test_scenario1() throws IOException {
        Map<Point, Character> world = RegolithReservoir.Input.loadWorld();
        int count = regolithReservoir.countHowManyUnitsOfSandComeToRest(world, regolithReservoir.getValidDisplacementPart1(world), RegolithReservoir.getAbyssLevel(world));

        assertEquals(24, count);
    }

    @Test public void test_scenario2() throws IOException {
        Map<Point, Character> world = RegolithReservoir.Input.loadWorld();
        int count = regolithReservoir.countHowManyUnitsOfSandComeToRest(world, regolithReservoir.getValidDisplacementPart2(world), RegolithReservoir.getFloorLevel(world));

        assertEquals(93, count);
    }

}