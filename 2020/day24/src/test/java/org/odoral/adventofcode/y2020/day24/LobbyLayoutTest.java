package org.odoral.adventofcode.y2020.day24;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.y2020.day24.model.Directions;
import org.odoral.adventofcode.y2020.day24.model.Tile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LobbyLayoutTest {

    protected LobbyLayout lobbyLayout;

    @Before public void setUp() {
        lobbyLayout = new LobbyLayout();
    }
    
    @Test public void testTileNeighbours(){
        Tile tile = new Tile(0, 0);
        List<Tile> neighbourTiles = tile.neighbourTiles();
        
        neighbourTiles.forEach(neighbourTile -> {
            assertEquals(2L, neighbourTile.neighbourTiles()
                .stream()
            .filter(neighbourTiles::contains)
            .count());
            assertTrue(neighbourTile.neighbourTiles().contains(tile));
        });

        Tile westTile = new Tile(tile.getX() + Directions.W.getX(), tile.getY() + Directions.W.getY());
        
        Set<Tile> neighbours = Stream.of(tile, westTile)
            .flatMap(t -> t.neighbourTiles().stream())
            .collect(Collectors.toSet());
        assertEquals(10, neighbours.size());
    }
    
    @Test public void test() throws IOException {
        List<String> tileIds = lobbyLayout.loadTileId("/scenario1.txt");
        assertEquals(20, tileIds.size());
        
        Map<Tile, Boolean> lobbyTiles = new HashMap<>();
        lobbyTiles = lobbyLayout.flipTiles(tileIds, lobbyTiles);
        long blackTiles = lobbyLayout.count(lobbyTiles, true);
        assertEquals(10, blackTiles);

        testDailyTileFlips(lobbyTiles, 1, new int[]{15, 12, 25, 14, 23, 28, 41, 37, 49, 37});
        testDailyTileFlips(lobbyTiles, 10, new int[]{132, 259, 406, 566, 788, 1106, 1373, 1844, 2208});
    }

    protected void testDailyTileFlips(Map<Tile, Boolean> lobbyTiles, int dailyIncrement, int[] expectedBlackTiles) {
        for (int expectedBlackTile : expectedBlackTiles) {
            lobbyTiles = lobbyLayout.flipTilesAccordingToRules(lobbyTiles, dailyIncrement);
            assertEquals(expectedBlackTile, lobbyLayout.count(lobbyTiles, true));
        }
    }
}