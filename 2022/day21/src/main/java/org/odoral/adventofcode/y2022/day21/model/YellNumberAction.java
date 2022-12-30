package org.odoral.adventofcode.y2022.day21.model;

import java.util.Map;

public class YellNumberAction implements MonkeyAction {
    final String monkeyName;

    String number;

    private YellNumberAction(String monkeyName, String number) {
        this.monkeyName = monkeyName;
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String getMonkeyName() {
        return monkeyName;
    }

    @Override
    public Long yell(Map<String, MonkeyAction> actions) {
        return Long.valueOf(number);
    }

    public static YellNumberAction parse(String monkeyName, String number) {
        return new YellNumberAction(monkeyName, number);
    }

}
