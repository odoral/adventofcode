package org.odoral.adventofcode.y2021.day5;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class HydrothermalVentureTest {

    protected HydrothermalVenture hydrothermalVenture;

    @Before public void setUp() {
        hydrothermalVenture = new HydrothermalVenture();
    }

    @Test public void test_scenario1() throws IOException {
        List<HydrothermalVenture.Line> input = CommonUtils.loadResource("/input.txt", hydrothermalVenture::mapLine);
        HydrothermalVenture.Result result = hydrothermalVenture.drawLines(input, HydrothermalVenture.FILTER_HORIZONTAL_AND_VERTICAL_LINES);
        assertEquals(5, result.countOverlaps(2));
    }

    @Test public void test_scenario2() throws IOException {
        List<HydrothermalVenture.Line> input = CommonUtils.loadResource("/input.txt", hydrothermalVenture::mapLine);
        HydrothermalVenture.Result result = hydrothermalVenture.drawLines(input, HydrothermalVenture.NO_FILTER);
        assertEquals(12, result.countOverlaps(2));
    }

}