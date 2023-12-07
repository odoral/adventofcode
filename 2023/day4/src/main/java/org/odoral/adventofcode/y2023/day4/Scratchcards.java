package org.odoral.adventofcode.y2023.day4;

import org.apache.commons.lang3.StringUtils;
import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Scratchcards {

    public static void main(String[] args) throws IOException {
        Scratchcards scratchcards = new Scratchcards();
        List<Card> cards = CommonUtils.loadResource("/input.txt", Card::parse);
        Result result = scratchcards.calculateTotalPoints(cards);
        log.info("Total points: {}", result.points);
        log.info("Total cards: {}", result.cardCount);
    }

    protected Result calculateTotalPoints(List<Card> cards) {
        return new Result(cards.stream()
            .mapToLong(card -> {
                List<Integer> matchingNumbers = matchingNumbers(card);
                long cardPoints = calculateCardPoints(matchingNumbers);
                getCardCopies(cards, card, matchingNumbers.size());
                return cardPoints;
            })
            .sum(),
            cards.stream().mapToLong(card -> card.instancies.get()).sum());
    }

    protected void getCardCopies(List<Card> cards, Card card, int cardsWon) {
        log.info("Card {} won {} cards", card.number, cardsWon);
        for (int pointIndex = 0; pointIndex < cardsWon && card.number + pointIndex < cards.size(); pointIndex++) {
            cards.get(card.number + pointIndex).instancies.addAndGet(card.instancies.get());
        }
    }

    protected List<Integer> matchingNumbers(Card card) {
        return card.winningNumbers.stream()
            .filter(card.numbersYouHave::contains)
            .collect(Collectors.toList());
    }

    protected long calculateCardPoints(List<Integer> integers) {
        if (integers.isEmpty()) {
            return 0;
        } else {
            return BigInteger.ONE.add(BigInteger.ONE).pow(integers.size() - 1).longValue();
        }
    }

    public static class Result {
        final Long points;
        final Long cardCount;

        public Result(Long points, long cardCount) {
            this.points = points;
            this.cardCount = cardCount;
        }
    }

    public static class Card {
        final int number;
        final AtomicInteger instancies;
        final List<Integer> winningNumbers;
        final List<Integer> numbersYouHave;

        private Card(int number, List<Integer> winningNumbers, List<Integer> numbersYouHave) {
            this.number = number;
            this.instancies = new AtomicInteger(1);
            this.winningNumbers = winningNumbers;
            this.numbersYouHave = numbersYouHave;
        }

        public static Card parse(String cardConfiguration) {
            String[] cardFields = cardConfiguration.split(":");
            String[] numberFields = cardFields[1].split("\\|");
            return new Card(
                Integer.parseInt(cardFields[0].split("\\s+")[1]),
                Arrays.stream(CommonUtils.toIntegerArray(StringUtils.trim(numberFields[0]), "\\s+")).collect(Collectors.toList()),
                Arrays.stream(CommonUtils.toIntegerArray(StringUtils.trim(numberFields[1]), "\\s+")).collect(Collectors.toList())
            );
        }
    }
}
