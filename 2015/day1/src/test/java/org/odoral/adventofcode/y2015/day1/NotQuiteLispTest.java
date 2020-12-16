package org.odoral.adventofcode.y2015.day1;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class NotQuiteLispTest {

    protected NotQuiteLisp notQuiteLisp;

    @Before public void setUp() {
        notQuiteLisp = new NotQuiteLisp();
    }
    
    @Test public void test() throws IOException {
        List<String> instructions = notQuiteLisp.loadInput("/scenario1.txt");
        assertEquals(1, instructions.size());
        assertEquals("(())", instructions.get(0));
        
        assertEquals(0, notQuiteLisp.calculateTargetFloor(Collections.singletonList("(())"), 0));
        assertEquals(3, notQuiteLisp.calculateTargetFloor(Collections.singletonList("((("), 0));
        assertEquals(-1, notQuiteLisp.calculateTargetFloor(Collections.singletonList("))("), 0));
        
        assertEquals(1, notQuiteLisp.instructionsToReach(Collections.singletonList(")"), 0, -1));
        assertEquals(5, notQuiteLisp.instructionsToReach(Collections.singletonList("()())"), 0, -1));
    }
}