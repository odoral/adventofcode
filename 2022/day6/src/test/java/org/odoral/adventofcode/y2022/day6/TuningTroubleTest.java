package org.odoral.adventofcode.y2022.day6;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TuningTroubleTest {

    protected TuningTrouble tuningTrouble;

    @Before public void setUp() {
        tuningTrouble = new TuningTrouble();
    }

    @Test public void test_scenario1() {
        TuningTrouble tuningTrouble = new TuningTrouble();

        assertEquals(5, tuningTrouble.findPacketMarker("bvwbjplbgvbhsrlpgdmjqwftvncz", TuningTrouble.START_OF_PACKET_MARKER_SIZE));
        assertEquals(6, tuningTrouble.findPacketMarker("nppdvjthqldpwncqszvftbrmjlhg", TuningTrouble.START_OF_PACKET_MARKER_SIZE));
        assertEquals(10, tuningTrouble.findPacketMarker("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", TuningTrouble.START_OF_PACKET_MARKER_SIZE));
        assertEquals(11, tuningTrouble.findPacketMarker("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", TuningTrouble.START_OF_PACKET_MARKER_SIZE));
    }

    @Test public void test_scenario2() {
        TuningTrouble tuningTrouble = new TuningTrouble();

        assertEquals(19, tuningTrouble.findPacketMarker("mjqjpqmgbljsphdztnvjfqwrcgsmlb", TuningTrouble.START_OF_MESSAGE_MARKER_SIZE));
        assertEquals(23, tuningTrouble.findPacketMarker("bvwbjplbgvbhsrlpgdmjqwftvncz", TuningTrouble.START_OF_MESSAGE_MARKER_SIZE));
        assertEquals(23, tuningTrouble.findPacketMarker("nppdvjthqldpwncqszvftbrmjlhg", TuningTrouble.START_OF_MESSAGE_MARKER_SIZE));
        assertEquals(29, tuningTrouble.findPacketMarker("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", TuningTrouble.START_OF_MESSAGE_MARKER_SIZE));
        assertEquals(26, tuningTrouble.findPacketMarker("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", TuningTrouble.START_OF_MESSAGE_MARKER_SIZE));
    }

}