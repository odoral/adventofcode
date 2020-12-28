package org.odoral.adventofcode.y2020.day24;

import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.y2020.day24.model.Directions;
import org.odoral.adventofcode.y2020.day24.model.Tile;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LobbyLayout {

    public static void main(String[] args) throws IOException {
        LobbyLayout lobbyLayout = new LobbyLayout();
        List<String> tileIds = lobbyLayout.loadTileId("/input.txt");
        Map<Tile, Boolean> lobbyTiles = new HashMap<>();
        lobbyTiles = lobbyLayout.flipTiles(tileIds, lobbyTiles);
        long blackTiles = lobbyLayout.count(lobbyTiles, true);
        log.info("{} tiles are left with the black side up", blackTiles);

        lobbyTiles = lobbyLayout.flipTilesAccordingToRules(lobbyTiles, 100);
        blackTiles = lobbyLayout.count(lobbyTiles, true);
        log.info("{} tiles are left with the black side up after 100 days", blackTiles);
    }

    public List<String> loadTileId(String resource) throws IOException {
        return CommonUtils.loadResource(resource, Function.identity());
    }

    public Map<Tile, Boolean> flipTiles(List<String> tileIds, Map<Tile, Boolean> lobbyTiles) {
        log.info("Flipping {} tiles", tileIds.size());
        tileIds.stream()
            .map(this::toTile)
            .forEach(tile -> {
                Boolean tileStatus = lobbyTiles.remove(tile);
                if(Objects.isNull(tileStatus)){
                    log.info("Flipping tile {} to black", tile);
                    lobbyTiles.put(tile, true);
                }else{
                    log.info("Flipping tile {} to white", tile);
                }
            });
        return lobbyTiles;
    }

    protected Tile toTile(String tileId) {
        List<Directions> directions = new ArrayList<>();
        
        String auxTileId = tileId;
        while(!auxTileId.isEmpty()){
            Directions[] dir = Directions.values();
            for (Directions value : dir) {
                if (auxTileId.startsWith(value.getShortname())) {
                    directions.add(value);
                    auxTileId = auxTileId.substring(value.getShortname().length());
                }
            }
        }
        
        log.info("{} => {}", tileId, directions);
        int x = 0;
        int y = 0;
        for (Directions direction : directions) {
            x += direction.getX();
            y += direction.getY();
        }
        
        return new Tile(x, y);
    }

    public long count(Map<Tile, Boolean> lobbyTiles, boolean tileIsBlack) {
        return lobbyTiles.values()
            .stream()
            .filter(b -> b == tileIsBlack)
            .count();
    }

    public Map<Tile, Boolean> flipTilesAccordingToRules(Map<Tile, Boolean> lobbyTiles, int iterations) {
        for (int i = 0; i < iterations; i++) {
            lobbyTiles = flipTilesAccordingToRules(lobbyTiles);
        }
        return lobbyTiles;
    }
    
    public Map<Tile, Boolean> flipTilesAccordingToRules(Map<Tile, Boolean> lobbyTiles) {
        List<Tile> tilesToFlipToWhite = new ArrayList<>();
        List<Tile> tilesToFlipToBlack = new ArrayList<>();
        
        lobbyTiles.keySet()
            .stream()
            .flatMap(tile -> Stream.concat(Stream.of(tile), tile.neighbourTiles().stream()))
            .distinct()
            .sorted(Comparator.comparingInt(Tile::getX).thenComparing(Tile::getY))
            .forEach(tile -> {
                boolean isBlackTile = lobbyTiles.getOrDefault(tile, false);
                if(isBlackTile){
                    // Any black tile with zero or more than 2 black tiles immediately adjacent to it is flipped to white.
                    long blackNeighbourTiles = tile.neighbourTiles()
                        .stream()
                        .map(lobbyTiles::get)
                        .filter(Objects::nonNull)
                        .filter(b -> b)
                        .count();
                    if(blackNeighbourTiles == 0 || blackNeighbourTiles > 2){
                        log.info("Flipping black tile: {}", tile);
                        tilesToFlipToWhite.add(tile);
                    }
                }else{
                    // Any white tile with exactly 2 black tiles immediately adjacent to it is flipped to black.
                    long blackNeighbourTiles = tile.neighbourTiles()
                        .stream()
                        .map(lobbyTiles::get)
                        .filter(Objects::nonNull)
                        .filter(b -> b)
                        .count();
                    if(blackNeighbourTiles == 2){
                        log.info("Flipping white tile: {}", tile);
                        tilesToFlipToBlack.add(tile);
                    }
                }
            });
        
        log.info("Tiles to flip to white: {}", tilesToFlipToWhite);
        log.info("Tiles to flip to black: {}", tilesToFlipToBlack);
        
        tilesToFlipToWhite.forEach(lobbyTiles::remove);
        tilesToFlipToBlack.forEach(tile -> lobbyTiles.put(tile, true));
        
        return lobbyTiles;
    }

}
