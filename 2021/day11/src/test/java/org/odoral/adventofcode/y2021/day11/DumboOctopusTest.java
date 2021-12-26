package org.odoral.adventofcode.y2021.day11;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class DumboOctopusTest {

    protected DumboOctopus dumboOctopus;

    @Before public void setUp() {
        dumboOctopus = new DumboOctopus();
    }

    @Test public void test_scenario1() throws IOException {
        Integer[][] inputMap = DumboOctopus.loadInputMap("/input.txt");
        DumboOctopus.Result result = dumboOctopus.processSteps(inputMap, 1);
        assertArrayEquals(DumboOctopus.loadInputMap("/expected_after_1_step.txt"), result.inputMap);
        result = dumboOctopus.processSteps(result.inputMap, 1);
        assertArrayEquals(DumboOctopus.loadInputMap("/expected_after_2_step.txt"), result.inputMap);

        inputMap = DumboOctopus.loadInputMap("/input.txt");
        result = dumboOctopus.processSteps(inputMap, 100);
        assertArrayEquals(DumboOctopus.loadInputMap("/expected_after_100_steps.txt"), result.inputMap);
        assertEquals(1656, result.numberOfFlashes);
    }

    @Test public void test_scenario2() throws IOException {
        Integer[][] inputMap = DumboOctopus.loadInputMap("/input.txt");
        DumboOctopus.Result result = dumboOctopus.findStepAllOctopusesFlash(inputMap);
        assertArrayEquals(DumboOctopus.loadInputMap("/expected_all_flashes.txt"), result.inputMap);
        assertEquals(195, result.steps);
    }

}