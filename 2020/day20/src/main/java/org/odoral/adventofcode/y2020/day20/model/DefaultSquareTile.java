package org.odoral.adventofcode.y2020.day20.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class DefaultSquareTile implements SquareTile {
    final long number;
    @Getter @Setter Character[][] data;

    @Override
    public long number() {
        return number;
    }

    @Override
    public int width() {
        return data[0].length;
    }

    @Override
    public char at(int x, int y) {
        return data[y][x];
    }
}
