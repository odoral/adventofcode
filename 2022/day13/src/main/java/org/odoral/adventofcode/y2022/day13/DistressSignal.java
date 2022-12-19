package org.odoral.adventofcode.y2022.day13;

import org.apache.commons.lang3.StringUtils;
import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.exception.AdventOfCodeException;
import org.odoral.adventofcode.common.model.KeyValue;
import org.odoral.adventofcode.common.model.MutableKeyValue;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("java:S3740")
public class DistressSignal {

    public static void main(String[] args) throws IOException {
        DistressSignal distressSignal = new DistressSignal();
        List<KeyValue<List, List>> signalPairs = DistressSignal.Input.loadSignals("/input.txt");

        int total = distressSignal.sumIndicesWithRightOrder(signalPairs);
        log.info("Total part one: {}", total);

        total = distressSignal.getDecoderKey(signalPairs);
        log.info("Total part two: {}", total);
    }

    public int sumIndicesWithRightOrder(List<KeyValue<List, List>> signalPairs) {
        return IntStream.range(0, signalPairs.size())
            .map(index -> {
                KeyValue<List, List> signalPair = signalPairs.get(index);
                return rightOrder(signalPair.getKey(), signalPair.getValue())
                    .map(result -> result ? index + 1 : 0)
                    .orElse(index + 1);
            }).sum();
    }

    public int getDecoderKey(List<KeyValue<List, List>> signalPairs) {
        List<List> dividerPackets = Arrays.asList(
            Collections.singletonList(2),
            Collections.singletonList(6)
        );
        List<List> signals = getSignalsWithDividerPackets(signalPairs, dividerPackets);

        signals.sort((l1, l2) -> rightOrder(l1, l2)
            .map(r -> r ? -1 : 1)
            .orElse(0));

        return IntStream.range(0, signals.size())
            .filter(index -> isDividerPacket(signals.get(index), dividerPackets))
            .map(index -> index + 1)
            .reduce(1, Math::multiplyExact);
    }

    protected List<List> getSignalsWithDividerPackets(List<KeyValue<List, List>> signalPairs, List<List> dividerPackets) {
        List<List> signals = signalPairs.stream()
            .flatMap(signal -> Stream.of(signal.getKey(), signal.getValue()))
            .collect(Collectors.toList());

        signals.addAll(dividerPackets);

        return signals;
    }

    protected boolean isDividerPacket(List signal, List<List> dividerPackets) {
        return dividerPackets.stream()
            .anyMatch(dividerPacket -> dividerPacket == signal);
    }

    protected Optional<Boolean> rightOrder(List left, List right) {
        Optional<Boolean> result = Optional.empty();
        for (int i = 0; i < Math.min(left.size(), right.size()); i++) {
            Object leftObject = left.get(i);
            Object rightObject = right.get(i);
            if (leftObject instanceof Integer && rightObject instanceof Integer) {
                result = rightOrder((Integer) leftObject, (Integer) rightObject);
            } else if (leftObject instanceof Integer) {
                result = rightOrder(Collections.singletonList(leftObject), (List) rightObject);
            } else if (rightObject instanceof Integer) {
                result = rightOrder((List) leftObject, Collections.singletonList(rightObject));
            } else {
                result = rightOrder((List) leftObject, (List) rightObject);
            }
            if (result.isPresent()) {
                break;
            }
        }
        if (!result.isPresent()) {
            if (left.size() < right.size()) {
                result = Optional.of(true);
            } else if (left.size() > right.size()) {
                result = Optional.of(false);
            }
        }
        return result;
    }

    protected Optional<Boolean> rightOrder(Integer leftObject, Integer rightObject) {
        Optional<Boolean> result = Optional.empty();
        if (leftObject < rightObject) {
            result = Optional.of(true);
        } else if (leftObject > rightObject) {
            result = Optional.of(false);
        }
        return result;
    }

    public static class Input {

        public static List<KeyValue<List, List>> loadSignals(String resource) throws IOException {
            List<KeyValue<List, List>> input = new ArrayList<>();
            MutableKeyValue<List, List> current = new MutableKeyValue<>();
            AtomicInteger side = new AtomicInteger();
            CommonUtils.loadResource(resource, Function.identity()).stream()
                .forEach(line -> {
                    if (StringUtils.isBlank(line)) {
                        input.add(current.getImmutable());
                        side.set(0);
                    } else {
                        List parseResult = parseLine(line);
                        if (side.getAndIncrement() == 0) {
                            current.setKey(parseResult);
                        } else {
                            current.setValue(parseResult);
                        }
                    }
                });
            input.add(current.getImmutable());
            return input;
        }

        public static List parseLine(String line) {
            List currentList = null;
            List mainList = null;
            Deque<List> lists = new ArrayDeque<>();
            StringBuilder number = new StringBuilder();
            for (int lineIndex = 0; lineIndex < line.length(); lineIndex++) {
                char character = line.charAt(lineIndex);
                switch (character) {
                    case '[':
                        List newList = new ArrayList();
                        if (currentList != null) {
                            currentList.add(newList);
                        }
                        currentList = newList;
                        lists.add(newList);
                        if (mainList == null) {
                            mainList = currentList;
                        }
                        break;
                    case ']':
                        number = accumulateNumber(number, currentList);
                        lists.pop();
                        if (!lists.isEmpty()) {
                            currentList = lists.peek();
                        } else {
                            currentList = null;
                        }
                        break;
                    case ',':
                        number = accumulateNumber(number, currentList);
                        break;
                    default:
                        number.append(character);
                }
            }

            return mainList;
        }

        public static StringBuilder accumulateNumber(StringBuilder number, List currentList) {
            if (number.length() > 0) {
                Optional.ofNullable(currentList)
                    .orElseThrow(() -> new AdventOfCodeException(""))
                    .add(Integer.valueOf(number.toString()));
                number = new StringBuilder();
            }
            return number;
        }
    }

}