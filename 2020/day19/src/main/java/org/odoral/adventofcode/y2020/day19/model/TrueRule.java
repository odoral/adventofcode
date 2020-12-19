package org.odoral.adventofcode.y2020.day19.model;

import java.util.Collections;
import java.util.List;

import lombok.ToString;

@ToString
public class TrueRule implements Rule {

    @Override
    public List<Integer> pass(String message, int position) {
        return Collections.singletonList(position);
    }
}
