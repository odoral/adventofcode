package org.odoral.adventofcode.y2020.day11;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SeatingSystemTest {

    protected SeatingSystem seatingSystem;

    @Before public void setUp() {
        seatingSystem = new SeatingSystem();
    }
    
    @Test public void test_scenario1() throws IOException {
        Character[][] seatLayout = seatingSystem.loadSeatLayout("/scenario1.txt");

        Character[][] seatLayoutAfterIteration = testIteration(seatLayout, "/scenario1_iteration1.txt", true, 1, 4);
        seatLayoutAfterIteration = testIteration(seatLayoutAfterIteration, "/scenario1_iteration2.txt", true, 1, 4);
        seatLayoutAfterIteration = testIteration(seatLayoutAfterIteration, "/scenario1_iteration3.txt", true, 1, 4);
        seatLayoutAfterIteration = testIteration(seatLayoutAfterIteration, "/scenario1_iteration4.txt", true, 1, 4);
        seatLayoutAfterIteration = testIteration(seatLayoutAfterIteration, "/scenario1_iteration5.txt", true, 1, 4);
        seatLayoutAfterIteration = testIteration(seatLayoutAfterIteration, "/scenario1_iteration5.txt", false, 1, 4);
        long occupiedSeats = seatingSystem.countOccupiedSeats(seatLayoutAfterIteration);

        assertEquals(37, occupiedSeats);
    }

    @Test public void test_scenario2() throws IOException {
        Character[][] seatLayout = seatingSystem.loadSeatLayout("/scenario2.txt");

        int adjacentOccupied = seatingSystem.getAdjacentOccupied(seatLayout, 4, 3, 8);
        assertEquals(8, adjacentOccupied);
    }

    @Test public void test_scenario3() throws IOException {
        Character[][] seatLayout = seatingSystem.loadSeatLayout("/scenario3.txt");

        int adjacentOccupied = seatingSystem.getAdjacentOccupied(seatLayout, 1, 1, 8);
        assertEquals(0, adjacentOccupied);
    }

    @Test public void test_scenario4() throws IOException {
        Character[][] seatLayout = seatingSystem.loadSeatLayout("/scenario4.txt");

        int adjacentOccupied = seatingSystem.getAdjacentOccupied(seatLayout, 3, 3, 8);
        assertEquals(0, adjacentOccupied);
    }

    @Test public void test_scenario5() throws IOException {
        Character[][] seatLayout = seatingSystem.loadSeatLayout("/scenario5.txt");

        Character[][] seatLayoutAfterIteration = testIteration(seatLayout, "/scenario5_iteration1.txt", true, Integer.MAX_VALUE, 5);
        seatLayoutAfterIteration = testIteration(seatLayoutAfterIteration, "/scenario5_iteration2.txt", true, Integer.MAX_VALUE, 5);
        seatLayoutAfterIteration = testIteration(seatLayoutAfterIteration, "/scenario5_iteration3.txt", true, Integer.MAX_VALUE, 5);
        seatLayoutAfterIteration = testIteration(seatLayoutAfterIteration, "/scenario5_iteration4.txt", true, Integer.MAX_VALUE, 5);
        seatLayoutAfterIteration = testIteration(seatLayoutAfterIteration, "/scenario5_iteration5.txt", true, Integer.MAX_VALUE, 5);
        seatLayoutAfterIteration = testIteration(seatLayoutAfterIteration, "/scenario5_iteration6.txt", true, Integer.MAX_VALUE, 5);
        seatLayoutAfterIteration = testIteration(seatLayoutAfterIteration, "/scenario5_iteration6.txt", false, Integer.MAX_VALUE, 5);
        long occupiedSeats = seatingSystem.countOccupiedSeats(seatLayoutAfterIteration);

        assertEquals(26, occupiedSeats);
    }

    protected Character[][] testIteration(Character[][] seatLayout, String expectedSeatLayoutResource, boolean isChangesExcepted, int adjacentSeats, int occupiedSeatsThreshold) throws IOException {
        SeatingSystem.IterationResult iterationResult = seatingSystem.iterateRound(seatLayout, adjacentSeats, occupiedSeatsThreshold);
        Character[][] expectedSeatLayoutAfterIteration = seatingSystem.loadSeatLayout(expectedSeatLayoutResource);
        assertArrayEquals(expectedSeatLayoutAfterIteration, iterationResult.seatLayout);
        assertEquals(isChangesExcepted, iterationResult.hasChanged);
        return iterationResult.seatLayout;
    }
}