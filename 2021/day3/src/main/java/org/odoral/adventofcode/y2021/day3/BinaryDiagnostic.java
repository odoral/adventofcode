package org.odoral.adventofcode.y2021.day3;

import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.exception.AdventOfCodeException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BinaryDiagnostic {

    public static void main(String[] args) throws IOException {
        BinaryDiagnostic binaryDiagnostic = new BinaryDiagnostic();
        List<String> diagnosticReport = CommonUtils.loadResource("/input.txt", Function.identity());
        PowerConsumptionResult powerConsumptionResult = binaryDiagnostic.readPowerConsumption(diagnosticReport);
        log.info("Power consumption is is: ({},{})={}", powerConsumptionResult.gammaRate, powerConsumptionResult.epsilonRate, powerConsumptionResult.getPowerConsumption());

        LifeSupportResult lifeSupportResult = binaryDiagnostic.readLifeSupportRating(diagnosticReport);
        log.info("Destination is: ({},{})={}", lifeSupportResult.oxygenGeneratorRating, lifeSupportResult.co2ScrubberRating, lifeSupportResult.getLifeSupportRating());
    }

    public PowerConsumptionResult readPowerConsumption(List<String> diagnosticReport) {
        Integer[] reducedReport = diagnosticReport.stream()
                .map(CommonUtils::toIntegerArray)
                .reduce((a1, a2) -> {
                    for (int i = 0; i < a2.length; i++) {
                        a1[i] += a2[i];
                    }
                    return a1;
                }).orElseThrow(() -> new AdventOfCodeException("No reduced report"));

        Long gammaRate = CommonUtils.fromBinary(Arrays.stream(reducedReport)
                .map(num -> num > diagnosticReport.size() / 2 ? "1" : "0")
                .collect(Collectors.joining()));

        Long epsilonRate = CommonUtils.fromBinary(Arrays.stream(reducedReport)
                .map(num -> num < diagnosticReport.size() / 2 ? "1" : "0")
                .collect(Collectors.joining()));

        return new PowerConsumptionResult(gammaRate, epsilonRate);
    }

    public LifeSupportResult readLifeSupportRating(List<String> diagnosticReport) {
        long oxygenGeneratorRating = readLifeSupportItem(diagnosticReport, (zero, one) -> {
            if (zero > one) {
                return "0";
            } else {
                return "1";
            }
        });
        long co2ScrubberRating = readLifeSupportItem(diagnosticReport, (zero, one) -> {
            if (zero > one) {
                return "1";
            } else {
                return "0";
            }
        });

        return new LifeSupportResult(oxygenGeneratorRating, co2ScrubberRating);
    }

    protected Long readLifeSupportItem(List<String> diagnosticReport, BiFunction<Long, Long, String> determineNumber) {
        List<String> remainingLines = diagnosticReport;
        for (int i = 0; i < diagnosticReport.get(0).length(); i++) {
            if (remainingLines.size() == 1) {
                break;
            }
            final int index = i;
            String mostCommonNumber = getMostCommonNumber(remainingLines, i, determineNumber);
            remainingLines = remainingLines.stream()
                    .filter(line -> mostCommonNumber.equals(line.substring(index, index + 1)))
                    .collect(Collectors.toList());
        }
        return CommonUtils.fromBinary(remainingLines.get(0));
    }

    public String getMostCommonNumber(List<String> diagnosticReport, int column, BiFunction<Long, Long, String> determineNumber) {
        Map<String, BigInteger> collect = diagnosticReport.stream()
                .map(line -> line.substring(column, column + 1))
                .collect(Collectors.toMap(Function.identity(), s -> BigInteger.ONE, BigInteger::add));

        long numberOfZeroes = collect.get("0").longValue();
        long numberOfOnes = collect.get("1").longValue();

        return determineNumber.apply(numberOfZeroes, numberOfOnes);
    }

    public static class PowerConsumptionResult {
        final long gammaRate;
        final long epsilonRate;

        public PowerConsumptionResult(long gammaRate, long epsilonRate) {
            this.gammaRate = gammaRate;
            this.epsilonRate = epsilonRate;
        }

        public long getPowerConsumption() {
            return gammaRate * epsilonRate;
        }

    }

    public static class LifeSupportResult {
        final long oxygenGeneratorRating;
        final long co2ScrubberRating;

        public LifeSupportResult(long oxygenGeneratorRating, long co2ScrubberRating) {
            this.oxygenGeneratorRating = oxygenGeneratorRating;
            this.co2ScrubberRating = co2ScrubberRating;
        }

        public long getLifeSupportRating() {
            return oxygenGeneratorRating * co2ScrubberRating;
        }

    }

}