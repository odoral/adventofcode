package org.odoral.adventofcode.y2020.day14;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.*;

@Slf4j
public class DockingDataTest {

    protected DockingData dockingData;

    @Before public void setUp() {
        dockingData = new DockingData();
    }
    
    @Test public void test() throws IOException {
        List<DockingData.Instruction> instructions = dockingData.loadInstructions("/scenario1.txt");
        Map<Long, String> addresses = dockingData.processInstructionsV1(instructions);
        
        assertEquals("000000000000000000000000000001000000", addresses.get(8L));
        assertEquals("000000000000000000000000000001100101", addresses.get(7L));
        assertEquals(165, dockingData.calculateSum(addresses));

        instructions = dockingData.loadInstructions("/scenario2.txt");
        addresses = dockingData.processInstructionsV2(instructions);
        assertEquals(208, dockingData.calculateSum(addresses));
    }
}