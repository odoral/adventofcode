package org.odoral.adventofcode.y2023.day4;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ScratchcardsTest {

    protected Scratchcards scratchcards;

    @Before public void setUp() {
        scratchcards = new Scratchcards();
    }

    @Test public void test_scenario1() throws IOException {
        List<Scratchcards.Card> cards = CommonUtils.loadResource("/input.txt", Scratchcards.Card::parse);
        Scratchcards.Result result = scratchcards.calculateTotalPoints(cards);
        assertEquals((Long) 13L, result.points);
    }

    @Test public void test_scenario2() throws IOException {
        List<Scratchcards.Card> cards = CommonUtils.loadResource("/input.txt", Scratchcards.Card::parse);
        Scratchcards.Result result = scratchcards.calculateTotalPoints(cards);
        assertEquals((Long) 30L, result.cardCount);
    }
}
