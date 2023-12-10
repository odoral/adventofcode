package org.odoral.adventofcode.common.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class LeftRight<L, R> {
    @EqualsAndHashCode.Include final L left;
    final R right;
}
