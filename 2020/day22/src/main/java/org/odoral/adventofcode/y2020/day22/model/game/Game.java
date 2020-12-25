package org.odoral.adventofcode.y2020.day22.model.game;

import org.odoral.adventofcode.y2020.day22.exception.CrabCombatException;
import org.odoral.adventofcode.y2020.day22.model.player.Player;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public interface Game {
    
    List<Player> playRound(List<Player> players);
    
    default List<Player> playGame(List<Player> players) {
        while (thereIsNoWinner(players)){
            players = playRound(players);
        }
        return players;
    }

    default boolean thereIsNoWinner(List<Player> players) {
        return players.stream()
            .noneMatch(player -> player.startingDeck.isEmpty());
    }

    default Player getWinner(List<Player> players) {
        return players.stream()
            .filter(player -> !player.startingDeck.isEmpty())
            .findAny()
            .orElseThrow(() -> new CrabCombatException("There is no winner yet: "+players));
    }

    default long getWinnerScore(Player player) {
        AtomicLong multiplier = new AtomicLong(player.startingDeck.size());

        return player.startingDeck.stream()
            .mapToLong(card -> card * multiplier.getAndDecrement())
            .sum();
    }

}
