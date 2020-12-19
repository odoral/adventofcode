package org.odoral.adventofcode.y2020.day19.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface Rule {

    List<Integer> pass(String message, int position);

    default List<Integer> and(Rule otherRule, String message, int position){
        List<Integer> pass = pass(message, position);
        
        return pass.stream()
            .map(firstMemberPosition -> otherRule.pass(message, firstMemberPosition))
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }

    default List<Integer> or(Rule otherRule, String message, int position){
        List<Integer> result = new ArrayList<>();
        result.addAll(pass(message, position));
        result.addAll(otherRule.pass(message, position));
        return result;
    }
}
