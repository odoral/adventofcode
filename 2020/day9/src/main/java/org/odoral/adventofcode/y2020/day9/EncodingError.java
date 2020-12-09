package org.odoral.adventofcode.y2020.day9;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EncodingError {
    
    public static void main(String [] args) throws IOException {
        EncodingError encodingError = new EncodingError();
        List<Long> numbers = encodingError.loadNumbers("/input.txt");
        log.info("Loaded {} numbers", numbers.size());

        encodingError.checkEncodingErrors(encodingError, numbers, 25);
        
        long invalidNumber = 1309761972;
        long encryptionWeakness = encodingError.getEncryptionWeakness(numbers, invalidNumber);
        log.info("Encryption weakness for {} is: {}", invalidNumber, encryptionWeakness);
    }

    public List<Long> loadNumbers(String resourcePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(resourcePath)))) {
            return br.lines()
                .map(Long::parseLong)
                .collect(Collectors.toList());
        }
    }

    protected void checkEncodingErrors(EncodingError encodingError, List<Long> numbers, int preamble) {
        for (int index = preamble; index < numbers.size(); index++) {
            boolean isValid = encodingError.isValidNumber(numbers, index, preamble);
            if(!isValid){
                log.error("Number: {} at position: {} is not valid.", numbers.get(index), index);
            }
        }
    }

    public boolean isValidNumber(List<Long> numbers, int index, int preamble) {
        Set<Long> validNumbers = calculateValidNumbers(numbers, index, preamble);
        
        return validNumbers.contains(numbers.get(index));
    }

    protected Set<Long> calculateValidNumbers(List<Long> numbers, int indexToTest, int preamble) {
        Set<Long> validNumbers = new HashSet<>();
        int from = indexToTest - preamble;
        for (int i = from; i < indexToTest; i++) {
            for (int j = i + 1; j < indexToTest ; j++) {
                validNumbers.add(numbers.get(i) + numbers.get(j));
            }
        }
        return validNumbers;
    }

    protected long getEncryptionWeakness(List<Long> numbers, long invalidNumber) {
        int from = 0;
        int to;
        for (; from < numbers.size() - 1; from++) {
            for (to = from + 1; to < numbers.size(); to++) {
                List<Long> preambleNumbers = numbers.subList(from, to);
                long total = preambleNumbers.stream().mapToLong(l -> l).sum();
                if(total == invalidNumber){
                    log.info("Found valid configuration to reach {}: from: {} to: {}", invalidNumber, from, to);
                    return preambleNumbers.stream()
                        .mapToLong(l -> l)
                        .min()
                        .orElseThrow(() -> new UnsupportedOperationException("There is no min value for preamble")) +
                        preambleNumbers.stream()
                            .mapToLong(l -> l)
                            .max()
                            .orElseThrow(() -> new UnsupportedOperationException("There is no max value for preamble"));
                }else if(total > invalidNumber){
                    break;
                }
            }
        }
        throw new IllegalArgumentException("No valid preamble");
    }
}
