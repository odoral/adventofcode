package org.odoral.adventofcode.y2022.day2;

import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.exception.AdventOfCodeException;

import java.io.IOException;
import java.util.List;
import java.util.function.ToLongFunction;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RockPaperScissors {

    public enum Option {
        ROCK(1),
        PAPER(2),
        SCISSORS(3);

        private final int points;

        Option(int points) {
            this.points = points;
        }

        public int getPoints() {
            return points;
        }

        public boolean win(Option other) {
            return (ordinal() + 2) % 3 == other.ordinal();
        }

        public boolean draw(Option other) {
            return this.equals(other);
        }

        public boolean lose(Option other) {
            return !draw(other) && !win(other);
        }

        public static Option map(String option) {
            switch (option) {
                case "A":
                case "X":
                    return ROCK;
                case "B":
                case "Y":
                    return PAPER;
                case "C":
                case "Z":
                    return SCISSORS;
                default:
                    throw new AdventOfCodeException("Unsupported option: " + option);

            }
        }
    }

    public static final String NEED_TO_LOSE = "X";
    public static final String NEED_TO_DRAW = "Y";
    public static final String NEED_TO_WIN = "Z";

    public static final long DRAW_RESULT = 3L;
    public static final long WIN_RESULT = 6L;
    public static final long LOSE_RESULT = 0L;

    public static void main(String[] args) throws IOException {
        RockPaperScissors rockPaperScissors = new RockPaperScissors();
        List<Round> rounds = CommonUtils.loadResource("/input.txt", Round::map);
        Result result = rockPaperScissors.getTotalScore(rounds, rockPaperScissors::getRoundScoreScenario1);
        log.info("Total score scenario1: {}", result.totalScore);

        result = rockPaperScissors.getTotalScore(rounds, rockPaperScissors::getRoundScoreScenario2);
        log.info("Total score scenario2: {}", result.totalScore);
    }

    protected Result getTotalScore(List<Round> rounds, ToLongFunction<Round> roundToLongFunction) {
        return new Result(rounds.stream()
            .mapToLong(roundToLongFunction)
            .sum());
    }

    protected long getRoundScoreScenario1(Round round) {
        Option me = Option.map(round.me);
        Option opponent = Option.map(round.opponent);
        long totalScore = me.points;

        if (me.win(opponent)) {
            totalScore += WIN_RESULT;
        } else if (me.draw(opponent)) {
            totalScore += DRAW_RESULT;
        }

        return totalScore;
    }

    protected long getRoundScoreScenario2(Round round) {
        long totalScore = 0;

        switch (round.me) {
            case NEED_TO_WIN:
                totalScore += WIN_RESULT;
                totalScore += getOptionToWin(round.opponent).getPoints();
                break;
            case NEED_TO_DRAW:
                totalScore += DRAW_RESULT;
                totalScore += Option.map(round.opponent).getPoints();
                break;
            case NEED_TO_LOSE:
                totalScore += LOSE_RESULT;
                totalScore += getOptionToLose(round.opponent).getPoints();
                break;
            default:
                throw new AdventOfCodeException("Unsupported option: " + round.me);
        }

        return totalScore;
    }

    protected Option getOptionToWin(String opponent) {
        return Option.values()[(Option.map(opponent).ordinal() + 1) % 3];
    }

    protected Option getOptionToLose(String opponent) {
        return Option.values()[(Option.map(opponent).ordinal() + 2) % 3];
    }

    public static class Round {
        String opponent;
        String me;

        public static Round map(String line) {
            Round round = new Round();
            String[] fields = line.split(" ");
            round.opponent = fields[0];
            round.me = fields[1];
            return round;
        }
    }

    public static class Result {
        final long totalScore;

        public Result(long totalScore) {
            this.totalScore = totalScore;
        }

    }
}