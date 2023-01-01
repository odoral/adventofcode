package org.odoral.adventofcode.common.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class PointTest {

    public static final Point ZERO_AXIS = new Point(0, 0);
    public static final Point ONE_ONE_AXIS = new Point(1, 1);

    @Test public void test_ROTATE_HALF_PI() {
        assertEquals(new Point(0, 1), Point.ROTATE_HALF_PI.apply(new Point(1, 0), ZERO_AXIS));
        assertEquals(new Point(-1, 1), Point.ROTATE_HALF_PI.apply(new Point(1, 1), ZERO_AXIS));
        assertEquals(new Point(-1, 0), Point.ROTATE_HALF_PI.apply(new Point(0, 1), ZERO_AXIS));
        assertEquals(new Point(-1, -1), Point.ROTATE_HALF_PI.apply(new Point(-1, 1), ZERO_AXIS));
        assertEquals(new Point(0, -1), Point.ROTATE_HALF_PI.apply(new Point(-1, 0), ZERO_AXIS));
        assertEquals(new Point(1, -1), Point.ROTATE_HALF_PI.apply(new Point(-1, -1), ZERO_AXIS));

        assertEquals(new Point(0, 2), Point.ROTATE_HALF_PI.apply(new Point(2, 2), ONE_ONE_AXIS));
        assertEquals(new Point(2, 2), Point.ROTATE_HALF_PI.apply(new Point(2, 0), ONE_ONE_AXIS));
        assertEquals(new Point(2, 3), Point.ROTATE_HALF_PI.apply(new Point(3, 0), ONE_ONE_AXIS));
        
        assertEquals(new Point(7, 8), Point.ROTATE_HALF_PI.apply(new Point(12, 5), new Point(8, 4)));
    }

    @Test public void test_ROTATE_PI() {
        assertEquals(new Point(-1, 0), Point.ROTATE_PI.apply(new Point(1, 0), ZERO_AXIS));
        assertEquals(new Point(-1, -1), Point.ROTATE_PI.apply(new Point(1, 1), ZERO_AXIS));
        assertEquals(new Point(0, -1), Point.ROTATE_PI.apply(new Point(0, 1), ZERO_AXIS));
        assertEquals(new Point(1, -1), Point.ROTATE_PI.apply(new Point(-1, 1), ZERO_AXIS));
        assertEquals(new Point(1, 0), Point.ROTATE_PI.apply(new Point(-1, 0), ZERO_AXIS));
        assertEquals(new Point(1, 1), Point.ROTATE_PI.apply(new Point(-1, -1), ZERO_AXIS));

        assertEquals(new Point(0, 0), Point.ROTATE_PI.apply(new Point(2, 2), ONE_ONE_AXIS));
        assertEquals(new Point(0, 2), Point.ROTATE_PI.apply(new Point(2, 0), ONE_ONE_AXIS));
        assertEquals(new Point(-1, 2), Point.ROTATE_PI.apply(new Point(3, 0), ONE_ONE_AXIS));
    }
}