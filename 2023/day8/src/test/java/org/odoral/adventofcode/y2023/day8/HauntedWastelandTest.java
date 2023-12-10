package org.odoral.adventofcode.y2023.day8;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class HauntedWastelandTest {

    protected HauntedWasteland hauntedWasteland;

    @Before public void setUp() {
        hauntedWasteland = new HauntedWasteland();
    }

    @Test public void test_scenario1() throws IOException {
        HauntedWasteland hauntedWasteland = new HauntedWasteland();
        HauntedWasteland.Input input = HauntedWasteland.Input.load("/input.txt");
        HauntedWasteland.Result result = hauntedWasteland.getStepsToReachZZZ("AAA", input, "ZZZ");
        assertEquals(Arrays.asList("AAA", "CCC", "ZZZ"), result.path);
        assertEquals(2L, result.steps);

        input = HauntedWasteland.Input.load("/input1.txt");
        result = hauntedWasteland.getStepsToReachZZZ("AAA", input, "ZZZ");
        assertEquals(Arrays.asList("AAA", "BBB", "AAA", "BBB", "AAA", "BBB", "ZZZ"), result.path);
        assertEquals(6L, result.steps);
    }

    @Test public void test_scenario2() throws IOException {
        HauntedWasteland hauntedWasteland = new HauntedWasteland();
        HauntedWasteland.Input input = HauntedWasteland.Input.load("/input2.txt");
        HauntedWasteland.Result result = hauntedWasteland.getStepsToReachNodesEndingOnZ(input);
        assertEquals(6L, result.steps);
    }

}
