package org.odoral.adventofcode.y2022.day21.model;

import java.util.Map;

public interface MonkeyAction {
    String getMonkeyName();

    Long yell(Map<String, MonkeyAction> actions);
}
