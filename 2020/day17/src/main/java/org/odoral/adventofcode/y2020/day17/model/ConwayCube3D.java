package org.odoral.adventofcode.y2020.day17.model;

import org.odoral.adventofcode.y2020.day17.exception.ConwayCubesException;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Getter
public class ConwayCube3D implements ConwayCube {
    
    @EqualsAndHashCode.Include final int x;
    @EqualsAndHashCode.Include final int y;
    @EqualsAndHashCode.Include final int z;
    final Boolean cubeStatus;

    public List<ConwayCube3D> neighbours() {
        return IntStream.rangeClosed(x - 1, x + 1)
            .mapToObj(neighbourX -> IntStream.rangeClosed(y - 1, y + 1)
                .mapToObj(neighbourY -> IntStream.rangeClosed(z - 1, z + 1)
                    .filter(neighbourZ -> (neighbourX != x || neighbourY != y || neighbourZ != z))
                    .mapToObj(neighbourZ -> new ConwayCube3D(neighbourX, neighbourY, neighbourZ, false)))
                .flatMap(Function.identity()))
            .flatMap(Function.identity())
            .collect(Collectors.toList());
    }

    public ConwayCube3D activated() {
        return new ConwayCube3D(x, y, z, true);
    }

    public static String printSpace(Map<ConwayCube3D, ConwayCube3D> space){
        int xMin = space.keySet()
            .stream()
            .mapToInt(ConwayCube3D::getX)
            .min().orElseThrow(() -> new ConwayCubesException("No min value for X"));
        int xMax = space.keySet()
            .stream()
            .mapToInt(ConwayCube3D::getX)
            .max().orElseThrow(() -> new ConwayCubesException("No max value for X"));
        int yMin = space.keySet()
            .stream()
            .mapToInt(ConwayCube3D::getY)
            .min().orElseThrow(() -> new ConwayCubesException("No min value for Y"));
        int yMax = space.keySet()
            .stream()
            .mapToInt(ConwayCube3D::getY)
            .max().orElseThrow(() -> new ConwayCubesException("No max value for Y"));
        int zMin = space.keySet()
            .stream()
            .mapToInt(ConwayCube3D::getZ)
            .min().orElseThrow(() -> new ConwayCubesException("No min value for Z"));
        int zMax = space.keySet()
            .stream()
            .mapToInt(ConwayCube3D::getZ)
            .max().orElseThrow(() -> new ConwayCubesException("No max value for Z"));

        return IntStream.rangeClosed(zMin, zMax)
            .mapToObj(z -> IntStream.rangeClosed(yMin, yMax)
                .mapToObj(y -> IntStream.rangeClosed(xMin, xMax)
                    .mapToObj(x -> space.getOrDefault(new ConwayCube3D(x, y, z, false), new ConwayCube3D(x, y, z, false)).getCubeStatus() ? "#" : ".")
                    .reduce("", String::concat))
                .collect(Collectors.joining("\n")))
            .collect(Collectors.joining("\n------------------------------\n"));
    }

}
