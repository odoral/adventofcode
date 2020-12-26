package org.odoral.adventofcode.y2020.day23;

import org.junit.Before;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.assertEquals;

@Slf4j
public class CrabCupsTest {

    protected CrabCups crabCups;

    @Before public void setUp() {
        crabCups = new CrabCups();
    }
    
    @Test public void test_firstPart(){
        CrabCups.CupClock cupClock = CrabCups.CupClock.from("389125467", 3L);
        assertEquals(Long.valueOf(3), cupClock.currentCup.label);

        cupClock = crabCups.move(cupClock);
        log.info("Cups after one move: {}", cupClock);
        assertEquals("54673289", cupClock.getCupsAfter(1));
        assertEquals(Long.valueOf(2), cupClock.currentCup.label);

        cupClock = crabCups.move(cupClock, 9);
        log.info("Cups after ten moves: {}", cupClock);
        assertEquals("92658374", cupClock.getCupsAfter(1));
        
        cupClock = crabCups.move(cupClock, 90);
        log.info("Cups after 100 moves: {}", cupClock);
        assertEquals("67384529", cupClock.getCupsAfter(1));
    }
    
    @Test public void test_secondPart(){
        CrabCups.CupClock cupClock = CrabCups.CupClock.fromPartTwo("389125467", 3L);
        cupClock = crabCups.move(cupClock, 10000000);
        log.info("Cups after ten million moves: {}", cupClock);
        assertEquals(Long.valueOf(149245887792L), cupClock.getMultiplicationOfTwoLabelsAfterLabel(1));
    }
}