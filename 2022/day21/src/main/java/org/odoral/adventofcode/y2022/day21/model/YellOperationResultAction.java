package org.odoral.adventofcode.y2022.day21.model;

import org.odoral.adventofcode.common.exception.AdventOfCodeException;

import java.util.Map;
import java.util.function.BinaryOperator;

public class YellOperationResultAction implements MonkeyAction {
    final String monkeyName;
    String operation;

    private YellOperationResultAction(String monkeyName, String operation) {
        this.monkeyName = monkeyName;
        this.operation = operation;
    }

    @Override
    public String getMonkeyName() {
        return monkeyName;
    }

    @Override
    public Long yell(Map<String, MonkeyAction> actions) {
        String[] fields = operation.split("\\s");
        BinaryOperator<Long> operationFunction;
        switch (fields[1]) {
            case "+":
                operationFunction = Math::addExact;
                break;
            case "-":
                operationFunction = Math::subtractExact;
                break;
            case "*":
                operationFunction = Math::multiplyExact;
                break;
            case "/":
                operationFunction = Math::floorDiv;
                break;
            default:
                throw new AdventOfCodeException("Unsupported operation: " + fields[1]);
        }
        return operationFunction.apply(
            actions.get(fields[0]).yell(actions),
            actions.get(fields[2]).yell(actions)
        );
    }

    public Long[] yellTwoNumbers(Map<String, MonkeyAction> actions) {
        String[] fields = operation.split("\\s");
        long number1 = actions.get(fields[0]).yell(actions);
        long number2 = actions.get(fields[2]).yell(actions);

        return new Long[]{number1, number2};
    }

    public static YellOperationResultAction parse(String monkeyName, String operation) {
        return new YellOperationResultAction(monkeyName, operation);
    }
}
