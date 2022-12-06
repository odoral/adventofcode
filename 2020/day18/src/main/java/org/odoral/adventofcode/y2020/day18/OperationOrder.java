package org.odoral.adventofcode.y2020.day18;

import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.List;
import java.util.function.ToLongFunction;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OperationOrder {

    public static void main(String[] args) throws IOException {
        OperationOrder operationOrder = new OperationOrder();
        List<String> operations = operationOrder.loadInput("/input.txt");
        long total = operationOrder.processOperations(operations, operationOrder::processOperationPart1);
        log.info("Total for operations from first part: {}", total);
        total = operationOrder.processOperations(operations, operationOrder::processOperationPart2);
        log.info("Total for operations from second part: {}", total);
    }

    public List<String> loadInput(String resource) throws IOException {
        return CommonUtils.loadResource(resource, line -> line.replaceAll("\\s", ""));
    }

    public long processOperations(List<String> operations, ToLongFunction<String> operationProcessor) {
        return operations.stream()
            .mapToLong(operationProcessor)
            .sum();
    }

    protected long processOperationPart1(String operation) {
        long total = 0;
        Operation currentOperation = Operation.SUM;
        char[] chars = operation.toCharArray();
        StringBuilder accNumber = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (Operation.isOperation(c)) {
                if (!accNumber.toString().isEmpty()) {
                    total = currentOperation.doOperation(total, Integer.parseInt(accNumber.toString()));
                    accNumber = new StringBuilder();
                }
                currentOperation = Operation.from(c);
            } else if (c == '(') {
                String subOperation = extractSubOperation(operation, i);
                i += subOperation.length() + 1;
                long subtotal = processOperationPart1(subOperation);
                total = currentOperation.doOperation(total, subtotal);
            } else if (i == chars.length - 1) {
                accNumber.append(c);
                total = currentOperation.doOperation(total, Integer.parseInt(accNumber.toString()));
            } else {
                accNumber.append(c);
            }
        }
        return total;
    }

    protected String extractSubOperation(String operation, int indexFrom) {
        int openedParenthesis = 1;
        int indexTo = indexFrom;
        while (openedParenthesis != 0) {
            indexTo++;
            if (operation.charAt(indexTo) == '(') {
                openedParenthesis++;
            } else if (operation.charAt(indexTo) == ')') {
                openedParenthesis--;
            }
        }

        return operation.substring(indexFrom + 1, indexTo);
    }

    protected long processOperationPart2(String operation) {
        while (operation.contains("+")) {
            operation = processIsolatedSumOperations(operation);
            operation = processIsolatedMultOperations(operation);
        }

        return processOperationPart1(operation);
    }

    protected String processIsolatedSumOperations(String operation) {
        Pattern compile = Pattern.compile("(?<Sum>(\\d+\\+)*\\d+)");
        return processPatternOperations(operation, compile);
    }

    protected String processIsolatedMultOperations(String operation) {
        Pattern compile = Pattern.compile("\\(((\\d+\\*)*\\d+)\\)");
        return processPatternOperations(operation, compile);
    }

    protected String processPatternOperations(String operation, Pattern compile) {
        Matcher matcher = compile.matcher(operation);
        int position = 0;
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            MatchResult matchResult = matcher.toMatchResult();
            sb.append(operation, position, matchResult.start());
            String group = matchResult.group();
            sb.append(processOperationPart1(group));
            position = (matchResult.start() + group.length());
        }
        sb.append(operation.substring(position));
        operation = sb.toString();
        return operation;
    }

    public enum Operation {
        SUM('+'),
        MULT('*');

        private final Character symbol;

        Operation(Character symbol) {
            this.symbol = symbol;
        }

        public static boolean isOperation(char c) {
            return c == '+' || c == '*';
        }

        public static Operation from(char c) {
            switch (c) {
                case '+':
                    return SUM;
                case '*':
                    return MULT;
                default:
                    throw new UnsupportedOperationException("Unsupported operation: " + c);
            }
        }

        public long doOperation(long number1, long number2) {
            switch (this) {
                case SUM:
                    return number1 + number2;
                case MULT:
                    return number1 * number2;
                default:
                    throw new IllegalArgumentException("Unsupported operation: " + this);
            }
        }
    }
}
