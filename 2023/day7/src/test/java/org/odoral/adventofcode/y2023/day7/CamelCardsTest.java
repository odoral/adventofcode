package org.odoral.adventofcode.y2023.day7;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CamelCardsTest {

    protected CamelCards camelCards;

    @Before public void setUp() {
        camelCards = new CamelCards();
    }

    @Test public void test_scenario1() throws IOException {
        List<CamelCards.Hand> hands = CommonUtils.loadResource("/input.txt", CamelCards.Hand::parse);
        CamelCards.Result result = camelCards.getTotalWinnings(hands);
        assertEquals(6440L, result.totalWinnings);
    }

    @Test public void test_scenario2() throws IOException {
        List<CamelCards.Hand> hands = CommonUtils.loadResource("/input.txt", CamelCards.Hand::parse);
        camelCards.setConsiderJokers(true);
        CamelCards.Result result = camelCards.getTotalWinnings(hands);
        assertEquals(5905L, result.totalWinnings);
    }

}
