package org.odoral.adventofcode.y2020.day25;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.assertEquals;

@Slf4j
public class ComboBreakerTest {

    protected ComboBreaker comboBreaker;

    @Before public void setUp() {
        comboBreaker = new ComboBreaker();
    }
    
    @Test public void test() throws IOException {
        List<Long> publicKeys = comboBreaker.loadPublicKeys("/scenario1.txt");
        List<Long> loopSizes = comboBreaker.calculateLoopSizes(publicKeys, 7L);

        for (int i = 0; i < publicKeys.size(); i++) {
            Long publicKey = publicKeys.get(i);
            Long loopSize = loopSizes.get(i);
            log.info("Loop size for {}: {}", publicKey, loopSize);
        }
        assertEquals(Arrays.asList(8L, 11L), loopSizes);

        Long encryptionKey = comboBreaker.calculateEncryptionKey(publicKeys, loopSizes);
        assertEquals(Long.valueOf(14897079L), encryptionKey);
    }

}