package org.odoral.adventofcode.common.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class KeyValue<K, V> {
    @EqualsAndHashCode.Include final K key;
    final V value;
}
