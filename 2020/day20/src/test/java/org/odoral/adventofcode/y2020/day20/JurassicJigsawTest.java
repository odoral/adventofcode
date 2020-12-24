package org.odoral.adventofcode.y2020.day20;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.model.KeyValue;
import org.odoral.adventofcode.y2020.day20.model.SquareTile;

import java.io.IOException;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Slf4j
public class JurassicJigsawTest {

    protected JurassicJigsaw jurassicJigsaw;

    @Before public void setUp() {
        jurassicJigsaw = new JurassicJigsaw();
    }
    
    @Test public void test_scenario1() throws IOException {
        List<SquareTile> tiles = jurassicJigsaw.loadTiles("/scenario1.txt");
        assertEquals(9, tiles.size());
        log.info("Loaded {} tiles.", tiles.size());
        List<SquareTile> corners = jurassicJigsaw.findCorners(tiles);
        assertEquals(4, corners.size());
        assertTrue(corners.stream().anyMatch(t -> t.number() == 1951));
        assertTrue(corners.stream().anyMatch(t -> t.number() == 3079));
        assertTrue(corners.stream().anyMatch(t -> t.number() == 2971));
        assertTrue(corners.stream().anyMatch(t -> t.number() == 1171));
        assertEquals(20899048083289L, jurassicJigsaw.calculateResultPartOne(corners));
    }
    
    @Test public void test_scenario2() throws IOException {
        List<SquareTile> tiles = jurassicJigsaw.loadTiles("/scenario1.txt");
        assertEquals(9, tiles.size());
        log.info("Loaded {} tiles.", tiles.size());
        SquareTile image = jurassicJigsaw.buildImage(tiles);
        
        List<KeyValue<Integer, Integer>> seaMonsterPositions = jurassicJigsaw.transformImageToLookForSeaMonsters(image);
        assertEquals(2, seaMonsterPositions.size());
        long waterRoughness = jurassicJigsaw.calculateWaterRoughness(image);
        long seaMonsterRelatedWaterRoughness = jurassicJigsaw.calculateSeaMonsterRelatedRoughness();
        assertEquals(273, waterRoughness - (seaMonsterRelatedWaterRoughness * seaMonsterPositions.size()));
    }

}