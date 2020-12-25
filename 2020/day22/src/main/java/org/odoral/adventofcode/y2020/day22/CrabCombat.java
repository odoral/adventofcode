package org.odoral.adventofcode.y2020.day22;

import org.apache.commons.lang3.StringUtils;
import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.y2020.day22.model.game.BasicCombatGame;
import org.odoral.adventofcode.y2020.day22.model.game.Game;
import org.odoral.adventofcode.y2020.day22.model.game.RecursiveCombatGame;
import org.odoral.adventofcode.y2020.day22.model.player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CrabCombat {

    public static void main(String[] args) throws IOException {
        CrabCombat crabCombat = new CrabCombat();
        List<Player> players = crabCombat.loadPlayers("/input.txt");
        Game game = new BasicCombatGame();
        players = game.playGame(players);
        Player player = game.getWinner(players);
        long winnerScore = game.getWinnerScore(player);
        log.info("{} wins!! score: {}", player.name, winnerScore);
        
        players = crabCombat.loadPlayers("/input.txt");
        game = new RecursiveCombatGame();
        players = game.playGame(players);
        player = game.getWinner(players);
        winnerScore = game.getWinnerScore(player);
        log.info("{} wins recursive combat!! score: {}", player.name, winnerScore);
    }

    public List<Player> loadPlayers(String resource) throws IOException {
        List<Player> players = new ArrayList<>();
        CommonUtils.loadResource(resource, line -> {
            if(line.startsWith("Player")){
                players.add(new Player(line.split(":")[0]));
            }else if(StringUtils.isNotBlank(line)){
                Player player = players.get(players.size() - 1);
                player.startingDeck.addLast(Integer.parseInt(line));
            }
            return null;
        });
        return players;
    }

}
