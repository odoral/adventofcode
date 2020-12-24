package org.odoral.adventofcode.y2020.day20.model.transformations;

import org.odoral.adventofcode.y2020.day20.model.SquareTile;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RotatedSquareTile implements TransformedSquareTile {
    final SquareTile squareTile;

    @Override
    public long number() {
        return squareTile.number();
    }

    @Override
    public int width() {
        return squareTile.width();
    }

    @Override
    public char at(int x, int y) {
        return squareTile.at(y, x);
    }
}
