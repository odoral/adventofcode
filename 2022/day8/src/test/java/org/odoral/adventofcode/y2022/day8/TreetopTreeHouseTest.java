package org.odoral.adventofcode.y2022.day8;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.model.ValuedPoint;

import java.io.IOException;
import java.util.Comparator;

import static org.junit.Assert.assertEquals;

public class TreetopTreeHouseTest {

    protected TreetopTreeHouse treetopTreeHouse;

    @Before public void setUp() {
        treetopTreeHouse = new TreetopTreeHouse();
    }

    @Test public void test_scenario1() throws IOException {
        TreetopTreeHouse treetopTreeHouse = new TreetopTreeHouse();
        ValuedPoint<Integer>[][] treeHeight = treetopTreeHouse.loadInputMap("/input.txt");
        treetopTreeHouse.initColumnCache(treeHeight);
        long visibleTrees = treetopTreeHouse.process(treeHeight, treetopTreeHouse.IS_VISIBLE).stream().filter(b -> b).count();

        assertEquals(21, visibleTrees);
    }

    @Test public void test_scenario2() throws IOException {
        TreetopTreeHouse treetopTreeHouse = new TreetopTreeHouse();
        ValuedPoint<Integer>[][] treeHeight = treetopTreeHouse.loadInputMap("/input.txt");
        treetopTreeHouse.initColumnCache(treeHeight);
        assertEquals(8, treetopTreeHouse.SCENIC_SCORE.apply(treeHeight, new ValuedPoint<>(3, 2, 5)).longValue());

        Long maxScenicScore = treetopTreeHouse.process(treeHeight, treetopTreeHouse.SCENIC_SCORE).stream().max(Comparator.naturalOrder()).orElse(-1L);
        assertEquals(8, maxScenicScore.longValue());
    }

}