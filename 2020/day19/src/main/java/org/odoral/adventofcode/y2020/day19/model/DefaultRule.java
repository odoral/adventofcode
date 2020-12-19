package org.odoral.adventofcode.y2020.day19.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultRule implements Rule {
    final Map<Integer, Rule> rulesMap;
    final String config;
    final int ruleNumber;

    @Override
    public List<Integer> pass(String message, int position) {
        if (position == message.length()) {
            return Collections.emptyList();
        }
        
        if ("a".equals(config.trim()) || "b".equals(config.trim())) {
            if (config.trim().charAt(0) == message.charAt(position)) {
                return Collections.singletonList(position + 1);
            } else {
                return Collections.emptyList();
            }
        }

        return Stream.of(config.split("\\|"))
            .map(rConfig -> AndRule.reduceAndRules(parseRules(rConfig)))
            .reduce(new FalseRule(), OrRule::new)
            .pass(message, position);
    }

    protected List<Rule> parseRules(String memberRules) {
        return Stream.of(memberRules.trim().split(" "))
            .map(String::trim)
            .map(Integer::parseInt)
            .map(rulesMap::get)
            .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return ruleNumber + "(" + config + ")";
    }

}
