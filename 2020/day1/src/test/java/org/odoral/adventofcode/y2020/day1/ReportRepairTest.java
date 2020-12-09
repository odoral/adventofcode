package org.odoral.adventofcode.y2020.day1;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class ReportRepairTest {

    protected ReportRepair reportRepair;

    @Before public void setUp() {
        reportRepair = new ReportRepair();
    }
    
    @Test public void test_twoNumbers() throws IOException {
        List<Integer> numbers = CommonUtils.loadResource("/scenario1.txt", Integer::parseInt);
        List<ReportRepair.Result> numbersWhichSum = reportRepair.getTwoNumbersWhichSum(numbers, ReportRepair._2020);
        assertEquals(1, numbersWhichSum.size());
        ReportRepair.Result result = numbersWhichSum.get(0);
        assertEquals(2, result.getStream().count());
        assertEquals(ReportRepair._2020, result.getStream().mapToInt(i -> i).sum());
        assertEquals(new Integer(1721), result.numbers[0]);
        assertEquals(new Integer(299), result.numbers[1]);
        assertEquals(514579, result.getStream()
            .mapToInt(i -> i)
            .reduce((r, l) -> r*l)
            .orElseThrow(() -> new IllegalStateException("There is no number in the stream")));
    }
    
    @Test public void test_threeNumbers() throws IOException {
        List<Integer> numbers = CommonUtils.loadResource("/scenario1.txt", Integer::parseInt);
        List<ReportRepair.Result> numbersWhichSum = reportRepair.getThreeNumbersWhichSum(numbers, ReportRepair._2020);
        assertEquals(1, numbersWhichSum.size());
        ReportRepair.Result result = numbersWhichSum.get(0);
        assertEquals(3, result.getStream().count());
        assertEquals(ReportRepair._2020, result.getStream().mapToInt(i -> i).sum());
        assertEquals(new Integer(979), result.numbers[0]);
        assertEquals(new Integer(366), result.numbers[1]);
        assertEquals(new Integer(675), result.numbers[2]);
        assertEquals(241861950, result.getStream()
            .mapToInt(i -> i)
            .reduce((r, l) -> r*l)
            .orElseThrow(() -> new IllegalStateException("There is no number in the stream")));
    }
}