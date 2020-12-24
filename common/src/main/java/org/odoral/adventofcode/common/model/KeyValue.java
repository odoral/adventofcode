package org.odoral.adventofcode.common.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class KeyValue<K, V> {
    final K key;
    final V value;
}
