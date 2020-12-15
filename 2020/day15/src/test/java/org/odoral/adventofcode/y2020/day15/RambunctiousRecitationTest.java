package org.odoral.adventofcode.y2020.day15;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RambunctiousRecitationTest {

    protected RambunctiousRecitation rambunctiousRecitation;

    @Before public void setUp() {
        rambunctiousRecitation = new RambunctiousRecitation();
    }
    
    @Test public void test(){
        assertEquals(0L, rambunctiousRecitation.play(rambunctiousRecitation.setStartingNumbers(0, 3, 6), 4L, 6L));
        assertEquals(3L, rambunctiousRecitation.play(rambunctiousRecitation.setStartingNumbers(0, 3, 6), 5L, 6L));
        assertEquals(3L, rambunctiousRecitation.play(rambunctiousRecitation.setStartingNumbers(0, 3, 6), 6L, 6L));
        assertEquals(1L, rambunctiousRecitation.play(rambunctiousRecitation.setStartingNumbers(0, 3, 6), 7L, 6L));
        assertEquals(0L, rambunctiousRecitation.play(rambunctiousRecitation.setStartingNumbers(0, 3, 6), 8L, 6L));
        assertEquals(4L, rambunctiousRecitation.play(rambunctiousRecitation.setStartingNumbers(0, 3, 6), 9L, 6L));
        assertEquals(0L, rambunctiousRecitation.play(rambunctiousRecitation.setStartingNumbers(0, 3, 6), 10L, 6L));

        assertEquals(436L, rambunctiousRecitation.play(rambunctiousRecitation.setStartingNumbers(0, 3, 6), 2020L, 6L));
        
        assertEquals(1L, rambunctiousRecitation.play(rambunctiousRecitation.setStartingNumbers(1, 3, 2), 2020L, 2L));
        assertEquals(10L, rambunctiousRecitation.play(rambunctiousRecitation.setStartingNumbers(2, 1, 3), 2020L, 3L));
        assertEquals(27L, rambunctiousRecitation.play(rambunctiousRecitation.setStartingNumbers(1, 2, 3), 2020L, 3L));
        assertEquals(78L, rambunctiousRecitation.play(rambunctiousRecitation.setStartingNumbers(2, 3, 1), 2020L, 1L));
        assertEquals(438L, rambunctiousRecitation.play(rambunctiousRecitation.setStartingNumbers(3, 2, 1), 2020L, 1L));
        assertEquals(1836L, rambunctiousRecitation.play(rambunctiousRecitation.setStartingNumbers(3, 1, 2), 2020L, 2L));
        
        //Given 0,3,6, the 30000000th number spoken is 175594.
        assertEquals(175594L, rambunctiousRecitation.play(rambunctiousRecitation.setStartingNumbers(0, 3, 6), 30000000L, 6L));
        //Given 1,3,2, the 30000000th number spoken is 2578.
        assertEquals(2578L, rambunctiousRecitation.play(rambunctiousRecitation.setStartingNumbers(1, 3, 2), 30000000L, 2L));
        //Given 2,1,3, the 30000000th number spoken is 3544142.
        assertEquals(3544142L, rambunctiousRecitation.play(rambunctiousRecitation.setStartingNumbers(2, 1, 3), 30000000L, 3L));
        //Given 1,2,3, the 30000000th number spoken is 261214.
        assertEquals(261214L, rambunctiousRecitation.play(rambunctiousRecitation.setStartingNumbers(1, 2, 3), 30000000L, 3L));
        //Given 2,3,1, the 30000000th number spoken is 6895259.
        assertEquals(6895259L, rambunctiousRecitation.play(rambunctiousRecitation.setStartingNumbers(2, 3, 1), 30000000L, 1L));
        //Given 3,2,1, the 30000000th number spoken is 18.
        assertEquals(18L, rambunctiousRecitation.play(rambunctiousRecitation.setStartingNumbers(3, 2, 1), 30000000L, 1L));
        //Given 3,1,2, the 30000000th number spoken is 362.
        assertEquals(362L, rambunctiousRecitation.play(rambunctiousRecitation.setStartingNumbers(3, 1, 2), 30000000L, 2L));
    }
}