package org.odoral.adventofcode.y2015.day2;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class IWasToldThereWouldBeNoMathTest {

    protected IWasToldThereWouldBeNoMath iWasToldThereWouldBeNoMath;

    @Before public void setUp() {
        iWasToldThereWouldBeNoMath = new IWasToldThereWouldBeNoMath();
    }
    
    @Test public void test() throws IOException {
        List<IWasToldThereWouldBeNoMath.Dimension> dimensions = iWasToldThereWouldBeNoMath.loadScenario("/scenario1.txt");
        
        assertEquals(1, dimensions.size());
        IWasToldThereWouldBeNoMath.Dimension dimension = dimensions.get(0);
        assertEquals(2, dimension.l);
        assertEquals(3, dimension.w);
        assertEquals(4, dimension.h);
        assertEquals(52, dimension.neededPaper());
        assertEquals(6, dimension.extraPaper());
        
        int totalNeeded = iWasToldThereWouldBeNoMath.calculatePaperNeeded(dimensions);
        assertEquals(58, totalNeeded);

        assertEquals(34, iWasToldThereWouldBeNoMath.calculateRibbonNeeded(
            Collections.singletonList(new IWasToldThereWouldBeNoMath.Dimension(2, 3, 4))));
        assertEquals(14, iWasToldThereWouldBeNoMath.calculateRibbonNeeded(
            Collections.singletonList(new IWasToldThereWouldBeNoMath.Dimension(1, 1, 10))));
    }
}