package org.odoral.adventofcode.y2022.day4;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CampCleanupTest {

    protected CampCleanup campCleanup;

    @Before public void setUp() {
        campCleanup = new CampCleanup();
    }

    @Test public void test_scenario1() throws IOException {
        CampCleanup campCleanup = new CampCleanup();
        List<List<CampCleanup.Range>> ranges = CommonUtils.loadResource("/input.txt", campCleanup::load);
        CampCleanup.Result result = campCleanup.getOverlappedRanges(ranges, CampCleanup.Range::fullyOverlap);

        assertEquals(result.overlappedRanges.toString(), 2, result.overlappedRanges.size());
    }

    @Test public void test_scenario2() throws IOException {
        CampCleanup campCleanup = new CampCleanup();
        List<List<CampCleanup.Range>> ranges = CommonUtils.loadResource("/input.txt", campCleanup::load);
        CampCleanup.Result result = campCleanup.getOverlappedRanges(ranges, CampCleanup.Range::overlap);

        assertEquals(result.overlappedRanges.toString(), 4, result.overlappedRanges.size());
    }

}