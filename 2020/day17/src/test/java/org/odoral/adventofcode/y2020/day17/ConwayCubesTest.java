package org.odoral.adventofcode.y2020.day17;

import org.junit.Before;
import org.junit.Test;
import org.odoral.adventofcode.y2020.day17.model.ConwayCube3D;
import org.odoral.adventofcode.y2020.day17.model.ConwayCube4D;

import java.io.IOException;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.assertEquals;

@Slf4j
public class ConwayCubesTest {

    protected ConwayCubes conwayCubes;

    @Before public void setUp() {
        conwayCubes = new ConwayCubes();
    }
    
    @Test public void test_3D() throws IOException {
        Map<ConwayCube3D, ConwayCube3D> space = conwayCubes.loadInitialState3D("/scenario1.txt");
        log.info("Loaded {} layers", space.size());
        assertEquals(ConwayCube3D.printSpace(space), 5, conwayCubes.countActivated(space));

        space = conwayCubes.doBootCycle(space);
        assertEquals(ConwayCube3D.printSpace(space), 11, conwayCubes.countActivated(space));

        space = conwayCubes.doBootCycle(space);
        assertEquals(ConwayCube3D.printSpace(space), 21, conwayCubes.countActivated(space));

        space = conwayCubes.doBootCycle(space);
        assertEquals(ConwayCube3D.printSpace(space),38, conwayCubes.countActivated(space));
        
        space = conwayCubes.doBootCycle(space);
        space = conwayCubes.doBootCycle(space);
        space = conwayCubes.doBootCycle(space);
        assertEquals(ConwayCube3D.printSpace(space),112, conwayCubes.countActivated(space));
    }
    
    @Test public void test_4D() throws IOException {
        Map<ConwayCube4D, ConwayCube4D> space = conwayCubes.loadInitialState4D("/scenario1.txt");
        log.info("Loaded {} layers", space.size());
        assertEquals(5, conwayCubes.countActivated(space));

        space = conwayCubes.doBootCycle(space);
        assertEquals(29, conwayCubes.countActivated(space));

        space = conwayCubes.doBootCycle(space);
        assertEquals(60, conwayCubes.countActivated(space));

        space = conwayCubes.doBootCycle(space);
        space = conwayCubes.doBootCycle(space);
        space = conwayCubes.doBootCycle(space);
        space = conwayCubes.doBootCycle(space);
        assertEquals(848, conwayCubes.countActivated(space));
    }
}