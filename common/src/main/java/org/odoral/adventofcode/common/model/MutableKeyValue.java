package org.odoral.adventofcode.common.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class MutableKeyValue<K, V> {
    @EqualsAndHashCode.Include K key;
    V value;

    public KeyValue<K, V> getImmutable() {
        return new KeyValue<>(key, value);
    }
}
