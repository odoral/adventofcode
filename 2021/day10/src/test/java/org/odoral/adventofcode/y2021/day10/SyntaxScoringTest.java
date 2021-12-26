package org.odoral.adventofcode.y2021.day10;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SyntaxScoringTest {

    protected SyntaxScoring syntaxScoring;

    @Before public void setUp() {
        syntaxScoring = new SyntaxScoring();
    }

    @Test public void test_findFirstWrongEnclosingCharacter() {
        Optional<Character> firstWrongEnclosingCharacter = syntaxScoring.findFirstWrongEnclosingCharacter("{([(<{}[<>[]}>{[]{[(<()>");
        assertTrue(firstWrongEnclosingCharacter.isPresent());
        assertEquals('}', firstWrongEnclosingCharacter.get().charValue());
    }

    @Test public void test_scenario1() throws IOException {
        List<String> lines = CommonUtils.loadResource("/input.txt", Function.identity());
        SyntaxScoring.ResultScenario1 resultScenario1 = syntaxScoring.findIncorrectClosingCharacters(lines);
        assertEquals(5, resultScenario1.incorrectClosingCharacters.size());
        assertEquals(26397, resultScenario1.calculateSyntaxErrorScore());
    }

    @Test public void test_completeLine() {
        String ending = syntaxScoring.completeLine("[({(<(())[]>[[{[]{<()<>>");
        assertEquals("}}]])})]", ending);
    }

    @Test public void test_scenario2() throws IOException {
        List<String> lines = CommonUtils.loadResource("/input.txt", Function.identity());
        SyntaxScoring.ResultScenario2 resultScenario2 = syntaxScoring.completeIncompleteLines(lines);
        assertEquals(5, resultScenario2.incompleteLines.size());
        assertEquals(288957, resultScenario2.calculateMiddleScore());
    }

}