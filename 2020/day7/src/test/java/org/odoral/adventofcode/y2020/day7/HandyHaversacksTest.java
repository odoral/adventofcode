package org.odoral.adventofcode.y2020.day7;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class HandyHaversacksTest {
    
    protected HandyHaversacks handyHaversacks;

    @Before public void setUp() {
        handyHaversacks = new HandyHaversacks();
    }
    
    @Test public void test_howManyContains() throws IOException {
        Map<String, HandyHaversacks.Bag> bagMap = handyHaversacks.loadBagConfiguration("/scenario1.txt");
        assertEquals(9, bagMap.size());
        long count = handyHaversacks.countHowManyContains(bagMap, HandyHaversacks.BAG_ID_SHINY_GOLD, HandyHaversacks.MIN_AMOUNT_TO_SEARCH);
        assertEquals(4, count);
    }
    
    @Test public void test_totalBags() throws IOException {
        Map<String, HandyHaversacks.Bag> bagMap = handyHaversacks.loadBagConfiguration("/scenario2.txt");
        assertEquals(7, bagMap.size());
        long count = handyHaversacks.totalBags(bagMap.get(HandyHaversacks.BAG_ID_SHINY_GOLD), bagMap);
        assertEquals(126, count);
    }
}