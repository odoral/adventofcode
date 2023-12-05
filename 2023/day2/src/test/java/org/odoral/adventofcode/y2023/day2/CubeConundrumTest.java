package org.odoral.adventofcode.y2023.day2;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CubeConundrumTest {

    protected CubeConundrum cubeConundrum;

    @Before public void setUp() {
        cubeConundrum = new CubeConundrum();
    }

    @Test public void test_scenario1() throws IOException {
        List<CubeConundrum.Game> games = CommonUtils.loadResource("/input.txt", CubeConundrum.Game::from);
        CubeConundrum.Result result = cubeConundrum.getIDSumOfValidGames(games);
        assertEquals(3, result.validGames.size());
        assertEquals((Long) 8L, result.idSum);
    }

    @Test public void test_scenario2() throws IOException {
        List<CubeConundrum.Game> games = CommonUtils.loadResource("/input.txt", CubeConundrum.Game::from);
        CubeConundrum.Result result = cubeConundrum.getPowerOfMinimunSetOfCubes(games);
        assertEquals(5, result.validGames.size());
        assertEquals((Long) 2286L, result.idSum);
    }

}
