package org.odoral.adventofcode.y2020.day22;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.y2020.day22.exception.CrabCombatException;
import org.odoral.adventofcode.y2020.day22.model.game.BasicCombatGame;
import org.odoral.adventofcode.y2020.day22.model.game.Game;
import org.odoral.adventofcode.y2020.day22.model.game.RecursiveCombatGame;
import org.odoral.adventofcode.y2020.day22.model.player.Player;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class CrabCombatTest {

    public static final String PLAYER_1 = "Player 1";
    public static final String PLAYER_2 = "Player 2";
    
    protected CrabCombat crabCombat;

    @Before public void setUp() {
        crabCombat = new CrabCombat();
    }
    
    @Test public void test_basicCombatGame() throws IOException {
        List<Player> players = crabCombat.loadPlayers("/scenario1.txt");
        assertEquals(2, players.size());
        assertEquals(5, players.get(0).startingDeck.size());
        assertEquals(5, players.get(1).startingDeck.size());
        
        Game game = new BasicCombatGame();
        players = game.playRound(players);
        assertArrayEquals(
            new Integer []{2, 6, 3, 1, 9, 5},
            filterPlayer(players, PLAYER_1).startingDeck.toArray(new Integer[0]));
        assertArrayEquals(
            new Integer []{8, 4, 7, 10},
            filterPlayer(players, PLAYER_2).startingDeck.toArray(new Integer[0]));

        players = game.playGame(players);
        assertArrayEquals(
            new Integer []{},
            filterPlayer(players, PLAYER_1).startingDeck.toArray(new Integer[0]));
        assertArrayEquals(
            new Integer []{3, 2, 10, 6, 8, 5, 9, 4, 7, 1},
            filterPlayer(players, PLAYER_2).startingDeck.toArray(new Integer[0]));

        Player player = game.getWinner(players);
        assertEquals(PLAYER_2, player.name);
        long winnerScore = game.getWinnerScore(player);
        assertEquals(306, winnerScore);
    }

    @Test public void test_recursiveCombatGame() throws IOException {
        List<Player> players = crabCombat.loadPlayers("/infinite_loop_scenario.txt");
        Game game = new RecursiveCombatGame();
        players = game.playGame(players);
        assertEquals(PLAYER_1, game.getWinner(players).name);

        players = crabCombat.loadPlayers("/scenario1.txt");
        players = game.playGame(players);
        Player player = game.getWinner(players);
        assertEquals(PLAYER_2, player.name);
        long winnerScore = game.getWinnerScore(player);
        assertEquals(291, winnerScore);
    }

    protected Player filterPlayer(List<Player> players, String playerName) {
        return players.stream()
            .filter(player -> player.name.equals(playerName))
            .findAny()
            .orElseThrow(() -> new CrabCombatException("No player with name: "+playerName));
    }
}