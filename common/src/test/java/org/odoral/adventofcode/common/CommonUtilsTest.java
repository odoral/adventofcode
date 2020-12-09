package org.odoral.adventofcode.common;

import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.*;

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
}