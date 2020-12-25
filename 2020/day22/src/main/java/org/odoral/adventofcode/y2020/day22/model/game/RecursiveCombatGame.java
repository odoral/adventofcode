package org.odoral.adventofcode.y2020.day22.model.game;

import org.odoral.adventofcode.y2020.day22.model.player.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RecursiveCombatGame implements Game {
    
    protected static final Set<String> PLAYED_ROUNDS = new HashSet<>();
    protected static final Game basicGame = new BasicCombatGame();

    @Override
    public List<Player> playGame(List<Player> players) {
        PLAYED_ROUNDS.clear();
        return Game.super.playGame(players);
    }

    @Override
    public List<Player> playRound(List<Player> players) {
        String roundFootprint = getRoundFootprint(players);
        if(PLAYED_ROUNDS.contains(roundFootprint)){
            log.info("Repeated round, player 1 wins!!!");
            Player playerOne = players.get(0);
            players.forEach(player -> {
                playerOne.startingDeck.addAll(player.startingDeck);
                player.startingDeck.clear();
            });
            return players;
        }
        PLAYED_ROUNDS.add(roundFootprint);
        
        if(haveToPlayRecursiveCombat(players)){
            return playRecursiveCombatRound(players);
        }else{
            return basicGame.playRound(players);
        }
    }

    protected boolean haveToPlayRecursiveCombat(List<Player> players) {
        return players.stream()
            .allMatch(player -> player.startingDeck.size() >= player.startingDeck.getFirst() + 1);
    }

    protected List<Player> playRecursiveCombatRound(List<Player> players) {
        List<Player> recursiveCombatPlayers = players.stream()
            .map(Player::getPlayerForRecursiveCombat)
            .collect(Collectors.toList());
        Set<String> previousPlayedRoundsForUpperGame = new HashSet<>(PLAYED_ROUNDS);
        
        Integer [] cards = players.stream()
            .map(player -> player.startingDeck.removeFirst())
            .toArray(Integer[]::new);

        recursiveCombatPlayers = playGame(recursiveCombatPlayers);
        
        int winner = -1;
        for (int i = 0; i < recursiveCombatPlayers.size(); i++) {
            if(!recursiveCombatPlayers.get(i).startingDeck.isEmpty()){
                winner = i;
            }
        }

        Player winnerPlayer = players.get(winner);
        log.debug("{} wins", winnerPlayer.name);

        for (int i = 0; i < cards.length; i++) {
            winnerPlayer.startingDeck.addLast(cards[(i + winner) % cards.length]);
        }
        
        PLAYED_ROUNDS.clear();
        PLAYED_ROUNDS.addAll(previousPlayedRoundsForUpperGame);
        return players;
    }

    protected String getRoundFootprint(List<Player> players){
        return players.stream()
            .map(this::getPlayerDeckFootprint)
            .collect(Collectors.joining("_"));
    }

    protected String getPlayerDeckFootprint(Player player) {
        return player.startingDeck.stream()
            .map(String::valueOf)
            .collect(Collectors.joining(","));
    }
}
