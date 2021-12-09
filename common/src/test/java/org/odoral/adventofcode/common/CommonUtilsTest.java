package org.odoral.adventofcode.common;

import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class CommonUtilsTest {

    @Test(expected = Exception.class) public void test_loadResourceException() throws IOException {
        CommonUtils.loadResource("/not_exists.txt", Function.identity());
    }

    @Test public void test_loadResource() throws IOException {
        List<Integer> numbers = CommonUtils.loadResource("/input.txt", Integer::parseInt);
        assertEquals(10, numbers.size());
        for (int i = 0; i < numbers.size(); i++) {
            assertEquals(new Integer(i+1), numbers.get(i));
        }
    }
    
    @Test public void test_toCharacterArray(){
        Character[] characters = CommonUtils.toCharacterArray("abc123ABC123");
        assertEquals(12, characters.length);
        Character[] expected = {'a', 'b', 'c', '1', '2', '3', 'A', 'B', 'C', '1', '2', '3'};
        assertArrayEquals(expected, characters);
    }

    @Test public void test_toIntegerArray(){
        Integer[] integers = CommonUtils.toIntegerArray("123456789");
        assertEquals(9, integers.length);
        Integer[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        assertArrayEquals(expected, integers);
    }
}