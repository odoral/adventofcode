package org.odoral.adventofcode.y2022.day5;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SupplyStacksTest {

    protected SupplyStacks supplyStacks;

    @Before public void setUp() {
        supplyStacks = new SupplyStacks();
    }

    @Test public void test_mapStacks() {
        assertTrue(SupplyStacks.Input.mapStacks(" 1   2   3 ").isEmpty());

        List<Character> characters = SupplyStacks.Input.mapStacks("[Z] [M] [P]");
        assertEquals('Z', characters.get(0).charValue());
        assertEquals('M', characters.get(1).charValue());
        assertEquals('P', characters.get(2).charValue());
    }

    @Test public void test_scenario1() {
        SupplyStacks supplyStacks = new SupplyStacks();
        SupplyStacks.Input input = supplyStacks.loadInput("/input.txt");
        SupplyStacks.Result result = supplyStacks.move(input.stacks, input.movements, supplyStacks.moveUsingCrateMover9000(input.stacks));

        assertEquals("CMZ", result.cratesOnTop);
    }

    @Test public void test_scenario2() {
        SupplyStacks supplyStacks = new SupplyStacks();
        SupplyStacks.Input input = supplyStacks.loadInput("/input.txt");
        SupplyStacks.Result result = supplyStacks.move(input.stacks, input.movements, supplyStacks.moveUsingCrateMover9001(input.stacks));

        assertEquals("MCD", result.cratesOnTop);
    }

}