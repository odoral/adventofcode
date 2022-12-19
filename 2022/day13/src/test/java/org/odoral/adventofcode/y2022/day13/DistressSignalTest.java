package org.odoral.adventofcode.y2022.day13;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.model.KeyValue;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DistressSignalTest {

    protected DistressSignal distressSignal;

    @Before public void setUp() {
        distressSignal = new DistressSignal();
    }

    @Test public void test_inputParsing() throws IOException {
        List<KeyValue<List, List>> signalPairs = DistressSignal.Input.loadSignals("/input.txt");
        assertEquals(8, signalPairs.size());
    }

    @Test public void test_scenario1() throws IOException {
        DistressSignal distressSignal = new DistressSignal();
        List<KeyValue<List, List>> signalPairs = DistressSignal.Input.loadSignals("/input.txt");

        int total = distressSignal.sumIndicesWithRightOrder(signalPairs);
        assertEquals(13, total);
    }

    @Test public void test_scenario2() throws IOException {
        DistressSignal distressSignal = new DistressSignal();
        List<KeyValue<List, List>> signalPairs = DistressSignal.Input.loadSignals("/input.txt");

        int total = distressSignal.getDecoderKey(signalPairs);
        assertEquals(140, total);
    }

}