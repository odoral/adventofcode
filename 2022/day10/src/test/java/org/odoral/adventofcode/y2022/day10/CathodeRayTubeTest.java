package org.odoral.adventofcode.y2022.day10;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class CathodeRayTubeTest {

    public static final String EXPECTED_DRAW_RESULT =
        "##..##..##..##..##..##..##..##..##..##..\n" +
        "###...###...###...###...###...###...###.\n" +
        "####....####....####....####....####....\n" +
        "#####.....#####.....#####.....#####.....\n" +
        "######......######......######......####\n" +
        "#######.......#######.......#######.....";
    protected CathodeRayTube cathodeRayTube;

    @Before public void setUp() {
        cathodeRayTube = new CathodeRayTube();
    }

    @Test public void test_scenario1() throws IOException {
        CathodeRayTube cathodeRayTube = new CathodeRayTube();
        List<String> instructions = CommonUtils.loadResource("/input.txt", Function.identity());

        int total = cathodeRayTube.getSumSignalStrengths(instructions, 6);
        assertEquals(13140, total);
    }

    @Test public void test_scenario2() throws IOException {
        CathodeRayTube cathodeRayTube = new CathodeRayTube();
        List<String> instructions = CommonUtils.loadResource("/input.txt", Function.identity());

        String drawResult = cathodeRayTube.drawInstructions(instructions);
        assertEquals(EXPECTED_DRAW_RESULT, drawResult);
    }

}