package org.odoral.adventofcode.y2020.day20.model.transformations;

import org.odoral.adventofcode.y2020.day20.model.SquareTile;

import java.util.function.UnaryOperator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FlipSquareTile implements TransformedSquareTile {

    public static final UnaryOperator<SquareTile> TR_FLIP_VERTICAL = t -> new FlipSquareTile(t, Flip.VERTICAL);
    public static final UnaryOperator<SquareTile> TR_FLIP_HORIZONTAL = t -> new FlipSquareTile(t, Flip.HORIZONTAL);
    public static final UnaryOperator<SquareTile> TR_ROTATE = RotatedSquareTile::new;

    public enum Flip{
        HORIZONTAL,
        VERTICAL
    }
    
    final SquareTile squareTile;
    final Flip flip;
    
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
        switch (flip){
            case VERTICAL:
                return squareTile.at(width() - 1 - x, y);
            case HORIZONTAL:
                return squareTile.at(x, width() - 1 - y);
            default:
                throw new IllegalStateException("Unexpected value: " + squareTile);
        }
    }
}
