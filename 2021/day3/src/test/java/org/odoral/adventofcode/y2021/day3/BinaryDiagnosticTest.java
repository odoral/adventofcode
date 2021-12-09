package org.odoral.adventofcode.y2021.day3;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class BinaryDiagnosticTest {

    protected BinaryDiagnostic binaryDiagnostic;

    @Before public void setUp() {
        binaryDiagnostic = new BinaryDiagnostic();
    }

    @Test public void test_scenario1() throws IOException {
        List<String> diagnosticReport = CommonUtils.loadResource("/input.txt", Function.identity());
        BinaryDiagnostic.PowerConsumptionResult powerConsumptionResult = binaryDiagnostic.readPowerConsumption(diagnosticReport);
        assertEquals(22, powerConsumptionResult.gammaRate);
        assertEquals(9, powerConsumptionResult.epsilonRate);
        assertEquals(198, powerConsumptionResult.getPowerConsumption());
    }

    @Test public void test_scenario2() throws IOException {
        List<String> plannedRoute = CommonUtils.loadResource("/input.txt", Function.identity());
        BinaryDiagnostic.LifeSupportResult powerConsumptionResult = binaryDiagnostic.readLifeSupportRating(plannedRoute);
        assertEquals(23, powerConsumptionResult.oxygenGeneratorRating);
        assertEquals(10, powerConsumptionResult.co2ScrubberRating);
        assertEquals(230, powerConsumptionResult.getLifeSupportRating());
    }

}