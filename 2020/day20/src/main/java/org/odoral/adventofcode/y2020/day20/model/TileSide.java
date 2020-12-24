package org.odoral.adventofcode.y2020.day20.model;

import org.odoral.adventofcode.y2020.day20.model.transformations.FlipSquareTile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
@Data
public class TileSide {

    public enum Side {
        LEFT,
        TOP,
        RIGHT,
        BOTTOM
    }
    
    final long number;
    final Character[] data;
    final Side side;

    public long sideId() {
        long result = calculateSideId(data);
        long resultFlip = calculateSideId(flip());
        return Math.max(result, resultFlip);
    }

    public long calculateSideId() {
        return calculateSideId(data);
    }

    public long calculateSideId(Character[] sideData) {
        final long prime = 31;
        long result = 1;
        for (Character character : sideData) {
            result = prime * result + (character == '#' ? 1 : 0);
        }
        return result;
    }

    protected Character[] flip() {
        List<Character> flip = Stream.of(data)
            .collect(Collectors.toList());
        Collections.reverse(flip);
        return flip.toArray(new Character[flip.size()]);
    }
    public List<UnaryOperator<SquareTile>> getTransformationsToConnectTo(TileSide sourceConnection) {
        List<UnaryOperator<SquareTile>> transformations = new ArrayList<>();

        switch (sourceConnection.getSide()){
            case LEFT:
                switch (side){
                    case LEFT:
                        transformations.add(FlipSquareTile.TR_FLIP_VERTICAL);
                        break;
                    case TOP:
                        transformations.add(FlipSquareTile.TR_ROTATE);
                        transformations.add(FlipSquareTile.TR_FLIP_VERTICAL);
                        break;
                    case RIGHT:
                        break;
                    case BOTTOM:
                        transformations.add(FlipSquareTile.TR_ROTATE);
                        break;
                }
                break;
            case TOP:
                switch (side){
                    case LEFT:
                        transformations.add(FlipSquareTile.TR_ROTATE);
                        transformations.add(FlipSquareTile.TR_FLIP_HORIZONTAL);
                        break;
                    case TOP:
                        transformations.add(FlipSquareTile.TR_FLIP_HORIZONTAL);
                        break;
                    case RIGHT:
                        transformations.add(FlipSquareTile.TR_ROTATE);
                        break;
                    case BOTTOM:
                        break;
                }
                break;
            case RIGHT:
                switch (side){
                    case LEFT:
                        break;
                    case TOP:
                        transformations.add(FlipSquareTile.TR_ROTATE);
                        break;
                    case RIGHT:
                        transformations.add(FlipSquareTile.TR_FLIP_VERTICAL);
                        break;
                    case BOTTOM:
                        transformations.add(FlipSquareTile.TR_ROTATE);
                        transformations.add(FlipSquareTile.TR_FLIP_VERTICAL);
                        break;
                }
                break;
            case BOTTOM:
                switch (side) {
                    case LEFT:
                        transformations.add(FlipSquareTile.TR_ROTATE);
                        break;
                    case TOP:
                        break;
                    case RIGHT:
                        transformations.add(FlipSquareTile.TR_ROTATE);
                        transformations.add(FlipSquareTile.TR_FLIP_HORIZONTAL);
                        break;
                    case BOTTOM:
                        transformations.add(FlipSquareTile.TR_FLIP_HORIZONTAL);
                        break;
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + sourceConnection.getSide());
        }
        
        if(this.calculateSideId() != sourceConnection.calculateSideId()){
            switch (sourceConnection.side){
                case LEFT:
                case RIGHT:
                    transformations.add(t -> new FlipSquareTile(t, FlipSquareTile.Flip.HORIZONTAL));
                    break;
                case TOP:
                case BOTTOM:
                    transformations.add(FlipSquareTile.TR_FLIP_VERTICAL);
                    break;
            }
        }
        
        return transformations;
    }
}
