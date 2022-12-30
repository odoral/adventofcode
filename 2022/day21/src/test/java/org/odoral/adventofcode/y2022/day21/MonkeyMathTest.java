package org.odoral.adventofcode.y2022.day21;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.y2022.day21.model.MonkeyAction;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MonkeyMathTest {

    protected MonkeyMath monkeyMath;

    @Before public void setUp() {
        monkeyMath = new MonkeyMath();
    }
    
    @Test public void test_load() throws IOException {
        Map<String, MonkeyAction> monkeyActions = MonkeyMath.Input.load();
        assertEquals(15, monkeyActions.size());
    }
    @Test public void test_scenario1() throws IOException {
        long number = monkeyMath.whatNumberWillYell(MonkeyMath.ROOT_MONKEY_NAME);
        assertEquals(152, number);
    }

    @Test public void test_scenario2() throws IOException {
        long number = monkeyMath.whatNumberDoIHaveToYell(1000);
        assertEquals(301, number);
    }

}