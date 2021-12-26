package org.odoral.adventofcode.y2021.day13;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TransparentOrigamiTest {

    protected TransparentOrigami transparentOrigami;

    @Before public void setUp() {
        transparentOrigami = new TransparentOrigami();
    }

    @Test public void test_scenario1() throws IOException {
        TransparentOrigami.Instructions instructions = TransparentOrigami.loadInstructions("/input.txt");
        assertEquals(18, instructions.points.size());
        assertEquals(2, instructions.folds.size());

        // Only first fold
        instructions.folds.remove(1);

        TransparentOrigami.Result result = transparentOrigami.calculateVisibleDots(instructions);
        assertEquals(17, result.getVisibleDots());
    }

}