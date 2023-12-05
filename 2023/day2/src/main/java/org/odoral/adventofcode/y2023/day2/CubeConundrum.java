package org.odoral.adventofcode.y2023.day2;

import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.exception.AdventOfCodeException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CubeConundrum {

    public static void main(String[] args) throws IOException {
        CubeConundrum cubeConundrum = new CubeConundrum();
        List<Game> games = CommonUtils.loadResource("/input.txt", Game::from);
        Result result = cubeConundrum.getIDSumOfValidGames(games);
        log.info("There are {} valid games.\nSum: {}", result.validGames.size(), result.idSum);

        result = cubeConundrum.getPowerOfMinimunSetOfCubes(games);
        log.info("There are {} valid games.\nSum of the power: {}", result.validGames.size(), result.idSum);
    }

    protected Result getIDSumOfValidGames(List<Game> games) {
        List<Game> validGames = games.stream()
            .filter(this::isValidGame)
            .collect(Collectors.toList());

        return new Result(validGames, validGames.stream().mapToLong(game -> game.number).sum());
    }

    protected boolean isValidGame(Game game) {
        return game.sets.stream().noneMatch(this::isInvalidSet);
    }

    protected boolean isInvalidSet(Set set) {
        return set.cubes.stream().anyMatch(this::isInvalidCubeNumber);
    }

    protected boolean isInvalidCubeNumber(Cube cube) {
        switch (cube.color) {
            case "red":
                return cube.cubeCount > 12;
            case "green":
                return cube.cubeCount > 13;
            case "blue":
                return cube.cubeCount > 14;
            default:
                throw new AdventOfCodeException(cube.color + " is not expected.");
        }
    }

    protected Result getPowerOfMinimunSetOfCubes(List<Game> games) {
        return new Result(games, games.stream()
            .mapToLong(this::toPowerOfSets)
            .sum());
    }

    protected long toPowerOfSets(Game game) {
        AtomicLong redCount = new AtomicLong(1);
        AtomicLong greenCount = new AtomicLong(1);
        AtomicLong blueCount = new AtomicLong(1);

        game.sets.forEach(set -> {
            getCubeColorCount(set, "red").filter(count -> redCount.get() < count).ifPresent(redCount::set);
            getCubeColorCount(set, "green").filter(count -> greenCount.get() < count).ifPresent(greenCount::set);
            getCubeColorCount(set, "blue").filter(count -> blueCount.get() < count).ifPresent(blueCount::set);
        });

        return redCount.get() * greenCount.get() * blueCount.get();
    }

    protected Optional<Integer> getCubeColorCount(Set set, String color) {
        return set.cubes.stream()
            .filter(cube -> cube.color.equals(color))
            .map(cube -> cube.cubeCount)
            .findFirst();
    }

    public static class Result {
        final List<Game> validGames;
        final Long idSum;

        public Result(List<Game> validGames, Long idSum) {
            this.validGames = validGames;
            this.idSum = idSum;
        }
    }

    @ToString
    public static class Game {
        final int number;
        final List<Set> sets;

        private Game(int number, List<Set> sets) {
            this.number = number;
            this.sets = sets;
        }

        public static Game from(String gameConfiguration) {
            String[] gameFields = gameConfiguration.split(":");

            return new Game(
                Integer.parseInt(gameFields[0].split("\\s")[1]),
                Arrays.stream(gameFields[1].split(";"))
                    .map(Set::from)
                    .collect(Collectors.toList())
            );
        }
    }

    @ToString
    protected static class Set {
        final List<Cube> cubes;

        private Set(List<Cube> cubes) {
            this.cubes = cubes;
        }

        public static Set from(String setConfiguration) {
            return new Set(Arrays.stream(setConfiguration.split(","))
                .map(Cube::from)
                .collect(Collectors.toList()));
        }
    }

    @ToString
    protected static class Cube {
        final String color;
        final int cubeCount;

        private Cube(String color, int cubeCount) {
            this.color = color;
            this.cubeCount = cubeCount;
        }

        public static Cube from(String cubeConfiguration) {
            String[] fields = cubeConfiguration.trim().split("\\s");
            return new Cube(fields[1], Integer.parseInt(fields[0]));
        }
    }
}
