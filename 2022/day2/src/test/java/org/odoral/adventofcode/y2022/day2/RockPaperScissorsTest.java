package org.odoral.adventofcode.y2022.day2;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RockPaperScissorsTest {

    protected RockPaperScissors rockPaperScissors;

    @Before public void setUp() {
        rockPaperScissors = new RockPaperScissors();
    }

    @Test public void test_scenario1() throws IOException {
        RockPaperScissors rockPaperScissors = new RockPaperScissors();

        assertEquals(8L, rockPaperScissors.getRoundScoreScenario1(RockPaperScissors.Round.map("A Y")));
        assertEquals(1L, rockPaperScissors.getRoundScoreScenario1(RockPaperScissors.Round.map("B X")));
        assertEquals(6L, rockPaperScissors.getRoundScoreScenario1(RockPaperScissors.Round.map("C Z")));

        List<RockPaperScissors.Round> rounds = CommonUtils.loadResource("/input.txt", RockPaperScissors.Round::map);
        RockPaperScissors.Result result = rockPaperScissors.getTotalScore(rounds, rockPaperScissors::getRoundScoreScenario1);
        assertEquals(15L, result.totalScore);
    }

    @Test public void test_scenario2() throws IOException {
        RockPaperScissors rockPaperScissors = new RockPaperScissors();

        assertEquals(4L, rockPaperScissors.getRoundScoreScenario2(RockPaperScissors.Round.map("A Y")));
        assertEquals(1L, rockPaperScissors.getRoundScoreScenario2(RockPaperScissors.Round.map("B X")));
        assertEquals(7L, rockPaperScissors.getRoundScoreScenario2(RockPaperScissors.Round.map("C Z")));

        List<RockPaperScissors.Round> rounds = CommonUtils.loadResource("/input.txt", RockPaperScissors.Round::map);
        RockPaperScissors.Result result = rockPaperScissors.getTotalScore(rounds, rockPaperScissors::getRoundScoreScenario2);
        assertEquals(12L, result.totalScore);
    }

    @Test public void testOptionWin() {
        assertTrue(RockPaperScissors.Option.ROCK.win(RockPaperScissors.Option.SCISSORS));
        assertTrue(RockPaperScissors.Option.PAPER.win(RockPaperScissors.Option.ROCK));
        assertTrue(RockPaperScissors.Option.SCISSORS.win(RockPaperScissors.Option.PAPER));

        assertFalse(RockPaperScissors.Option.ROCK.win(RockPaperScissors.Option.PAPER));
        assertFalse(RockPaperScissors.Option.ROCK.win(RockPaperScissors.Option.ROCK));
        assertFalse(RockPaperScissors.Option.PAPER.win(RockPaperScissors.Option.SCISSORS));
        assertFalse(RockPaperScissors.Option.PAPER.win(RockPaperScissors.Option.PAPER));
        assertFalse(RockPaperScissors.Option.SCISSORS.win(RockPaperScissors.Option.ROCK));
        assertFalse(RockPaperScissors.Option.SCISSORS.win(RockPaperScissors.Option.SCISSORS));
    }

    @Test public void testOptionDraw() {
        assertTrue(RockPaperScissors.Option.ROCK.draw(RockPaperScissors.Option.ROCK));
        assertTrue(RockPaperScissors.Option.PAPER.draw(RockPaperScissors.Option.PAPER));
        assertTrue(RockPaperScissors.Option.SCISSORS.draw(RockPaperScissors.Option.SCISSORS));

        assertFalse(RockPaperScissors.Option.ROCK.draw(RockPaperScissors.Option.PAPER));
        assertFalse(RockPaperScissors.Option.ROCK.draw(RockPaperScissors.Option.SCISSORS));
        assertFalse(RockPaperScissors.Option.PAPER.draw(RockPaperScissors.Option.SCISSORS));
        assertFalse(RockPaperScissors.Option.PAPER.draw(RockPaperScissors.Option.ROCK));
        assertFalse(RockPaperScissors.Option.SCISSORS.draw(RockPaperScissors.Option.ROCK));
        assertFalse(RockPaperScissors.Option.SCISSORS.draw(RockPaperScissors.Option.PAPER));
    }

    @Test public void testOptionLose() {
        assertTrue(RockPaperScissors.Option.ROCK.lose(RockPaperScissors.Option.PAPER));
        assertTrue(RockPaperScissors.Option.PAPER.lose(RockPaperScissors.Option.SCISSORS));
        assertTrue(RockPaperScissors.Option.SCISSORS.lose(RockPaperScissors.Option.ROCK));

        assertFalse(RockPaperScissors.Option.ROCK.lose(RockPaperScissors.Option.ROCK));
        assertFalse(RockPaperScissors.Option.ROCK.lose(RockPaperScissors.Option.SCISSORS));
        assertFalse(RockPaperScissors.Option.PAPER.lose(RockPaperScissors.Option.PAPER));
        assertFalse(RockPaperScissors.Option.PAPER.lose(RockPaperScissors.Option.ROCK));
        assertFalse(RockPaperScissors.Option.SCISSORS.lose(RockPaperScissors.Option.SCISSORS));
        assertFalse(RockPaperScissors.Option.SCISSORS.lose(RockPaperScissors.Option.PAPER));
    }

}