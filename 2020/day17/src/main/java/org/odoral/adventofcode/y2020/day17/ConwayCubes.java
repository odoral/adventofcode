package org.odoral.adventofcode.y2020.day17;

import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.y2020.day17.exception.ConwayCubesException;
import org.odoral.adventofcode.y2020.day17.model.ConwayCube;
import org.odoral.adventofcode.y2020.day17.model.ConwayCube3D;
import org.odoral.adventofcode.y2020.day17.model.ConwayCube4D;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

import static java.util.stream.Collectors.toMap;

@Slf4j
public class ConwayCubes {

    public static void main(String[] args) throws IOException {
        ConwayCubes conwayCubes = new ConwayCubes();

        Map<ConwayCube3D, ConwayCube3D> space3D = conwayCubes.loadInitialState3D("/input.txt");
        for (int i = 0; i < 6; i++) {
            space3D = conwayCubes.doBootCycle(space3D);
        }
        log.info("Activated 3Dcubes after 6th cycle: {}", conwayCubes.countActivated(space3D));

        Map<ConwayCube4D, ConwayCube4D> space4D = conwayCubes.loadInitialState4D("/input.txt");
        for (int i = 0; i < 6; i++) {
            space4D = conwayCubes.doBootCycle(space4D);
        }
        log.info("Activated 4Dcubes after 6th cycle: {}", conwayCubes.countActivated(space4D));
    }

    public Map<ConwayCube3D, ConwayCube3D> loadInitialState3D(String resource) throws IOException {
        AtomicInteger yPos = new AtomicInteger(0);
        int zPos = 0;
        return CommonUtils.loadResource(resource, line -> {
            AtomicInteger xPos = new AtomicInteger(0);
            final int currentYPos = yPos.getAndIncrement();
            return Stream.of(CommonUtils.toCharacterArray(line))
                .map(cubeStatus -> {
                    int currentXPos = xPos.getAndIncrement();
                    switch (cubeStatus) {
                        case '.':
                            return null;
                        case '#':
                            return new ConwayCube3D(currentXPos, currentYPos, zPos, true);
                        default:
                            throw new ConwayCubesException("Unsupported cube status: " + cubeStatus);
                    }
                }).collect(Collectors.toList());
        }).stream()
            .flatMap(List::stream)
            .filter(Objects::nonNull)
            .collect(toMap(Function.identity(), Function.identity()));
    }

    public Map<ConwayCube4D, ConwayCube4D> loadInitialState4D(String resource) throws IOException {
        AtomicInteger yPos = new AtomicInteger(0);
        int zPos = 0;
        int wPos = 0;
        return CommonUtils.loadResource(resource, line -> {
            AtomicInteger xPos = new AtomicInteger(0);
            final int currentYPos = yPos.getAndIncrement();
            return Stream.of(CommonUtils.toCharacterArray(line))
                .map(cubeStatus -> {
                    int currentXPos = xPos.getAndIncrement();
                    switch (cubeStatus) {
                        case '.':
                            return null;
                        case '#':
                            return new ConwayCube4D(currentXPos, currentYPos, zPos, wPos, true);
                        default:
                            throw new ConwayCubesException("Unsupported cube status: " + cubeStatus);
                    }
                }).collect(Collectors.toList());
        }).stream()
            .flatMap(List::stream)
            .filter(Objects::nonNull)
            .collect(toMap(Function.identity(), Function.identity()));
    }

    public <T extends ConwayCube> Map<T, T> doBootCycle(Map<T, T> space) {
        Map<T, T> resultingSpace = new HashMap<>();
        
        // Get potential cubes to check
        List<T> cubesToCheck = space.keySet()
            .stream()
            .flatMap(cube -> {
                List<T> neighbours = cube.neighbours();
                neighbours.add(cube);
                return neighbours.stream();
            })
            .distinct()
            .collect(Collectors.toList());

        for (T conwayCube : cubesToCheck) {
            T spaceCube = space.get(conwayCube);
            if(spaceCube != null){
                conwayCube = spaceCube;
            }
            long activatedNeighbours = conwayCube.activatedNeighbours(space);
            if(conwayCube.isActive()){
                if(activatedNeighbours >= 2 && activatedNeighbours <= 3){
                    resultingSpace.put(conwayCube, conwayCube);
                }
            }else if(activatedNeighbours == 3){
                resultingSpace.put(conwayCube.activated(), conwayCube.activated());
            }
        }
        
        return resultingSpace;
    }

    public long countActivated(Map<? extends ConwayCube, ? extends ConwayCube> space) {
        return space.keySet()
            .stream()
            .filter(ConwayCube::isActive)
            .count();
    }
    
}
