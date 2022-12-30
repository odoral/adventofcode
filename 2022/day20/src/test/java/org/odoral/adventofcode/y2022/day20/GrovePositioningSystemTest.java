package org.odoral.adventofcode.y2022.day20;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.model.Chain;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class GrovePositioningSystemTest {

    protected GrovePositioningSystem grovePositioningSystem;

    @Before public void setUp() {
        grovePositioningSystem = new GrovePositioningSystem();
    }

    @Test public void test_inputParsing() throws IOException {
        Chain<Long> firstChain = GrovePositioningSystem.Input.load();
        Chain<Long> currentChain = firstChain.getNext();
        int totalChains = 1;
        while (currentChain != firstChain) {
            currentChain = currentChain.getNext();
            totalChains++;
        }
        assertEquals(7, totalChains);
    }

    @Test public void test_chainToString() throws IOException {
        Chain<Long> chain = GrovePositioningSystem.Input.toChain(Arrays.asList(1L, 2L, 1L));
        assertEquals("1, 2, 1", grovePositioningSystem.chainToString(chain));
    }
        
    @Test public void test_sortChain() throws IOException {
        Chain<Long> chain = GrovePositioningSystem.Input.toChain(Arrays.asList(1L, 2L, 1L));
        grovePositioningSystem.mixChain(grovePositioningSystem.chainToList(chain));
        assertEquals("1, 2, 1", grovePositioningSystem.chainToString(chain));
        
        chain = GrovePositioningSystem.Input.toChain(Arrays.asList(0L, 20L, 0L));
        grovePositioningSystem.mixChain(grovePositioningSystem.chainToList(chain));
        assertEquals("0, 20, 0", grovePositioningSystem.chainToString(chain));

        chain = GrovePositioningSystem.Input.toChain(Arrays.asList(1L, -2L, 1L));
        grovePositioningSystem.mixChain(grovePositioningSystem.chainToList(chain));
        assertEquals("1, -2, 1", grovePositioningSystem.chainToString(chain));
        
        chain = GrovePositioningSystem.Input.toChain(Arrays.asList(0L, -20L, 0L));
        grovePositioningSystem.mixChain(grovePositioningSystem.chainToList(chain));
        assertEquals("0, -20, 0", grovePositioningSystem.chainToString(chain));
        
        chain = GrovePositioningSystem.Input.toChain(Arrays.asList(1L, -2L, -20L));
        grovePositioningSystem.mixChain(grovePositioningSystem.chainToList(chain));
        assertEquals("1, -20, -2", grovePositioningSystem.chainToString(chain));
        
        chain = GrovePositioningSystem.Input.toChain(Arrays.asList(0L, -3L, -20L));
        grovePositioningSystem.mixChain(grovePositioningSystem.chainToList(chain));
        assertEquals("0, -20, -3", grovePositioningSystem.chainToString(chain));
        
        chain = GrovePositioningSystem.Input.toChain(Arrays.asList(1L, -3L, -20L));
        grovePositioningSystem.mixChain(grovePositioningSystem.chainToList(chain));
        assertEquals("1, -3, -20", grovePositioningSystem.chainToString(chain));
    }

    @Test public void test_scenario1() throws IOException {
        long result = grovePositioningSystem.getSumOfTheThreeNumbersThatFormTheGroveCoordinates();
        assertEquals(3, result);
    }

    @Test public void test_scenario2() throws IOException {
        long result = grovePositioningSystem.getSumOfTheThreeNumbersThatFormTheGroveCoordinates(GrovePositioningSystem.DECRYPTION_KEY, GrovePositioningSystem.MIX_COUNT);
        assertEquals(1623178306L, result);
    }

}