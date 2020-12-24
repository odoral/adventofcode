package org.odoral.adventofcode.y2020.day20;

import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.model.KeyValue;
import org.odoral.adventofcode.y2020.day20.exception.JurassicJigsawException;
import org.odoral.adventofcode.y2020.day20.model.DefaultSquareTile;
import org.odoral.adventofcode.y2020.day20.model.SquareTile;
import org.odoral.adventofcode.y2020.day20.model.TileSide;
import org.odoral.adventofcode.y2020.day20.model.TiledImage;
import org.odoral.adventofcode.y2020.day20.model.transformations.CroppedSquareTile;
import org.odoral.adventofcode.y2020.day20.model.transformations.FlipSquareTile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JurassicJigsaw {

    public static void main(String[] args) throws IOException {
        JurassicJigsaw jurassicJigsaw = new JurassicJigsaw();
        List<SquareTile> tiles = jurassicJigsaw.loadTiles("/input.txt");
        log.info("Loaded {} tiles", tiles.size());
        long resultPartOne = jurassicJigsaw.calculateResultPartOne(jurassicJigsaw.findCorners(tiles));
        log.info("Result for part one: {}", resultPartOne);


        SquareTile image = jurassicJigsaw.buildImage(tiles);
        List<KeyValue<Integer, Integer>> seaMonsterPositions = jurassicJigsaw.transformImageToLookForSeaMonsters(image);
        long waterRoughness = jurassicJigsaw.calculateWaterRoughness(image);
        long seaMonsterRelatedWaterRoughness = jurassicJigsaw.calculateSeaMonsterRelatedRoughness();
        log.info("Result for part two: {}", waterRoughness - (seaMonsterRelatedWaterRoughness * seaMonsterPositions.size()));
    }

    public List<SquareTile> loadTiles(String resource) throws IOException {
        List<SquareTile> tiles = new ArrayList<>();
        AtomicInteger row = new AtomicInteger(0);
        CommonUtils.loadResource(resource, line -> {
            DefaultSquareTile tile;
            if(line.startsWith("Tile")){
                int tileNumber = Integer.parseInt(line.split(" ")[1].split(":")[0]);
                tile = new DefaultSquareTile(tileNumber);
                tiles.add(tile);
                row.set(0);
            }else{
                tile = (DefaultSquareTile) tiles.get(tiles.size() - 1);
                if(!line.isEmpty()){
                    Character[] chars = CommonUtils.toCharacterArray(line);
                    int r = row.getAndIncrement();
                    if(r == 0){
                        tile.setData(new Character[chars.length][]);
                    }
                    tile.getData()[r] = chars;
                }
            }
            return null;
        });
        return tiles;
    }
    
    public Character[][] loadSeaMonster(String resource) throws IOException {
        return CommonUtils.loadResource(resource, CommonUtils::toCharacterArray)
            .toArray(new Character[0][]);
    }

    public List<SquareTile> findCorners(List<SquareTile> tiles) {
        Map<Long, List<TileSide>> connections = calculateConnections(tiles);

        return filterCorners(tiles, connections);
    }

    public Map<Long, List<TileSide>> calculateConnections(Collection<SquareTile> tiles) {
        return tiles.stream()
            .map(SquareTile::sides)
            .flatMap(List::stream)
            .collect(Collectors.groupingBy(TileSide::sideId));
    }

    protected List<SquareTile> filterCorners(List<SquareTile> tiles, Map<Long, List<TileSide>> connections) {
        return tiles.stream()
            .filter(tile -> connections.values()
                .stream()
                .filter(e -> e.size() > 1)
                .filter(e -> e.stream().anyMatch(s -> s.getNumber() == tile.number()))
                .count() == 2)
            .collect(Collectors.toList());
    }

    public long calculateResultPartOne(List<SquareTile> corners) {
        return corners.stream()
            .mapToLong(SquareTile::number)
            .reduce(1L, (l1, l2) -> l1 * l2);
    }

    public TiledImage buildImage(List<SquareTile> tiles) {
        Map<Long, SquareTile> tilesMap = tiles.stream()
            .collect(Collectors.toMap(SquareTile::number, Function.identity()));

        List<SquareTile> finalTiles = new ArrayList<>();
        // Add first corner ensuring it connects on RIGHT and BOTTOM
        Map<Long, List<KeyValue<TileSide, TileSide>>> connectionsPerTile = connectionsPerTile(tilesMap.values());
        SquareTile firstTileInRow = tilesMap.get(connectionsPerTile.entrySet()
            .stream()
            .filter(e -> e.getValue().size() == 2)
            .findAny()
            .map(Map.Entry::getKey)
            .orElseThrow(() -> new JurassicJigsawException("There is no corner to start")));
        
        if(hasConnections(connectionsPerTile.get(firstTileInRow.number()), TileSide.Side.TOP)){
            firstTileInRow = new FlipSquareTile(firstTileInRow, FlipSquareTile.Flip.HORIZONTAL);
            tilesMap.put(firstTileInRow.number(), firstTileInRow);
            connectionsPerTile = connectionsPerTile(tilesMap.values());
        }
        if(hasConnections(connectionsPerTile.get(firstTileInRow.number()), TileSide.Side.LEFT)){
            firstTileInRow = new FlipSquareTile(firstTileInRow, FlipSquareTile.Flip.VERTICAL);
            tilesMap.put(firstTileInRow.number(), firstTileInRow);
            connectionsPerTile = connectionsPerTile(tilesMap.values());
        }
        finalTiles.add(firstTileInRow);
        
        // Add sorted tiles from TOP-LEFT corner
        while(finalTiles.size() != tiles.size()){
            SquareTile lastTile = finalTiles.get(finalTiles.size() - 1);
            Optional<KeyValue<TileSide, TileSide>> rightConnection = getConnection(lastTile, TileSide.Side.RIGHT, connectionsPerTile);
            KeyValue<TileSide, TileSide> connection;
            
            if(rightConnection.isPresent()){
                connection = rightConnection.get();
            }else{
                connection = getConnection(firstTileInRow, TileSide.Side.BOTTOM, connectionsPerTile)
                    .orElseThrow(() -> new JurassicJigsawException("There should be a BOTTOM connection."));
                firstTileInRow = tilesMap.get(connection.getValue().getNumber());
            }
            
            SquareTile nextTile = tilesMap.get(connection.getValue().getNumber());
            List<UnaryOperator<SquareTile>> transformations = connection.getValue().getTransformationsToConnectTo(connection.getKey());
            for (UnaryOperator<SquareTile> transformation : transformations) {
                nextTile = transformation.apply(nextTile);
            }
            finalTiles.add(nextTile);
            tilesMap.put(nextTile.number(), nextTile);
            connectionsPerTile = connectionsPerTile(tilesMap.values());
        }
        
        // Convert tiles
        return new TiledImage(finalTiles.stream()
            .map(tile -> new CroppedSquareTile(tile, 1))
            .collect(Collectors.toList()));
    }

    protected Optional<KeyValue<TileSide, TileSide>> getConnection(SquareTile sourceTile, TileSide.Side side, Map<Long, List<KeyValue<TileSide, TileSide>>> connectionsPerTile){
        return connectionsPerTile.get(sourceTile.number())
            .stream()
            .filter(e -> e.getKey().getSide().equals(side))
            .findAny();
    }

    protected Map<Long, List<KeyValue<TileSide, TileSide>>> connectionsPerTile(Collection<SquareTile> tiles) {
        return calculateConnections(tiles).entrySet()
            .stream()
            .filter(e -> e.getValue().size() == 2)
            .flatMap(e -> {
                List<TileSide> sides = e.getValue();
                return Stream.of(
                    KeyValue.<TileSide, TileSide>builder()
                        .key(sides.get(0))
                        .value(sides.get(1))
                        .build(),
                    KeyValue.<TileSide, TileSide>builder()
                        .key(sides.get(1))
                        .value(sides.get(0))
                        .build()
                );
            }).collect(Collectors.groupingBy(t -> t.getKey().getNumber()));
    }

    protected boolean hasConnections(List<KeyValue<TileSide, TileSide>> cornerConnections, TileSide.Side ... sidesConnected) {
        List<TileSide> connectedSides = cornerConnections.stream()
            .map(KeyValue::getKey)
            .collect(Collectors.toList());
        for (TileSide.Side sideConnected : sidesConnected) {
            if(connectedSides.stream()
                .noneMatch(side -> side.getSide().equals(sideConnected))){
                return false;
            }
        }
        return true;
    }

    protected List<KeyValue<Integer, Integer>> transformImageToLookForSeaMonsters(SquareTile image) throws IOException {
        List<KeyValue<Integer, Integer>> seaMonsterPositions = Collections.emptyList();
        List<Function<SquareTile, SquareTile>> transformations = Arrays.asList(
            Function.identity(),
            FlipSquareTile.TR_FLIP_VERTICAL,

            FlipSquareTile.TR_ROTATE,
            FlipSquareTile.TR_ROTATE.andThen(FlipSquareTile.TR_FLIP_VERTICAL),

            FlipSquareTile.TR_ROTATE.andThen(FlipSquareTile.TR_FLIP_HORIZONTAL),
            FlipSquareTile.TR_ROTATE.andThen(FlipSquareTile.TR_FLIP_HORIZONTAL).andThen(FlipSquareTile.TR_FLIP_VERTICAL),

            FlipSquareTile.TR_FLIP_HORIZONTAL,
            FlipSquareTile.TR_FLIP_HORIZONTAL.andThen(FlipSquareTile.TR_FLIP_VERTICAL)
        );
        for (Function<SquareTile, SquareTile> transformation : transformations) {
            log.info("Test transformation: {}", transformation);
            SquareTile transformedImage = transformation.apply(image);
            List<KeyValue<Integer, Integer>> result = lookForSeaMonsters(transformedImage);
            if(result.size() > seaMonsterPositions.size()){
                seaMonsterPositions = result;
            }
        }
        return seaMonsterPositions;
    }

    public List<KeyValue<Integer, Integer>> lookForSeaMonsters(SquareTile squareTile) throws IOException {
        Character[][] seaMonsterPattern = loadSeaMonster("/sea_monster.txt");
        Predicate<KeyValue<Integer, Integer>> matchesWithSeaMonster = buildSeaMonsterMatcher(seaMonsterPattern, squareTile);
        
        return IntStream.range(0, squareTile.width())
            .mapToObj(y -> IntStream.range(0, squareTile.width())
                .mapToObj(x -> KeyValue.<Integer, Integer>builder()
                    .key(x)
                    .value(y)
                    .build()))
            .flatMap(Function.identity())
            .filter(matchesWithSeaMonster)
            .collect(Collectors.toList());
    }

    protected Predicate<KeyValue<Integer, Integer>> buildSeaMonsterMatcher(Character[][] seaMonsterPattern, SquareTile tile) {
        Predicate<KeyValue<Integer, Integer>> result = pos -> 
            pos.getKey() + seaMonsterPattern[0].length < tile.width() && 
                pos.getValue() + seaMonsterPattern.length < tile.width();
        
        for (int y = 0; y < seaMonsterPattern.length; y++) {
            Character[] rowPattern = seaMonsterPattern[y];
            for (int x = 0; x < rowPattern.length; x++) {
                if(rowPattern[x] == '#'){
                    final int posX = x;
                    final int posY = y;
                    result = result.and(pos -> tile.at(pos.getKey() + posX, pos.getValue() + posY) == '#');
                }
            }
        }
        return result;
    }

    public long calculateWaterRoughness(SquareTile squareTile) {
        return IntStream.range(0, squareTile.width())
            .mapToObj(y -> IntStream.range(0, squareTile.width())
                .mapToObj(x -> squareTile.at(x, y)))
            .flatMap(Function.identity())
            .filter(c -> c == '#')
            .count();
    }

    public long calculateSeaMonsterRelatedRoughness() throws IOException {
        return Stream.of(loadSeaMonster("/sea_monster.txt"))
            .flatMap(Stream::of)
            .filter(c -> c == '#')
            .count();
    }
}
