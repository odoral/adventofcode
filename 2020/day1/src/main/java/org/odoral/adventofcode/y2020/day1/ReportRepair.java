package org.odoral.adventofcode.y2020.day1;

import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReportRepair {

    public static final int _2020 = 2020;

    public static void main(String [] args) throws IOException {
        ReportRepair reportRepair = new ReportRepair();
        List<Integer> numbers = CommonUtils.loadResource("/input.txt", Integer::parseInt);
        List<Result> numbersWhichSum = reportRepair.getTwoNumbersWhichSum(numbers, _2020);
        Result result = numbersWhichSum.get(0);
        log.info("{} + {} = {} => result: {}", 
            result.numbers[0], 
            result.numbers[1], _2020, 
            result.getStream()
                .mapToInt(i -> i)
                .reduce((r, l) -> r * l));
        
        numbersWhichSum = reportRepair.getThreeNumbersWhichSum(numbers, _2020);
        result = numbersWhichSum.get(0);
        log.info("{} + {} + {} = {} => result: {}", 
            result.numbers[0], 
            result.numbers[1], 
            result.numbers[2], 
            _2020, 
            result.getStream()
                .mapToInt(i -> i)
                .reduce((r, l) -> r * l)
                .orElseThrow(() -> new IllegalStateException("There is no number in the stream")));
    }
    
    public List<Result> getTwoNumbersWhichSum(List<Integer> numbers, int numToMatch){
        List<Result> results = new ArrayList<>();
        for (int indexPos = 0; indexPos < numbers.size(); indexPos++) {
            for (int other = indexPos + 1; other < numbers.size(); other++) {
                Integer number1 = numbers.get(indexPos);
                Integer number2 = numbers.get(other);
                if (number1 + number2 == numToMatch){
                    log.info("Found two matching number which adds to {}: [{}, {}]", numToMatch, number1, number2);
                    results.add(new Result(number1, number2));
                }
            }
        }
        return results;
    }

    public List<Result> getThreeNumbersWhichSum(List<Integer> numbers, int numToMatch){
        List<Result> results = new ArrayList<>();
        for (int indexPos = 0; indexPos < numbers.size(); indexPos++) {
            Integer number1 = numbers.get(indexPos);
            for (int other1 = indexPos + 1; other1 < numbers.size(); other1++) {
                Integer number2 = numbers.get(other1);
                for (int other2 = other1 + 1; other2 < numbers.size(); other2++) {
                    Integer number3 = numbers.get(other2);
                    if (number1 + number2 + number3 == numToMatch) {
                        log.info("Found three matching number which adds to {}: [{}, {}, {}]", numToMatch, number1, number2, number3);
                        results.add(new Result(number1, number2, number3));
                    }
                }
            }
        }
        return results;
    }
    
    public class Result{
        final Integer [] numbers;
        
        public Result(Integer ... numbers) {
            this.numbers = numbers;
        }
        
        public Stream<Integer> getStream(){
            return Stream.of(numbers);
        }
    }
}