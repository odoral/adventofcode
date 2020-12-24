package org.odoral.adventofcode.y2020.day20.model.transformations;

import org.odoral.adventofcode.y2020.day20.model.SquareTile;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CroppedSquareTile implements TransformedSquareTile {
    final SquareTile squareTile;
    final int border;

    @Override
    public long number() {
        return squareTile.number();
    }

    @Override
    public int width() {
        return squareTile.width() - (border*2);
    }

    @Override
    public char at(int x, int y) {
        return squareTile.at(x + border, y + border);
    }
}
