package org.odoral.adventofcode.y2021.day9;

import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.model.ValuedPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SmokeBasin {

    public static void main(String[] args) throws IOException {
        SmokeBasin smokeBasin = new SmokeBasin();
        Integer [][] inputMap = CommonUtils.loadResource("/input.txt", CommonUtils::toIntegerArray)
            .toArray(new Integer[0][]);
        ResultStep1 resultStep1 = smokeBasin.findLowerPoints(inputMap);
        log.info("Found {} lower points with sum of risk levels: {}", resultStep1.lowerPoints.size(), resultStep1.sumOfRiskLevels());

        ResultStep2 resultStep2 = smokeBasin.findBasins(inputMap);
        log.info("Found {} basins. Result for second step: {}", resultStep2.basins.size(), resultStep2.multiplyOfThreeLargerBasins());
    }

    public ResultStep1 findLowerPoints(Integer[][] inputMap) {
        List<ValuedPoint<Integer>> lowerPoints = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < inputMap.length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < inputMap[rowIndex].length; columnIndex++) {
                final int currentValue = inputMap[rowIndex][columnIndex];
                List<org.odoral.adventofcode.common.model.ValuedPoint<Integer>> neighbours = CommonUtils.findNeighbours(inputMap, rowIndex, columnIndex, false);
                if(neighbours.stream().noneMatch(n -> n.value <= currentValue)){
                    log.debug("Found lower point at {},{} = {}", rowIndex, columnIndex, currentValue);
                    lowerPoints.add(new ValuedPoint<>(rowIndex, columnIndex, currentValue));
                }
            }
        }
        return new ResultStep1(lowerPoints);
    }

    public ResultStep2 findBasins(Integer[][] inputMap) {
        List<ValuedPoint<Integer>> lowerPoints = findLowerPoints(inputMap).lowerPoints;
        List<Set<ValuedPoint<Integer>>> basins = lowerPoints.stream()
            .map(point -> {
                Set<ValuedPoint<Integer>> basinPoints = calculateBasin(inputMap, point, new HashSet<>());
                log.debug("Basin for {} -> {}", point, basinPoints);
                return basinPoints;
            })
            .collect(Collectors.toList());

        return new ResultStep2(basins);
    }

    protected Set<ValuedPoint<Integer>> calculateBasin(Integer[][] inputMap, ValuedPoint<Integer> point, HashSet<ValuedPoint<Integer>> basinPoints) {
        basinPoints.add(point);
        basinPoints.addAll(CommonUtils.findNeighbours(inputMap, point.x, point.y, false)
            .stream()
            .filter(neighbour -> !basinPoints.contains(neighbour))
            .filter(neighbour -> isValidBasinNeighbour(point, neighbour))
            .flatMap(neighbour -> calculateBasin(inputMap, neighbour, basinPoints).stream())
            .collect(Collectors.toList()));
        return basinPoints;
    }

    protected boolean isValidBasinNeighbour(ValuedPoint<Integer> point, ValuedPoint<Integer> neighbour) {
        return neighbour.value != 9 && neighbour.value > point.value;
    }

    public static class ResultStep1 {
        public final List<ValuedPoint<Integer>> lowerPoints;

        public ResultStep1(List<ValuedPoint<Integer>> lowerPoints) {
            this.lowerPoints = lowerPoints;
        }

        public int sumOfRiskLevels() {
            return lowerPoints.stream()
                .mapToInt(vp -> vp.value + 1)
                .sum();
        }
    }
    
    public static class ResultStep2 {
        public final List<Set<ValuedPoint<Integer>>> basins;

        public ResultStep2(List<Set<ValuedPoint<Integer>>> basins) {
            this.basins = basins;
        }

        public int multiplyOfThreeLargerBasins() {
            return basins.stream()
                .map(Set::size)
                .sorted((i1, i2) -> i2 - i1)
                .mapToInt(i -> i)
                .limit(3)
                .reduce(1, Math::multiplyExact);
        }
    }
    
}