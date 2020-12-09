package org.odoral.adventofcode.y2020.day8;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HandheldHaltingTest {

    public static final String SCENARIO_1_TXT = "/scenario1.txt";
    protected HandheldHalting handheldHalting;

    @Before public void setUp() {
        handheldHalting = new HandheldHalting();
    }
    
    @Test public void test_accumulatorBeforeInfiniteLoop() throws IOException {
        List<HandheldHalting.Instruction> instructions = handheldHalting.loadInstructions(this.getClass().getResourceAsStream(SCENARIO_1_TXT));
        assertEquals(9, instructions.size());
        HandheldHalting.Result result = handheldHalting.calculateAccumulatorBeforeLoop(instructions);
        assertEquals(5, result.accumulator);
        assertTrue(result.infiniteLoop);
    }
    
    @Test public void test_fixForInfiniteLoop() throws IOException {
        HandheldHalting.Result result = handheldHalting.fixInfiniteLoop(SCENARIO_1_TXT);
        assertEquals(8, result.accumulator);
        assertFalse(result.infiniteLoop);
    }
}