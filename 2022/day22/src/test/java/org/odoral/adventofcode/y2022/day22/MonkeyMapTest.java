package org.odoral.adventofcode.y2022.day22;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MonkeyMapTest {

    protected MonkeyMap monkeyMap;

    @Before public void setUp() {
        monkeyMap = new MonkeyMap();
    }

    @Test public void test_load() throws IOException {
        MonkeyMap.Input input = MonkeyMap.Input.load();
        assertEquals(13, input.orders.size());
        assertEquals(4, input.sideLength);
        assertEquals(6, input.rotationPointPerCubeFace.size());
    }
    
    @Test public void test_scenario1() throws IOException {
        int finalPassword = monkeyMap.calculateFinalPassword(MonkeyMap.getNextStatePart1());
        assertEquals(6032, finalPassword);
    }

    @Test public void test_scenario2() throws IOException {
//        int finalPassword = monkeyMap.calculateFinalPassword(MonkeyMap.getNextStatePart2());
//        assertEquals(5031, finalPassword);
    }

}