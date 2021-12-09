package org.odoral.adventofcode.y2021.day4;

import org.apache.commons.lang3.StringUtils;
import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GiantSquid {

    public static void main(String[] args) throws IOException {
        GiantSquid giantSquid = new GiantSquid();
        List<String> numbersAndBoards = CommonUtils.loadResource("/input.txt", Function.identity());
        Result result = giantSquid.playBingo(numbersAndBoards);
        log.info("Winner is: ({},{})={}", result.board, result.lastNumber, result.getScenarioResult());

        result = giantSquid.findLooser(numbersAndBoards);
        log.info("Looser is: ({},{})={}", result.board, result.lastNumber, result.getScenarioResult());
    }

    protected Result playBingo(List<String> numbersAndBoards) {
        Integer[] numbers = readNumbers(numbersAndBoards);
        List<Board> boards = readBoards(numbersAndBoards);

        for (Integer lastNumber : numbers) {
            boards.forEach(board -> board.markNumber(lastNumber));
            Optional<Board> anyWinner = boards.stream()
                .filter(Board::isBingo)
                .findAny();

            if (anyWinner.isPresent()) {
                return new Result(anyWinner.get(), lastNumber);
            }
        }

        throw new IllegalStateException("No winner");
    }

    protected Result findLooser(List<String> numbersAndBoards) {
        Integer[] numbers = readNumbers(numbersAndBoards);
        List<Board> boards = readBoards(numbersAndBoards);

        for (Integer lastNumber : numbers) {
            boards.forEach(board -> board.markNumber(lastNumber));
            List<Board> looserBoards = boards.stream()
                .filter(board -> !board.isBingo())
                .collect(Collectors.toList());

            if (boards.size() == 1 && looserBoards.isEmpty()) {
                return new Result(boards.get(0), lastNumber);
            }

            boards = looserBoards;
        }

        throw new IllegalStateException("No winner");
    }

    protected Integer[] readNumbers(List<String> numbersAndBoards) {
        return CommonUtils.toIntegerArray(numbersAndBoards.get(0), ",");
    }

    protected List<Board> readBoards(List<String> numbersAndBoards) {
        List<Board> boards = new ArrayList<>();
        Board currentBoard = null;
        int row = 0;

        for (int i = 1; i < numbersAndBoards.size(); i++) {
            String line = numbersAndBoards.get(i);
            if (StringUtils.isBlank(line)) {
                currentBoard = new Board(5);
                boards.add(currentBoard);
                row = 0;
            } else {
                currentBoard.boardNumbers[row++] = CommonUtils.toIntegerArray(StringUtils.normalizeSpace(line), " ");
            }
        }

        return boards;
    }

    public static class Result {
        final Board board;
        final int lastNumber;

        public Result(Board board, int lastNumber) {
            this.board = board;
            this.lastNumber = lastNumber;
        }

        public int getScenarioResult() {
            return lastNumber * board.sumBoardNumbers();
        }

    }

    public static class Board {
        final Integer[][] boardNumbers;

        public Board(int rows) {
            this.boardNumbers = new Integer[rows][];
        }

        public int sumBoardNumbers() {
            return Arrays.stream(boardNumbers)
                .flatMap(Stream::of)
                .filter(Objects::nonNull)
                .mapToInt(i -> i)
                .sum();
        }

        public void markNumber(Integer number) {
            for (int rowIndex = 0; rowIndex < boardNumbers.length; rowIndex++) {
                for (int columnIndex = 0; columnIndex < boardNumbers.length; columnIndex++) {
                    if (number.equals(boardNumbers[rowIndex][columnIndex])) {
                        boardNumbers[rowIndex][columnIndex] = null;
                        log.debug("{} marked", number);
                    }
                }
            }
        }

        public boolean isBingo() {
            Boolean[] bingoInRow = new Boolean[boardNumbers.length];
            Boolean[] bingoInColumn = new Boolean[boardNumbers.length];
            Arrays.fill(bingoInRow, true);
            Arrays.fill(bingoInColumn, true);

            for (int rowIndex = 0; rowIndex < boardNumbers.length; rowIndex++) {
                for (int columnIndex = 0; columnIndex < boardNumbers[rowIndex].length; columnIndex++) {
                    if (boardNumbers[rowIndex][columnIndex] != null) {
                        bingoInRow[rowIndex] = false;
                        bingoInColumn[columnIndex] = false;
                    }
                }
            }

            return Stream.of(bingoInRow, bingoInColumn)
                .flatMap(Stream::of)
                .anyMatch(r -> r);
        }
    }
}