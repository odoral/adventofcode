package org.odoral.adventofcode.y2021.day1;

import lombok.extern.slf4j.Slf4j;
import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class SonarSweep {

    public static final int SLIDING_WINDOW = 3;

    public static void main(String[] args) throws IOException {
        SonarSweep sonarSweep = new SonarSweep();
        List<Integer> depths = CommonUtils.loadResource("/input.txt", Integer::parseInt);
        Result result = sonarSweep.getLargerMeasurements(depths);
        log.info("There are {} larger measurements", result.largerMeasurements.length);

        Result result2 = sonarSweep.getLargerMeasurementsSlidingWindowMethod(depths, SLIDING_WINDOW);
        log.info("There are {} larger measurements using sliding window", result2.largerMeasurements.length);
    }

    public Result getLargerMeasurements(List<Integer> depths) {
        AtomicInteger previousMeasurement = new AtomicInteger(depths.get(0));
        return new Result(depths.stream()
                .skip(1)
                .filter(measurement -> previousMeasurement.getAndSet(measurement) < measurement)
                .toArray(Integer[]::new));
    }

    public Result getLargerMeasurementsSlidingWindowMethod(List<Integer> depths, int slidingWindow) {
        int index = 0;
        Integer previousMeasure = null;
        List<Integer> result = new ArrayList<>();
        while (index <= depths.size() - slidingWindow) {
            int newMeasure = depths.get(index) + depths.get(index + 1) + depths.get(index + 2);
            if(Objects.nonNull(previousMeasure)){
                log.debug("Comparing {} <> {}", previousMeasure, newMeasure);
                if(newMeasure > previousMeasure){
                    result.add(newMeasure);
                }
            }

            previousMeasure = newMeasure;
            index++;
        }
        return new Result(result.toArray(new Integer[0]));
    }

    public static class Result {
        final Integer[] largerMeasurements;

        public Result(Integer... largerMeasurements) {
            this.largerMeasurements = largerMeasurements;
        }

    }
}