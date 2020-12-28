package org.odoral.adventofcode.y2020.day24.model;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Getter
public class Tile {
    @EqualsAndHashCode.Include final int x;
    @EqualsAndHashCode.Include final int y;

    public List<Tile> neighbourTiles() {
        return Stream.of(Directions.values())
            .map(direction -> new Tile(x + direction.x, y + direction.y))
            .collect(Collectors.toList());
    }
}
