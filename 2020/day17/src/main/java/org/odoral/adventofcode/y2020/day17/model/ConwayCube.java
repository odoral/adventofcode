package org.odoral.adventofcode.y2020.day17.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface ConwayCube {
    
    <T extends ConwayCube> List<T> neighbours();

    <T extends ConwayCube> T activated();
    
    Boolean getCubeStatus();
    
    default long activatedNeighbours(Map<? extends ConwayCube, ? extends ConwayCube> space){
        return neighbours().stream()
            .map(space::get)
            .filter(Objects::nonNull)
            .filter(ConwayCube::getCubeStatus)
            .count();
    }

}
