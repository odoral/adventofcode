package org.odoral.adventofcode.y2021.day9;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class SmokeBasinTest {

    protected SmokeBasin smokeBasin;

    @Before public void setUp() {
        smokeBasin = new SmokeBasin();
    }

    @Test public void test_scenario1() throws IOException {
        Integer [][] inputMap = CommonUtils.loadResource("/input.txt", CommonUtils::toIntegerArray)
            .toArray(new Integer[0][]);
        SmokeBasin.ResultStep1 resultStep1 = smokeBasin.findLowerPoints(inputMap);
        assertEquals(4, resultStep1.lowerPoints.size());
        assertEquals(15, resultStep1.sumOfRiskLevels());
    }

    @Test public void test_scenario2() throws IOException {
        Integer [][] inputMap = CommonUtils.loadResource("/input.txt", CommonUtils::toIntegerArray)
            .toArray(new Integer[0][]);
        SmokeBasin.ResultStep2 resultStep2 = smokeBasin.findBasins(inputMap);
        assertEquals(4, resultStep2.basins.size());
        assertEquals(1134, resultStep2.multiplyOfThreeLargerBasins());
    }

}