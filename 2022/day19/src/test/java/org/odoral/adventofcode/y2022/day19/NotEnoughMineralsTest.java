package org.odoral.adventofcode.y2022.day19;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.assertEquals;

@Slf4j
public class NotEnoughMineralsTest {

    protected NotEnoughMinerals notEnoughMinerals;

    @Before public void setUp() {
        notEnoughMinerals = new NotEnoughMinerals();
    }

    @Test public void test_inputParsing() throws IOException {
        List<NotEnoughMinerals.BluePrint> bluePrints = NotEnoughMinerals.BluePrint.parse();
        assertEquals(2, bluePrints.size());
    }

    @Test public void test_scenario1() throws IOException {
        NotEnoughMinerals notEnoughMinerals = new NotEnoughMinerals();
        List<NotEnoughMinerals.BluePrint> bluePrints = NotEnoughMinerals.BluePrint.parse();

        assertEquals(9, notEnoughMinerals.extractGeodes(bluePrints.get(0), 24));
        assertEquals(12, notEnoughMinerals.extractGeodes(bluePrints.get(1), 24));
        assertEquals(33, notEnoughMinerals.calculateQualityLevel(bluePrints, 24));
    }

    @Test public void test_scenario2() throws IOException {
        NotEnoughMinerals notEnoughMinerals = new NotEnoughMinerals();
        List<NotEnoughMinerals.BluePrint> bluePrints = NotEnoughMinerals.BluePrint.parse();

        assertEquals(56, notEnoughMinerals.extractGeodes(bluePrints.get(0), 32));
        assertEquals(62, notEnoughMinerals.extractGeodes(bluePrints.get(1), 32));
    }

}