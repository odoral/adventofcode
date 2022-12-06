package org.odoral.adventofcode.y2020.day17.model;

import java.util.List;
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
public class ConwayCube4D implements ConwayCube {
    
    @EqualsAndHashCode.Include final int x;
    @EqualsAndHashCode.Include final int y;
    @EqualsAndHashCode.Include final int z;
    @EqualsAndHashCode.Include final int w;
    final boolean active;

    public List<ConwayCube4D> neighbours() {
        return IntStream.rangeClosed(x - 1, x + 1)
            .mapToObj(neighbourX -> IntStream.rangeClosed(y - 1, y + 1)
                .mapToObj(neighbourY -> IntStream.rangeClosed(z - 1, z + 1)
                    .mapToObj(neighbourZ -> IntStream.rangeClosed(w - 1, w + 1)
                        .filter(neighbourW -> (neighbourX != x || neighbourY != y || neighbourZ != z || neighbourW != w))
                        .mapToObj(neighbourW -> new ConwayCube4D(neighbourX, neighbourY, neighbourZ, neighbourW, false)))
                    .flatMap(Function.identity())))
            .flatMap(Function.identity())
            .flatMap(Function.identity())
            .collect(Collectors.toList());
    }

    public ConwayCube4D activated() {
        return new ConwayCube4D(x, y, z, w, true);
    }

}
