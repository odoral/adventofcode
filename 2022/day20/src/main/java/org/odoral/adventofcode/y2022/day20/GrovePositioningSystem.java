package org.odoral.adventofcode.y2022.day20;

import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.exception.AdventOfCodeException;
import org.odoral.adventofcode.common.model.Chain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GrovePositioningSystem {

    public static final long DECRYPTION_KEY = 811589153L;
    public static final int MIX_COUNT = 10;

    public static void main(String[] args) throws IOException {
        GrovePositioningSystem grovePositioningSystem = new GrovePositioningSystem();
        long total = grovePositioningSystem.getSumOfTheThreeNumbersThatFormTheGroveCoordinates();
        log.info("Total for part 1: {}", total);

        total = grovePositioningSystem.getSumOfTheThreeNumbersThatFormTheGroveCoordinates(DECRYPTION_KEY, MIX_COUNT);
        log.info("Total for part 2: {}", total);
    }

    public long getSumOfTheThreeNumbersThatFormTheGroveCoordinates() throws IOException {
        return getSumOfTheThreeNumbersThatFormTheGroveCoordinates(1, 1);
    }

    public long getSumOfTheThreeNumbersThatFormTheGroveCoordinates(long decryptionKey, int mixCount) throws IOException {
        Chain<Long> firstChain = Input.load();

        Chain<Long> zeroChain = getChainWithValue(firstChain, 0);
        List<Chain<Long>> initialOrder = chainToList(firstChain, decryptionKey);

        for (int i = 0; i < mixCount; i++) {
            mixChain(initialOrder);
        }

        log.info("Chain: {}", chainToString(zeroChain));
        return Stream.of(
                getChainAt(zeroChain, 1000, true, initialOrder.size()).getValue(),
                getChainAt(zeroChain, 2000, true, initialOrder.size()).getValue(),
                getChainAt(zeroChain, 3000, true, initialOrder.size()).getValue())
            .mapToLong(i -> i)
            .sum();

    }

    protected Chain<Long> getChainWithValue(Chain<Long> firstChain, int value) {
        while (firstChain.getValue() != value) {
            firstChain = firstChain.getNext();
        }
        return firstChain;
    }

    protected List<Chain<Long>> chainToList(Chain<Long> firstChain) {
        return chainToList(firstChain, 1);
    }

    protected List<Chain<Long>> chainToList(Chain<Long> firstChain, long decryptionKey) {
        List<Chain<Long>> initialOrder = new ArrayList<>();
        Chain<Long> currentChain = firstChain;

        do {
            currentChain.setValue(currentChain.getValue() * decryptionKey);
            initialOrder.add(currentChain);
        } while ((currentChain = currentChain.getNext()) != firstChain);
        return initialOrder;
    }

    protected void mixChain(List<Chain<Long>> initialOrder) {
        log.info("Sorting {} long chain", initialOrder.size());

        for (Chain<Long> chain : initialOrder) {
            if (chain.getValue() % (initialOrder.size() - 1) != 0) {
                Chain<Long> newChainPosition = getChainAt(chain, chain.getValue(), false, initialOrder.size() - 1);

                if (newChainPosition == chain) {
                    throw new AdventOfCodeException("Illegal state.");
                }

                if (chain.getValue() > 0) {
                    newChainPosition.insertAfter(chain.remove());
                } else if (chain.getValue() < 0) {
                    newChainPosition.insertBefore(chain.remove());
                }
            }
        }
    }

    protected Chain<Long> getChainAt(Chain<Long> currentChain, long relativePosition, boolean considerCurrentChain, int chainSize) {
        long count = Math.abs(relativePosition) % chainSize;
        UnaryOperator<Chain<Long>> move;
        if (relativePosition > 0) {
            move = Chain::getNext;
        } else if (relativePosition < 0) {
            move = Chain::getPrevious;
        } else {
            move = UnaryOperator.identity();
        }
        int index = 0;
        Chain<Long> chain = currentChain;
        while (++index <= count) {
            chain = move.apply(chain);
            if (!considerCurrentChain && chain == currentChain) {
                chain = move.apply(chain);
            }
        }

        return chain;
    }

    protected String chainToString(Chain<Long> chain) {
        Chain<Long> currentChain = chain.getNext();
        List<Long> values = new ArrayList<>();
        values.add(chain.getValue());
        while (currentChain != chain) {
            values.add(currentChain.getValue());
            currentChain = currentChain.getNext();
        }

        return values.stream().map(String::valueOf).collect(Collectors.joining(", "));
    }

    public static class Input {
        public static Chain<Long> load() throws IOException {
            return toChain(CommonUtils.loadResource("/input.txt", Long::parseLong));
        }

        public static Chain<Long> toChain(List<Long> chainElements) {
            Chain.Builder<Long> chainBuilder = new Chain.Builder<>();
            chainElements.stream()
                .map(Chain::new)
                .forEach(chainBuilder::addChain);
            Chain<Long>[] chain = chainBuilder.build();
            Chain<Long> firstChain = chain[0];
            Chain<Long> finalChain = chain[1];
            firstChain.setPrevious(finalChain);
            finalChain.setNext(firstChain);

            return firstChain;
        }
    }

}