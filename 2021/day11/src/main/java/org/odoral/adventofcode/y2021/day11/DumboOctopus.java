package org.odoral.adventofcode.y2021.day11;

import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.model.Point;
import org.odoral.adventofcode.common.model.ValuedPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DumboOctopus {

    public static void main(String[] args) throws IOException {
        DumboOctopus dumboOctopus = new DumboOctopus();
        Integer[][] inputMap = DumboOctopus.loadInputMap("/input.txt");
        int steps = 100;
        DumboOctopus.Result result = dumboOctopus.processSteps(inputMap, steps);
        log.info("Flashes after {} steps: {}", steps, result.numberOfFlashes);

        inputMap = DumboOctopus.loadInputMap("/input.txt");
        result = dumboOctopus.findStepAllOctopusesFlash(inputMap);
        log.info("All dumbo octopuses flashes in step: {}", result.steps);
    }

    public static Integer[][] loadInputMap(String resource) throws IOException {
        return CommonUtils.loadResource(resource, CommonUtils::toIntegerArray)
                .toArray(new Integer[0][]);
    }

    public Result processSteps(Integer[][] inputMap, int steps) {
        Result result = new Result(inputMap, 0, 0);
        for (int i = 0; i < steps; i++) {
            result = result.add(processStep(result.inputMap));
        }
        return result;
    }

    public Result processStep(Integer[][] inputMap) {
        int numFlashes = 0;
        List<Point> dumboOctopusFlashes = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < inputMap.length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < inputMap[rowIndex].length; columnIndex++) {
                inputMap[rowIndex][columnIndex]++;
                if (flashOnEnergyIncrease(inputMap, rowIndex, columnIndex)) {
                    numFlashes++;
                    dumboOctopusFlashes.add(new Point(rowIndex, columnIndex));
                }
            }
        }

        for (Point dumboOctopusFlash : dumboOctopusFlashes) {
            numFlashes += propagateEnergyToNeighbours(inputMap, dumboOctopusFlash.x, dumboOctopusFlash.y);
        }

        return new Result(inputMap, numFlashes, 1);
    }

    protected boolean flashOnEnergyIncrease(Integer[][] inputMap, int rowIndex, int columnIndex) {
        inputMap[rowIndex][columnIndex] = inputMap[rowIndex][columnIndex] % 10;
        return inputMap[rowIndex][columnIndex] == 0;
    }

    protected boolean flashOnEnergyReceivedFromNeighbour(Integer[][] inputMap, int rowIndex, int columnIndex) {
        if (inputMap[rowIndex][columnIndex] != 0) {
            inputMap[rowIndex][columnIndex] = (inputMap[rowIndex][columnIndex] + 1) % 10;
            return inputMap[rowIndex][columnIndex] == 0;
        }

        return false;
    }

    protected int propagateEnergyToNeighbours(Integer[][] inputMap, int rowIndex, int columnIndex) {
        int flashes = 0;
        List<ValuedPoint<Integer>> neighbours = CommonUtils.findNeighbours(inputMap, rowIndex, columnIndex, true);
        for (int i = 0; i < neighbours.size(); i++) {
            ValuedPoint<Integer> neighbour = neighbours.get(i);
            if (flashOnEnergyReceivedFromNeighbour(inputMap, neighbour.x, neighbour.y)) {
                flashes++;
                flashes += propagateEnergyToNeighbours(inputMap, neighbour.x, neighbour.y);
            }
        }

        return flashes;
    }

    public Result findStepAllOctopusesFlash(Integer[][] inputMap) {
        Result result = new Result(inputMap, 0, 0);
        Result lastStep;
        do {
            lastStep = processStep(result.inputMap);
            result = result.add(lastStep);
        } while (lastStep.numberOfFlashes != inputMap.length * inputMap[0].length);
        return result;
    }

    public static class Result {
        public final Integer[][] inputMap;
        public final int numberOfFlashes;
        public final int steps;

        public Result(Integer[][] inputMap, int numberOfFlashes, int steps) {
            this.inputMap = inputMap;
            this.numberOfFlashes = numberOfFlashes;
            this.steps = steps;
        }

        public Result add(Result other) {
            return new Result(other.inputMap, numberOfFlashes + other.numberOfFlashes, steps + 1);
        }
    }
}