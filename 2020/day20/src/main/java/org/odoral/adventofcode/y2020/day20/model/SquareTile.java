package org.odoral.adventofcode.y2020.day20.model;

import java.util.ArrayList;
import java.util.List;

public interface SquareTile {

    long number();
    int width();

    char at(int x, int y);

    default List<TileSide> sides() {
        List<TileSide> tileSides = new ArrayList<>();
        tileSides.add(side(TileSide.Side.TOP, number(), 0, 0, 1, 0));
        tileSides.add(side(TileSide.Side.LEFT, number(), 0, 0, 0, 1));
        tileSides.add(side(TileSide.Side.RIGHT, number(), width() - 1, 0, 0, 1));
        tileSides.add(side(TileSide.Side.BOTTOM, number(), 0, width() - 1, 1, 0));

        return tileSides;
    }

    default TileSide side(TileSide.Side side, long tileNumber, int x, int y, int xIncrement, int yIncrement) {
        Character[] sideCharacters = new Character[width()];
        for (int i = 0; i < width(); i++) {
            sideCharacters[i] = at(x + xIncrement * i, y + yIncrement * i);
        }
        return new TileSide(tileNumber, sideCharacters, side);
    }
    
    default String print(){
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < width(); y++) {
            for (int x = 0; x < width(); x++) {
                sb.append(at(x, y));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
