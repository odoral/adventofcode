package org.odoral.adventofcode.y2023.day7;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.exception.AdventOfCodeException;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CamelCards {

    protected static boolean considerJokers = false;

    public void setConsiderJokers(boolean considerJokers) {
        CamelCards.considerJokers = considerJokers;

        if (CamelCards.considerJokers) {
            CardValue.VALUES.put('J', 0);
        } else {
            CardValue.VALUES.put('J', 11);
        }
    }

    public static void main(String[] args) throws IOException {
        CamelCards camelCards = new CamelCards();
        List<Hand> hands = CommonUtils.loadResource("/input.txt", Hand::parse);
        Result result = camelCards.getTotalWinnings(hands);
        log.info("Solution: {}", result.totalWinnings);

        camelCards.setConsiderJokers(true);
        result = camelCards.getTotalWinnings(hands);
        log.info("Solution considering jokers: {}", result.totalWinnings);
    }

    public Result getTotalWinnings(List<Hand> hands) {
        AtomicInteger rank = new AtomicInteger(0);

        return new Result(hands.stream()
            .sorted()
            .mapToLong(hand -> hand.bid * rank.incrementAndGet())
            .sum()
        );
    }


    public static class Result {

        final long totalWinnings;

        public Result(long totalWinnings) {
            this.totalWinnings = totalWinnings;
        }

    }

    public static class Hand implements Comparable<Hand> {

        final String cards;
        final long bid;

        private Hand(String cards, long bid) {
            this.cards = cards;
            this.bid = bid;
        }

        public static Hand parse(String handConfiguration) {
            String[] handFields = handConfiguration.split("\\s+");
            return new Hand(handFields[0], Long.parseLong(handFields[1]));
        }

        @Override public int compareTo(Hand other) {
            return new CompareToBuilder()
                .append(
                    HandType.get(considerJokers ? groupCardsUsingJoker(this.cards) : groupCards(this.cards)),
                    HandType.get(considerJokers ? groupCardsUsingJoker(other.cards) : groupCards(other.cards)))
                .append(CardValue.VALUES.get(this.cards.charAt(0)), CardValue.VALUES.get(other.cards.charAt(0)))
                .append(CardValue.VALUES.get(this.cards.charAt(1)), CardValue.VALUES.get(other.cards.charAt(1)))
                .append(CardValue.VALUES.get(this.cards.charAt(2)), CardValue.VALUES.get(other.cards.charAt(2)))
                .append(CardValue.VALUES.get(this.cards.charAt(3)), CardValue.VALUES.get(other.cards.charAt(3)))
                .append(CardValue.VALUES.get(this.cards.charAt(4)), CardValue.VALUES.get(other.cards.charAt(4)))
                .build();
        }

    }

    public static class CardValue {

        static final Map<Character, Integer> VALUES = new HashMap<>();

        static {
            VALUES.put('A', 14);
            VALUES.put('K', 13);
            VALUES.put('Q', 12);
            VALUES.put('J', 11);
            VALUES.put('T', 10);
            VALUES.put('9', 9);
            VALUES.put('8', 8);
            VALUES.put('7', 7);
            VALUES.put('6', 6);
            VALUES.put('5', 5);
            VALUES.put('4', 4);
            VALUES.put('3', 3);
            VALUES.put('2', 2);
        }
    }

    public enum HandType {
        HIGH_CARD("11111"),
        ONE_PAIR("2111"),
        TWO_PAIR("221"),
        THREE_OF_A_KIND("311"),
        FULL_HOUSE("32"),
        FOUR_OF_A_KIND("41"),
        FIVE_OF_A_KIND("5");

        private final String groupedCards;

        HandType(String groupedCards) {
            this.groupedCards = groupedCards;
        }

        public static HandType get(String groupedCards) {
            return Stream.of(HandType.values())
                .filter(handType -> handType.groupedCards.equals(groupedCards))
                .findFirst()
                .orElseThrow(() -> new AdventOfCodeException("Unsupported hand: " + groupedCards));
        }
    }

    private static String groupCards(String cards) {
        return Stream.of(CommonUtils.toCharacterArray(cards))
            .collect(Collectors.toMap(card -> card, card -> 1, Integer::sum))
            .values()
            .stream()
            .sorted(Comparator.reverseOrder())
            .map(String::valueOf)
            .collect(Collectors.joining());
    }

    private static String groupCardsUsingJoker(String cards) {
        long jokers = Stream.of(CommonUtils.toCharacterArray(cards))
            .filter(card -> 'J' == card)
            .count();
        String groupedCards = Stream.of(CommonUtils.toCharacterArray(cards))
            .filter(card -> 'J' != card)
            .collect(Collectors.toMap(card -> card, card -> 1, Integer::sum))
            .values()
            .stream()
            .sorted(Comparator.reverseOrder())
            .map(String::valueOf)
            .collect(Collectors.joining());

        if (groupedCards.isEmpty()) {
            groupedCards = String.valueOf(jokers);
        } else if (jokers > 0) {
            groupedCards = Integer.parseInt(groupedCards.substring(0, 1)) + jokers + groupedCards.substring(1);
        }

        return groupedCards;
    }
}
