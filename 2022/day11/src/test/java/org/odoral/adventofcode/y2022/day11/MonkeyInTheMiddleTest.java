package org.odoral.adventofcode.y2022.day11;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MonkeyInTheMiddleTest {

    protected MonkeyInTheMiddle monkeyInTheMiddle;

    @Before public void setUp() {
        monkeyInTheMiddle = new MonkeyInTheMiddle();
    }

    @Test public void test_scenario1() throws IOException {
        MonkeyInTheMiddle monkeyInTheMiddle = new MonkeyInTheMiddle();
        MonkeyInTheMiddle.Input input = MonkeyInTheMiddle.Input.loadInput("/input.txt");
        MonkeyInTheMiddle.Result result = monkeyInTheMiddle.doRounds(input, 20, 3);
        assertEquals(10_605, result.getMonkeyBusinessLevel());
    }

    @Test public void test_scenario2() throws IOException {
        MonkeyInTheMiddle monkeyInTheMiddle = new MonkeyInTheMiddle();
        MonkeyInTheMiddle.Input input = MonkeyInTheMiddle.Input.loadInput("/input.txt");
        MonkeyInTheMiddle.Result result = monkeyInTheMiddle.doRounds(input, 10_000, 1);
        assertEquals(2_713_310_158L, result.getMonkeyBusinessLevel());
    }

}