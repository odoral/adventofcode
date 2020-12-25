package org.odoral.adventofcode.y2020.day22.model.game;

import org.odoral.adventofcode.y2020.day22.model.player.Player;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BasicCombatGame implements Game {
    
    @Override
    public List<Player> playRound(List<Player> players) {
        Integer [] cards = players.stream()
            .map(player -> player.startingDeck.removeFirst())
            .toArray(Integer[]::new);
        int winner = 0;
        int winnerCard = -1;
        for (int i = 0; i < cards.length; i++) {
            if(cards[i] > winnerCard){
                winnerCard = cards[i];
                winner = i;
            }
        }
        Player winnerPlayer = players.get(winner);
        log.debug("{} wins", winnerPlayer.name);

        for (int i = 0; i < cards.length; i++) {
            winnerPlayer.startingDeck.addLast(cards[(i + winner) % cards.length]);
        }

        return players;
    }
    
}
