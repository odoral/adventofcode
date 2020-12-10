package org.odoral.adventofcode.y2020.day10;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.assertEquals;

@Slf4j
public class AdapterArrayTest {

    protected AdapterArray adapterArray;

    @Before public void setUp() {
        adapterArray = new AdapterArray();
    }
    
    @Test public void test_joltsScenario1() throws IOException {
        List<Integer> adapters = adapterArray.loadAdaptersData("/scenario1.txt");

        Map<Integer, Integer> joltDiffCounter = adapterArray.countJoltDiffs(adapters, 0, 3);
        
        assertEquals(new Integer(7), joltDiffCounter.get(1));
        assertEquals(new Integer(5), joltDiffCounter.get(3));

        long solution = adapterArray.calculateFirstSolution(joltDiffCounter);
        assertEquals(35, solution);
        
        log.info("Solution for scenario1 is: {}", solution);
    }
    
    @Test public void test_joltsScenario2() throws IOException {
        List<Integer> adapters = adapterArray.loadAdaptersData("/scenario2.txt");

        Map<Integer, Integer> joltDiffCounter = adapterArray.countJoltDiffs(adapters, 0, 3);
        
        assertEquals(new Integer(22), joltDiffCounter.get(1));
        assertEquals(new Integer(10), joltDiffCounter.get(3));

        long solution = adapterArray.calculateFirstSolution(joltDiffCounter);
        assertEquals(220, solution);
        
        log.info("Solution for scenario2 is: {}", solution);
    }
    
    @Test public void test_arrangementsScenario1() throws IOException {
        List<Integer> adapters = adapterArray.loadAdaptersData("/scenario1.txt");

        long arrangements = adapterArray.calculateArrangements(adapters, 0, 3);

        assertEquals(8, arrangements);
    }
    
    @Test public void test_arrangementsScenario2() throws IOException {
        List<Integer> adapters = adapterArray.loadAdaptersData("/scenario2.txt");

        long arrangements = adapterArray.calculateArrangements(adapters, 0, 3);

        assertEquals(19208, arrangements);
    }
}