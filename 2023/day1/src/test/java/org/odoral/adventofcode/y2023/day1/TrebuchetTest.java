package org.odoral.adventofcode.y2023.day1;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class TrebuchetTest {

    protected Trebuchet trebuchet;

    @Before public void setUp() {
        trebuchet = new Trebuchet();
    }

    @Test public void test_scenario1() throws IOException {
        List<String> instructionLines = CommonUtils.loadResource("/input.txt", Function.identity());
        Trebuchet.Result result = trebuchet.getSumOfCalibrationValues(instructionLines);
        assertEquals(4, result.values.size());
        assertEquals(Arrays.asList(12L, 38L, 15L, 77L), result.values);
        assertEquals((Long) 142L, result.sum);
    }

    @Test public void test_scenario2() throws IOException {
        List<String> instructionLines = CommonUtils.loadResource("/input2.txt", Function.identity());
        Trebuchet.Result result = trebuchet.getSumOfCalibrationValues(trebuchet.fromLetterToDigit(instructionLines));
        assertEquals(7, result.values.size());
        assertEquals(Arrays.asList(29L, 83L, 13L, 24L, 42L, 14L, 76L), result.values);
        assertEquals((Long) 281L, result.sum);
    }

}
