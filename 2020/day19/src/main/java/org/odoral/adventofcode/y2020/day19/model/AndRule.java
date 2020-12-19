package org.odoral.adventofcode.y2020.day19.model;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AndRule implements Rule {
    final Rule rule1;
    final Rule rule2;

    public static Rule reduceAndRules(List<Rule> rules) {
        return rules.stream()
            .reduce(new TrueRule(), AndRule::new);
    }
    
    @Override
    public List<Integer> pass(String message, int position) {
        return rule1.and(rule2, message, position);
    }

    @Override
    public String toString() {
        return "("+rule1.toString()+") AND ("+rule2.toString()+")";
    }
}
