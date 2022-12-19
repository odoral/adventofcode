package org.odoral.adventofcode.y2022.day17;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.assertEquals;

@Slf4j
public class PyroclasticFlowTest {

    protected PyroclasticFlow pyroclasticFlow;

    @Before public void setUp() {
        pyroclasticFlow = new PyroclasticFlow();
    }

    @Test public void test_inputParsing() throws IOException {
        List<PyroclasticFlow.Rock> rocks = PyroclasticFlow.Rock.parse();
        assertEquals(5, rocks.size());

        rocks.forEach(rock -> log.info("\n-------\n{}-------", rock.shape()));

        List<Character> jetPattern = PyroclasticFlow.Input.parse();
        assertEquals(40, jetPattern.size());
    }

    @Test public void test_scenario1() throws IOException {
        PyroclasticFlow pyroclasticFlow = new PyroclasticFlow();
        List<PyroclasticFlow.Rock> rocks = PyroclasticFlow.Rock.parse();
        List<Character> jetPattern = PyroclasticFlow.Input.parse();
        long towerHeight = pyroclasticFlow.getTowerHeight(rocks, jetPattern, 2022);

        assertEquals(3068, towerHeight);
    }

    @Test public void test_scenario2() throws IOException {
        PyroclasticFlow pyroclasticFlow = new PyroclasticFlow();
        List<PyroclasticFlow.Rock> rocks = PyroclasticFlow.Rock.parse();
        List<Character> jetPattern = PyroclasticFlow.Input.parse();
        long towerHeight = pyroclasticFlow.getTowerHeight(rocks, jetPattern, 1000000000000L);

        assertEquals(1514285714288L, towerHeight);
    }

}