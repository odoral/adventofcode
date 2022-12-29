package org.odoral.adventofcode.y2022.day18;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.model.Point3D;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class BoilingBouldersTest {

    protected BoilingBoulders boilingBoulders;

    @Before public void setUp() {
        boilingBoulders = new BoilingBoulders();
    }

    @Test public void test_inputParsing() throws IOException {
        Set<Point3D> cubes = BoilingBoulders.Input.parse();
        assertEquals(13, cubes.size());
    }

    @Test public void test_scenario1() throws IOException {
        int total = boilingBoulders.calculateSurfaceArea();
        assertEquals(64, total);
    }

    @Test public void test_scenario2() throws IOException {
        int total = boilingBoulders.calculateExternalSurfaceArea();
        assertEquals(58, total);
    }

}