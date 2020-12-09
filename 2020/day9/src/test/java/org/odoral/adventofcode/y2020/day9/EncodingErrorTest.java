package org.odoral.adventofcode.y2020.day9;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class EncodingErrorTest {

    protected EncodingError encodingError;

    @Before public void setUp() {
        encodingError = new EncodingError();
    }
    
    @Test public void test_validNumbers() throws IOException {
        List<Long> numbers = encodingError.loadNumbers("/scenario1.txt");
        assertEquals(20, numbers.size());
        int preamble = 5;
        for (int index = preamble; index < numbers.size(); index++) {
            boolean validNumber = encodingError.isValidNumber(numbers, index, preamble);
            if(index == 14){
                assertFalse(validNumber);
            }else{
                assertTrue(validNumber);
            }
        }
    }
    
    @Test public void test_EncryptionWeakness() throws IOException {
        List<Long> numbers = encodingError.loadNumbers("/scenario1.txt");
        long encryptionWeakness = encodingError.getEncryptionWeakness(numbers, 127);
        assertEquals(62, encryptionWeakness);
    }
}