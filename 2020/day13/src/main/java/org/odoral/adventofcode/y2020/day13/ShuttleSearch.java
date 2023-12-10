package org.odoral.adventofcode.y2020.day13;

import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShuttleSearch {

    public static void main(String[] args) throws IOException {
        ShuttleSearch shuttleSearch = new ShuttleSearch();
        ShuttleSearch.InputData inputData = shuttleSearch.loadInputData("/input.txt");

        ShuttleSearch.BusResult busResult = shuttleSearch.lookForEarliestBus(inputData);
        log.info("Result for first part: {}", busResult.getFirstPartResult());

        long subsequentsDepartsTimeStamp = shuttleSearch.lookForSubsequentsDepartsTimeStamp(inputData, 100000000000000L);
        log.info("Timestamp for subsequents departs: {}", subsequentsDepartsTimeStamp);
    }

    public InputData loadInputData(String resource) throws IOException {
        InputData inputData = new InputData();
        CommonUtils.loadResource(resource, inputData::load);
        return inputData;
    }

    public BusResult lookForEarliestBus(InputData inputData) {
        Integer earliestDepartTime = inputData.earliestDepartTime;
        
        return inputData.getBusesInDuty().stream()
            .map(busId -> calculateBusResult(earliestDepartTime, busId))
            .min(Comparator.comparing(BusResult::getWaitingTime))
            .orElseThrow(() -> new UnsupportedOperationException("There are no buses in the input data."));
    }

    private BusResult calculateBusResult(Integer earliestDepartTime, Integer busId) {
        BusResult busResult;
        if(earliestDepartTime % busId == 0){
            busResult = new BusResult(busId, earliestDepartTime, 0);
        }else{
            int waitTime = (busId * ((earliestDepartTime / busId) + 1)) - earliestDepartTime;
            busResult = new BusResult(busId, earliestDepartTime + waitTime, waitTime);
        }
        return busResult;
    }

    public long lookForSubsequentsDepartsTimeStamp(InputData inputData, long fromTimeStamp) {
        Integer[] busIds = inputData.busIds.stream()
            .map(busId -> "x".equals(busId) ? -1 : Integer.parseInt(busId))
            .toArray(Integer[]::new);
        
        
        Integer firstBusId = busIds[0];
        long currentTimeStamp = ((fromTimeStamp / firstBusId.longValue()) + 1) * firstBusId.longValue();
        List<Number> rates = new ArrayList<>();
        rates.add(firstBusId);
        long increment = CommonUtils.lcm(rates);
        for (int offset = 1; offset < busIds.length; offset++) {
            Integer currentBusId = busIds[offset];
            if(currentBusId == -1) continue;
            while(isOutOfSync(currentTimeStamp, currentBusId, offset)){
                currentTimeStamp+=increment;
            }
            rates.add(currentBusId);
            increment = CommonUtils.lcm(rates);
            log.info("Found sync with busId[{}]={} => increments: {}", offset, currentBusId, increment);
        }

        return currentTimeStamp;
    }

    protected boolean isOutOfSync(long currentTimeStamp, Integer currentBusId, long currentBusOffset) {
        return (currentTimeStamp + currentBusOffset) % currentBusId != 0;
    }

    public static class InputData {
        static final Predicate<String> OUT_OF_SERVICE_BUS = "x"::equals;
        Integer earliestDepartTime;
        List<String> busIds;
        
        public InputData load(String line){
            if(Objects.isNull(earliestDepartTime)){
                earliestDepartTime = Integer.parseInt(line);
            }else{
                busIds = Stream.of(line.split(","))
                    .collect(Collectors.toList());
            }
            
            return this;
        }
        
        public List<Integer> getBusesInDuty(){
            return busIds.stream()
                .filter(OUT_OF_SERVICE_BUS.negate())
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        }
    }

    @Data
    @ToString
    public class BusResult {
        final Integer busId;
        final Integer departTime;
        final Integer waitingTime;

        public BusResult(Integer busId, Integer departTime, Integer waitingTime) {
            this.busId = busId;
            this.departTime = departTime;
            this.waitingTime = waitingTime;
        }

        public int getFirstPartResult() {
            return waitingTime * busId;
        }
    }
}
