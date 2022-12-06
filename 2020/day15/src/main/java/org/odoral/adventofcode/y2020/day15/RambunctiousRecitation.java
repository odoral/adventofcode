package org.odoral.adventofcode.y2020.day15;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RambunctiousRecitation {

    public static void main(String[] args) {
        RambunctiousRecitation rambunctiousRecitation = new RambunctiousRecitation();
        long number = rambunctiousRecitation.play(rambunctiousRecitation.setStartingNumbers(9, 3, 1, 0, 8, 4), 2020, 4L);
        log.info("Number of 2020th: {}", number);
        number = rambunctiousRecitation.play(rambunctiousRecitation.setStartingNumbers(9, 3, 1, 0, 8, 4), 30000000, 4L);
        log.info("Number of 30000000th: {}", number);
    }

    public Map<Long, NumberStatus> setStartingNumbers(long ... numbers) {
        Map<Long, NumberStatus> startingNumbers = new HashMap<>();
        long turn = 1;
        for (long number : numbers) {
            startingNumbers.put(number, NumberStatus.getInstance(turn++));
        }
        return startingNumbers;
    }

    public long play(Map<Long, NumberStatus> startingNumbers, long turns, long lastNumber) {
        long nextTurn = startingNumbers.size() + 1L;
        for (long turn = nextTurn; turn <= turns; turn++) {
            NumberStatus numberStatus = startingNumbers.get(lastNumber);
            if(numberStatus.isNew){
                lastNumber = 0L;
                numberStatus.isNew = false;
            }else{
                long previousTurn = turn - 1;
                lastNumber = previousTurn - numberStatus.turn;
                numberStatus.turn = previousTurn;
            }
            computeTurnNumber(startingNumbers, lastNumber, turn);
        }
        log.info("Number for turn {}: {}", turns, lastNumber);
        return lastNumber;
    }

    protected void computeTurnNumber(Map<Long, NumberStatus> startingNumbers, long turnNumber, long turn) {
        startingNumbers.compute(turnNumber, (k, v) -> {
            if (Objects.isNull(v)) {
                return NumberStatus.getInstance(turn);
            } else {
                v.isNew = false;
                return v;
            }
        });
    }

    @AllArgsConstructor
    @ToString
    public static class NumberStatus{
        long turn;
        boolean isNew;
        
        public static NumberStatus getInstance(long turn){
            return new NumberStatus(turn, true);
        }
    }
}
