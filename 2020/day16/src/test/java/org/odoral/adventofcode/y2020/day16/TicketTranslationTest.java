package org.odoral.adventofcode.y2020.day16;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.assertEquals;

@Slf4j
public class TicketTranslationTest {

    protected TicketTranslation ticketTranslation;

    @Before public void setUp() {
        ticketTranslation = new TicketTranslation();
    }
    
    @Test public void test_scenario1() throws IOException {
        TicketTranslation.InputData inputData = ticketTranslation.loadInputData("/scenario1.txt");
        log.info("Input data loaded: {}", inputData);
        
        assertEquals(71, ticketTranslation.calculateTicketScanningErrorRate(inputData));
    }
    
    @Test public void test_scenario2() throws IOException {
        TicketTranslation.InputData inputData = ticketTranslation.loadInputData("/scenario2.txt");
        log.info("Input data loaded: {}", inputData);
        
        assertEquals(0, ticketTranslation.calculateTicketScanningErrorRate(inputData));
        
        assertEquals(11, ticketTranslation.getResultByField(inputData, "row"::equals));
        assertEquals(12, ticketTranslation.getResultByField(inputData, "class"::equals));
        assertEquals(13, ticketTranslation.getResultByField(inputData, "seat"::equals));
    }
}