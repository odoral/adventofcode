package org.odoral.adventofcode.y2020.day19.model;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrRule implements Rule {
    final Rule rule1;
    final Rule rule2;

    @Override
    public List<Integer> pass(String message, int position) {
        return rule1.or(rule2, message, position);
    }

    @Override
    public String toString() {
        return "("+rule1.toString()+") OR ("+rule2.toString()+")";
    }
}
