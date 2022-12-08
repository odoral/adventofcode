package org.odoral.adventofcode.y2022.day7;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.model.Directory;

import java.io.IOException;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class NoSpaceLeftOnDeviceTest {

    protected NoSpaceLeftOnDevice noSpaceLeftOnDevice;

    @Before public void setUp() {
        noSpaceLeftOnDevice = new NoSpaceLeftOnDevice();
    }

    @Test public void test_scenario1() throws IOException {
        NoSpaceLeftOnDevice noSpaceLeftOnDevice = new NoSpaceLeftOnDevice();
        Directory root = noSpaceLeftOnDevice.buildFileSystem(CommonUtils.loadResource("/input.txt", Function.identity()));
        noSpaceLeftOnDevice.printFileSystem(root);
        NoSpaceLeftOnDevice.Result result = noSpaceLeftOnDevice.findAllDirectoriesWithAtMost(root, NoSpaceLeftOnDevice.TOTAL_SIZE_100K);
        assertEquals(95437L, result.total);
    }

    @Test public void test_scenario2() throws IOException {
        NoSpaceLeftOnDevice noSpaceLeftOnDevice = new NoSpaceLeftOnDevice();
        Directory root = noSpaceLeftOnDevice.buildFileSystem(CommonUtils.loadResource("/input.txt", Function.identity()));
        NoSpaceLeftOnDevice.Result result = noSpaceLeftOnDevice.findDirectoriesToFree(root, NoSpaceLeftOnDevice.TOTAL_SPACE_TO_FREE);
        assertEquals(24933642L, result.total);
    }

}