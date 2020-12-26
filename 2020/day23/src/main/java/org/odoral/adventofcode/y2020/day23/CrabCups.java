package org.odoral.adventofcode.y2020.day23;

import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.y2020.day23.exception.CrabCupsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CrabCups {

    public static void main(String[] args) {
        CrabCups crabCups = new CrabCups();
        CupClock cupClock = CupClock.from("198753462", 1L);
        cupClock = crabCups.move(cupClock, 100);
        log.info("Labels on the cups after 100 moves: {}", cupClock.getCupsAfter(1));

        cupClock = CrabCups.CupClock.fromPartTwo("198753462", 1L);
        cupClock = crabCups.move(cupClock, 10000000);
        log.info("Result after ten million moves: {}", cupClock.getMultiplicationOfTwoLabelsAfterLabel(1));
    }

    public CupClock move(CupClock cupClock, int moveCount) {
        for (int i = 0; i < moveCount; i++) {
            if(i % 100 == 0){
                log.info("{} moves done.", i);
            }
            cupClock = move(cupClock);
        }
        return cupClock;
    }

    public CupClock move(CupClock cupClock) {
        //The crab picks up the three cups that are immediately clockwise of the current cup.
        Cup currentCup = cupClock.currentCup;
        List<Cup> pickedCups = new ArrayList<>();
        pickedCups.add(currentCup.nextCup);
        while (pickedCups.size() < 3){
            pickedCups.add(pickedCups.get(pickedCups.size() - 1).nextCup);
        }
        
        //The crab selects a destination cup: the cup with a label equal to the current cup's label minus one.
        // If this would select one of the cups that was just picked up, the crab will keep subtracting one until it finds a cup that wasn't just picked up.
        long destinationLabel = currentCup.label == cupClock.minCupValue ? cupClock.maxCupValue : currentCup.label - 1;
        while(isValidDestinationLabel(pickedCups, destinationLabel)){
            // If at any point in this process the value goes below the lowest value on any cup's label, it wraps around to the highest value on any cup's label instead.
            destinationLabel--;
            if(destinationLabel < cupClock.minCupValue){
                destinationLabel = cupClock.maxCupValue;
            }
        }
        
        //The crab places the cups it just picked up so that they are immediately clockwise of the destination cup. They keep the same order as when they were picked up.
        currentCup.nextCup = pickedCups.get(2).nextCup;
        Cup destinationCup = cupClock.labeledCups.get(destinationLabel);
        pickedCups.get(2).nextCup = destinationCup.nextCup;
        destinationCup.nextCup = pickedCups.get(0);

        //The crab selects a new current cup: the cup which is immediately clockwise of the current cup.
        cupClock.currentCup = currentCup.nextCup;
        
        return cupClock;
    }

    protected boolean isValidDestinationLabel(List<Cup> pickedCups, long destinationLabel) {
        return pickedCups.stream().anyMatch(cup -> cup.label.equals(destinationLabel));
    }

    public static class CupClock {
        final Map<Long, Cup> labeledCups = new HashMap<>();
        Cup currentCup;
        final long cupCount;
        final long minCupValue;
        final long maxCupValue;

        public CupClock(List<Long> cups, int currentCupIndex) {
            initLabeledCups(cups, currentCupIndex);
            this.cupCount = cups.size();
            this.minCupValue = cups.stream().min(Long::compareTo).orElseThrow(() -> new CrabCupsException("There is no min label"));
            this.maxCupValue = cups.stream().max(Long::compareTo).orElseThrow(() -> new CrabCupsException("There is no max label"));
        }

        protected void initLabeledCups(List<Long> orderedLabels, int currentCupIndex) {
            Cup firstCup = null;
            Long previousLabel = null;
            for (int i = 0; i < orderedLabels.size(); i++) {
                Long label = orderedLabels.get(i);
                Cup cup = new Cup(label);
                if(i == 0){
                    firstCup = cup;
                }else{
                    if(i == orderedLabels.size() - 1){
                        cup.nextCup = firstCup;
                    }
                    labeledCups.get(previousLabel).nextCup = cup;
                }
                
                labeledCups.put(label, cup);
                
                if(currentCupIndex == i){
                    currentCup = cup;
                }
                
                previousLabel = label;
            }
        }

        public static CupClock from(String cupArrangement, Long currentCup){
            List<Long> intCupArrangement = Stream.of(CommonUtils.toCharacterArray(cupArrangement))
                .map(String::valueOf)
                .map(Long::valueOf)
                .collect(Collectors.toList());
            int currentCupIndex = intCupArrangement.indexOf(currentCup);
            return new CupClock(intCupArrangement, currentCupIndex);
        }

        public static CupClock fromPartTwo(String cupArrangement, Long currentCup){
            List<Long> intCupArrangement = Stream.of(CommonUtils.toCharacterArray(cupArrangement))
                .map(String::valueOf)
                .map(Long::valueOf)
                .collect(Collectors.toList());
            int currentCupIndex = intCupArrangement.indexOf(currentCup);
            long maxLabel = intCupArrangement.stream().max(Long::compareTo).orElseThrow(() -> new CrabCupsException("There is no max label"));
            for (long i = maxLabel + 1; i <= 1000000 ; i++) {
                intCupArrangement.add(i);
            }
            return new CupClock(intCupArrangement, currentCupIndex);
        }

        public String getCupsAfter(long label) {
            Cup sourceCup = labeledCups.get(label);
            Cup cup = sourceCup;
            StringBuilder sb = new StringBuilder();
            while(!cup.nextCup.equals(sourceCup)){
                sb.append(cup.nextCup.label);
                cup = cup.nextCup;
            }
            return sb.toString();
        }
        
        public Long getMultiplicationOfTwoLabelsAfterLabel(long label){
            Cup cup = labeledCups.get(label);
            return cup.nextCup.label * cup.nextCup.nextCup.label;
        }
    }

    @RequiredArgsConstructor
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    public static class Cup {
        @EqualsAndHashCode.Include final Long label;
        Cup nextCup;
    }
}
