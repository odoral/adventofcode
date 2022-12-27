package org.odoral.adventofcode.y2022.day15;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.model.KeyValue;
import org.odoral.adventofcode.common.model.Point;
import org.odoral.adventofcode.common.model.ValuedPoint;

import java.io.IOException;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.assertEquals;

@Slf4j
public class BeaconExclusionZoneTest {

    protected BeaconExclusionZone beaconExclusionZone;

    @Before public void setUp() {
        beaconExclusionZone = new BeaconExclusionZone();
    }

    @Test public void test_inputParsing() throws IOException {
        List<KeyValue<ValuedPoint<Integer>, Point>> signalPairs = BeaconExclusionZone.Input.load();
        assertEquals(14, signalPairs.size());
    }

    @Test public void test_scenario1() throws IOException {
        int count = beaconExclusionZone.countPositionsWithoutBeaconsInRow(10);
        assertEquals(26, count);
    }

    @Test public void test_scenario2() throws IOException {
        beaconExclusionZone.drawSensorsAndBeacons();
        long tuningFrequency = beaconExclusionZone.calculateTuningFrequency();
        assertEquals(56_000_011, tuningFrequency);
    }

}