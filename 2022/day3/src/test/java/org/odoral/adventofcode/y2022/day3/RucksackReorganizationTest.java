package org.odoral.adventofcode.y2022.day3;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class RucksackReorganizationTest {

    protected RucksackReorganization rucksackReorganization;

    @Before public void setUp() {
        rucksackReorganization = new RucksackReorganization();
    }

    @Test public void test_scenario1() throws IOException {
        RucksackReorganization rucksackReorganization = new RucksackReorganization();

        assertEquals(16L, rucksackReorganization.rucksackPriorityPart1("vJrwpWtwJgWrhcsFMMfFFhFp"));
        assertEquals(38L, rucksackReorganization.rucksackPriorityPart1("jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL"));
        assertEquals(42L, rucksackReorganization.rucksackPriorityPart1("PmmdzqPrVvPwwTWBwg"));
        assertEquals(22L, rucksackReorganization.rucksackPriorityPart1("wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn"));
        assertEquals(20L, rucksackReorganization.rucksackPriorityPart1("ttgJtRGJQctTZtZT"));
        assertEquals(19L, rucksackReorganization.rucksackPriorityPart1("CrZsJsPPZsGzwwsLwLmpwMDw"));

        List<String> rucksacks = CommonUtils.loadResource("/input.txt", Function.identity());
        RucksackReorganization.Result result = rucksackReorganization.getTotal(rucksacks, rucksackReorganization::rucksackPriorityPart1, 1);
        assertEquals(157L, result.total);
    }

    @Test public void test_scenario2() throws IOException {
        RucksackReorganization rucksackReorganization = new RucksackReorganization();
        List<String> rucksacks = CommonUtils.loadResource("/input.txt", Function.identity());
        RucksackReorganization.Result result = rucksackReorganization.getTotal(rucksacks, rucksackReorganization::rucksackPriorityPart2, 3);

        assertEquals(70L, result.total);
    }

}