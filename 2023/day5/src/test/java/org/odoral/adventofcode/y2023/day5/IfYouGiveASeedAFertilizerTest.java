package org.odoral.adventofcode.y2023.day5;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class IfYouGiveASeedAFertilizerTest {

    protected IfYouGiveASeedAFertilizer ifYouGiveASeedAFertilizer;

    @Before public void setUp() {
        ifYouGiveASeedAFertilizer = new IfYouGiveASeedAFertilizer();
    }

    @Test public void test_scenario1() throws IOException {
        IfYouGiveASeedAFertilizer.Input input = ifYouGiveASeedAFertilizer.getLoadResource();
        IfYouGiveASeedAFertilizer.Result result = ifYouGiveASeedAFertilizer.calculateLocations(input);
        assertEquals((Long) 35L, result.lowestLocation);
    }

    @Test public void test_scenario2() throws IOException {
        IfYouGiveASeedAFertilizer.Input input = ifYouGiveASeedAFertilizer.getLoadResource();
        IfYouGiveASeedAFertilizer.Result result = ifYouGiveASeedAFertilizer.calculateLocationsForRanges(input);
        assertEquals((Long) 46L, result.lowestLocation);
    }
}
