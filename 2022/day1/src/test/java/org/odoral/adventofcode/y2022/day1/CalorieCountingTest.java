package org.odoral.adventofcode.y2022.day1;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class CalorieCountingTest {

    protected CalorieCounting calorieCounting;

    @Before public void setUp() {
        calorieCounting = new CalorieCounting();
    }

    @Test public void test_scenario1() throws IOException {
        List<String> calories = CommonUtils.loadResource("/input.txt", Function.identity());
        CalorieCounting.Result result = calorieCounting.getTopCaloriesElves(calories, 1);
        assertEquals(24000L, result.calories);
    }

    @Test public void test_scenario2() throws IOException {
        List<String> calories = CommonUtils.loadResource("/input.txt", Function.identity());
        CalorieCounting.Result result = calorieCounting.getTopCaloriesElves(calories, 3);
        assertEquals(45000L, result.calories);
    }

}