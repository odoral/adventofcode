package org.odoral.adventofcode.y2022.day21;

import org.apache.commons.lang3.StringUtils;
import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.exception.AdventOfCodeException;
import org.odoral.adventofcode.y2022.day21.model.MonkeyAction;
import org.odoral.adventofcode.y2022.day21.model.YellNumberAction;
import org.odoral.adventofcode.y2022.day21.model.YellOperationResultAction;

import java.io.IOException;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MonkeyMath {

    public static final String ROOT_MONKEY_NAME = "root";
    public static final String MY_MONKEY_NAME = "humn";

    public static void main(String[] args) throws IOException {
        MonkeyMath monkeyMath = new MonkeyMath();
        long number = monkeyMath.whatNumberWillYell(ROOT_MONKEY_NAME);
        log.info("Monkey {} will yell {}", ROOT_MONKEY_NAME, number);

        number = monkeyMath.whatNumberDoIHaveToYell(-1_000_000_000);
        log.info("Monkey {} will yell with number: {}", MY_MONKEY_NAME, number);
    }

    public long whatNumberWillYell(String monkeyName) throws IOException {
        Map<String, MonkeyAction> monkeyActions = Input.load();
        return monkeyActions.get(monkeyName).yell(monkeyActions);
    }

    public long whatNumberDoIHaveToYell(int initialStep) throws IOException {
        Map<String, MonkeyAction> monkeyActions = Input.load();
        YellOperationResultAction rootMonkey = (YellOperationResultAction) monkeyActions.get(ROOT_MONKEY_NAME);
        YellNumberAction me = (YellNumberAction) monkeyActions.get(MY_MONKEY_NAME);

        long numberToEvaluate = lookForNumberUsingDynamicStepping(initialStep, monkeyActions, rootMonkey, me);

        return lookForLowerCandidate(monkeyActions, rootMonkey, me, numberToEvaluate);
    }

    protected long lookForNumberUsingDynamicStepping(int initialStep, Map<String, MonkeyAction> monkeyActions, YellOperationResultAction rootMonkey, YellNumberAction me) throws IOException {
        long numberToEvaluate = 0;
        me.setNumber(String.valueOf(numberToEvaluate));
        Long[] monkeyNumbers = rootMonkey.yellTwoNumbers(monkeyActions);
        long step = initialStep;
        long direction = getDirection();
        while (!monkeyNumbers[0].equals(monkeyNumbers[1])) {
            if ((monkeyNumbers[0] > monkeyNumbers[1] && step > 0) || (monkeyNumbers[0] < monkeyNumbers[1] && step < 0)) {
                step /= -10;
            }
            numberToEvaluate += step * direction;
            me.setNumber(String.valueOf(numberToEvaluate));
            monkeyNumbers = rootMonkey.yellTwoNumbers(monkeyActions);
        }
        return numberToEvaluate;
    }

    protected long lookForLowerCandidate(Map<String, MonkeyAction> monkeyActions, YellOperationResultAction rootMonkey, YellNumberAction me, long numberToEvaluate) {
        return LongStream.rangeClosed(numberToEvaluate - 10L, numberToEvaluate + 10L)
            .filter(numberToTest -> {
                me.setNumber(String.valueOf(numberToTest));
                Long[] monkeyNumbersToCheck = rootMonkey.yellTwoNumbers(monkeyActions);
                return monkeyNumbersToCheck[0].equals(monkeyNumbersToCheck[1]);
            }).min()
            .orElseThrow(() -> new AdventOfCodeException("Not found"));
    }

    protected long getDirection() throws IOException {
        Map<String, MonkeyAction> monkeyActions = Input.load();
        YellOperationResultAction rootMonkey = (YellOperationResultAction) monkeyActions.get(ROOT_MONKEY_NAME);
        YellNumberAction me = (YellNumberAction) monkeyActions.get(MY_MONKEY_NAME);

        me.setNumber("0");
        long number1 = rootMonkey.yellTwoNumbers(monkeyActions)[0];
        me.setNumber("100");
        long number2 = rootMonkey.yellTwoNumbers(monkeyActions)[0];
        return number2 > number1 ? 1 : -1;
    }

    public static class Input {

        public static Map<String, MonkeyAction> load() throws IOException {
            return CommonUtils.loadResource("/input.txt", line -> {
                    String[] fields = line.split(": ");
                    if (StringUtils.isNumeric(fields[1])) {
                        return YellNumberAction.parse(fields[0], fields[1]);
                    } else {
                        return YellOperationResultAction.parse(fields[0], fields[1]);
                    }
                }).stream()
                .collect(Collectors.toMap(MonkeyAction::getMonkeyName, Function.identity()));
        }
    }

}