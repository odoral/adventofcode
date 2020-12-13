package org.odoral.adventofcode.y2020.day13;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@Slf4j
public class ShuttleSearchTest {

    protected ShuttleSearch shuttleSearch;

    @Before public void setUp() {
        shuttleSearch = new ShuttleSearch();
    }
    
    @Test public void test() throws IOException {
        ShuttleSearch.InputData inputData = shuttleSearch.loadInputData("/scenario1.txt");
        assertEquals(939, inputData.earliestDepartTime.intValue());
        assertArrayEquals(new String[]{"7","13","x","x","59","x","31","19"}, inputData.busIds.toArray());
        assertArrayEquals(new Integer[]{7,13,59,31,19}, inputData.getBusesInDuty().toArray());
        
        ShuttleSearch.BusResult busResult = shuttleSearch.lookForEarliestBus(inputData);
        assertEquals(59, busResult.busId.intValue());
        assertEquals(944, busResult.departTime.intValue());
        assertEquals(5, busResult.waitingTime.intValue());
        assertEquals(295, busResult.getFirstPartResult());

        log.info("Result for first part: {}", busResult.getFirstPartResult());

        long subsequentsDepartsTimeStamp = shuttleSearch.lookForSubsequentsDepartsTimeStamp(inputData, 1);
        assertEquals(1068781, subsequentsDepartsTimeStamp);

        inputData.busIds = Arrays.asList("17","x","13","19");
        subsequentsDepartsTimeStamp = shuttleSearch.lookForSubsequentsDepartsTimeStamp(inputData, 1);
        assertEquals(3417, subsequentsDepartsTimeStamp);

        inputData.busIds = Arrays.asList("67","7","59","61");
        subsequentsDepartsTimeStamp = shuttleSearch.lookForSubsequentsDepartsTimeStamp(inputData, 1);
        assertEquals(754018, subsequentsDepartsTimeStamp);

        inputData.busIds = Arrays.asList("67","x","7","59","61");
        subsequentsDepartsTimeStamp = shuttleSearch.lookForSubsequentsDepartsTimeStamp(inputData, 1);
        assertEquals(779210, subsequentsDepartsTimeStamp);
        
        inputData.busIds = Arrays.asList("67","7","x","59","61");
        subsequentsDepartsTimeStamp = shuttleSearch.lookForSubsequentsDepartsTimeStamp(inputData, 1);
        assertEquals(1261476, subsequentsDepartsTimeStamp);
        
        inputData.busIds = Arrays.asList("1789","37","47","1889");
        subsequentsDepartsTimeStamp = shuttleSearch.lookForSubsequentsDepartsTimeStamp(inputData, 1);
        assertEquals(1202161486, subsequentsDepartsTimeStamp);
    }
}