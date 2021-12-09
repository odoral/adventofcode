package org.odoral.adventofcode.y2021.day4;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class GiantSquidTest {

    protected GiantSquid giantSquid;

    @Before public void setUp() {
        giantSquid = new GiantSquid();
    }

    @Test public void test_scenario1() throws IOException {
        List<String> numbersAndBoards = CommonUtils.loadResource("/input.txt", Function.identity());
        GiantSquid.Result result = giantSquid.playBingo(numbersAndBoards);
        assertEquals(188, result.board.sumBoardNumbers());
        assertEquals(24, result.lastNumber);
        assertEquals(4512, result.getScenarioResult());
    }

    @Test public void test_scenario2() throws IOException {
        List<String> numbersAndBoards = CommonUtils.loadResource("/input.txt", Function.identity());
        GiantSquid.Result result = giantSquid.findLooser(numbersAndBoards);
        assertEquals(148, result.board.sumBoardNumbers());
        assertEquals(13, result.lastNumber);
        assertEquals(1924, result.getScenarioResult());
    }

}