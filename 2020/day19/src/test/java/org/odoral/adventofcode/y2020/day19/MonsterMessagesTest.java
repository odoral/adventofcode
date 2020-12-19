package org.odoral.adventofcode.y2020.day19;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Slf4j
public class MonsterMessagesTest {

    protected MonsterMessages monsterMessages;

    @Before public void setUp() {
        monsterMessages = new MonsterMessages();
    }
    
    @Test public void test_scenario1() throws IOException {
        MonsterMessages.InputData inputData = monsterMessages.loadInputData("/scenario1.txt");
        List<String> filteredValidMessages = monsterMessages.filterValidMessages(inputData, 0);
        assertEquals(2, filteredValidMessages.size());
    }
    
    @Test public void test_scenario2() throws IOException {
        MonsterMessages.InputData inputData = monsterMessages.loadInputData("/scenario2.txt");
        List<String> filteredValidMessages = monsterMessages.filterValidMessages(inputData, 0);
        log.info("Valid filtered messages: {}", filteredValidMessages.size());
        assertEquals(filteredValidMessages.toString(), 3, filteredValidMessages.size());
        assertTrue(filteredValidMessages.contains("bbabbbbaabaabba"));
        assertTrue(filteredValidMessages.contains("ababaaaaaabaaab"));
        assertTrue(filteredValidMessages.contains("ababaaaaabbbaba"));
        
        inputData.loadNewRule("8: 42 | 42 8");
        inputData.loadNewRule("11: 42 31 | 42 11 31");
        
        filteredValidMessages = monsterMessages.filterValidMessages(inputData, 0);
        log.info("Valid filtered messages: {}", filteredValidMessages.size());
        assertEquals(filteredValidMessages.toString(), 12, filteredValidMessages.size());
        assertTrue(filteredValidMessages.contains("bbabbbbaabaabba"));
        assertTrue(filteredValidMessages.contains("babbbbaabbbbbabbbbbbaabaaabaaa"));
        assertTrue(filteredValidMessages.contains("aaabbbbbbaaaabaababaabababbabaaabbababababaaa"));
        assertTrue(filteredValidMessages.contains("bbbbbbbaaaabbbbaaabbabaaa"));
        assertTrue(filteredValidMessages.contains("bbbababbbbaaaaaaaabbababaaababaabab"));
        assertTrue(filteredValidMessages.contains("ababaaaaaabaaab"));
        assertTrue(filteredValidMessages.contains("ababaaaaabbbaba"));
        assertTrue(filteredValidMessages.contains("baabbaaaabbaaaababbaababb"));
        assertTrue(filteredValidMessages.contains("abbbbabbbbaaaababbbbbbaaaababb"));
        assertTrue(filteredValidMessages.contains("aaaaabbaabaaaaababaa"));
        assertTrue(filteredValidMessages.contains("aaaabbaabbaaaaaaabbbabbbaaabbaabaaa"));
        assertTrue(filteredValidMessages.contains("aabbbbbaabbbaaaaaabbbbbababaaaaabbaaabba"));
    }
}