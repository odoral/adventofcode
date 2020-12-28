package org.odoral.adventofcode.y2020.day25;

import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.y2020.day25.exception.ComboBreakerException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ComboBreaker {

    public static final long DIVIDER = 20201227L;

    public static void main(String[] args) throws IOException {
        ComboBreaker comboBreaker = new ComboBreaker();
        List<Long> publicKeys = comboBreaker.loadPublicKeys("/input.txt");
        List<Long> loopSizes = comboBreaker.calculateLoopSizes(publicKeys, 7L);

        Long encryptionKey = comboBreaker.calculateEncryptionKey(publicKeys, loopSizes);
        log.info("Encryption key is: {}", encryptionKey);
    }

    public List<Long> loadPublicKeys(String resource) throws IOException {
        return CommonUtils.loadResource(resource, Long::parseLong);
    }

    public List<Long> calculateLoopSizes(List<Long> publicKeys, long subjectNumber) {
        Long [] loopSizes = new Long[publicKeys.size()];
        
        long currentPublicKey = 1;
        long loop = 0;
        while(thereArePendingLoopSizes(loopSizes)){
            currentPublicKey = (currentPublicKey * subjectNumber) % DIVIDER;
            loop++;

            int publicKeyIndex;
            if((publicKeyIndex = publicKeys.indexOf(currentPublicKey)) != -1){
                loopSizes[publicKeyIndex] = loop;
            }
        }
        
        return Arrays.asList(loopSizes);
    }

    protected boolean thereArePendingLoopSizes(Long[] loopSizes) {
        return Stream.of(loopSizes)
            .anyMatch(Objects::isNull);
    }

    public Long calculateEncryptionKey(List<Long> publicKeys, List<Long> loopSizes) {
        Set<Long> encryptionKeys = new HashSet<>();
        for (int i = 0; i < publicKeys.size(); i++) {
            Long publicKey = publicKeys.get(i);
            Long loopSize = loopSizes.get((i + 1) % loopSizes.size());
            long encryptionKey = calculateEncryptionKey(publicKey, loopSize);
            log.info("Encryption key for {} / {} => {}", publicKey, loopSize, encryptionKey);
            encryptionKeys.add(encryptionKey);
        }
        if(encryptionKeys.size() > 1){
            throw new ComboBreakerException("Encryption keys are wrong: "+encryptionKeys);
        }
        return encryptionKeys.iterator().next();
    }

    protected long calculateEncryptionKey(Long subjectNumber, Long loopSize) {
        long result = 1;
        for (long i = 0; i < loopSize; i++) {
            result *= subjectNumber;
            result = result % DIVIDER;
        }
        return result;
    }
}
