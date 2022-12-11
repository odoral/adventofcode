package org.odoral.adventofcode.y2022.day10;

import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CathodeRayTube {

    public static final String NOOP = "noop";
    public static final String ADDX = "addx";

    public static void main(String[] args) throws IOException {
        CathodeRayTube cathodeRayTube = new CathodeRayTube();
        List<String> instructions = CommonUtils.loadResource("/input.txt", Function.identity());

        int total = cathodeRayTube.getSumSignalStrengths(instructions, 6);
        log.info("Sum of these six signal strengths: {}", total);

        String drawResult = cathodeRayTube.drawInstructions(instructions);
        log.info("Draw result:\n{}", drawResult);
    }

    protected int getSumSignalStrengths(List<String> instructions, long limitSignalStrengths) {
        List<Integer> signalStrengths = new ArrayList<>();
        processInstructions(instructions, (cycle, registerX) -> {
            if (isCycleToTakeSignalStrength(cycle)) {
                signalStrengths.add(cycle * registerX);
            }
        });

        return signalStrengths.stream()
            .limit(limitSignalStrengths)
            .mapToInt(i -> i)
            .sum();
    }

    protected boolean isCycleToTakeSignalStrength(int cycle) {
        return cycle != 0 && (cycle - 20) % 40 == 0;
    }

    protected String drawInstructions(List<String> instructions) {
        StringBuilder sb = new StringBuilder();
        processInstructions(instructions, (cycle, registerX) -> {
            if (spriteOverlaps(cycle, registerX)) {
                sb.append("#");
            } else {
                sb.append(".");
            }
        });

        return IntStream.range(0, sb.length() / 40)
            .mapToObj(i -> sb.substring(i * 40, (i * 40) + 40))
            .collect(Collectors.joining("\n"));
    }

    protected boolean spriteOverlaps(int cycle, int registerX) {
        int cycleValue = cycle % 40;
        if (cycleValue == 0) {
            cycleValue = 40;
        }
        return cycleValue >= registerX && cycleValue <= registerX + 2;
    }

    protected void processInstructions(List<String> instructions, BiConsumer<Integer, Integer> cycleRegisterXConsumer) {
        AtomicInteger registerX = new AtomicInteger(1);
        AtomicInteger cycle = new AtomicInteger(1);

        for (String instruction : instructions) {
            String[] instructionFields = instruction.split("\\s");

            switch (instructionFields[0]) {
                case NOOP:
                    doNoop(cycle, registerX, cycleRegisterXConsumer);
                    break;
                case ADDX:
                    doAddx(cycle, registerX, Integer.parseInt(instructionFields[1]), cycleRegisterXConsumer);
                    break;
            }
        }
    }

    protected void doNoop(AtomicInteger cycle, AtomicInteger registerX, BiConsumer<Integer, Integer> cycleRegisterXConsumer) {
        cycleRegisterXConsumer.accept(cycle.get(), registerX.get());
        cycle.incrementAndGet();
    }

    protected void doAddx(AtomicInteger cycle, AtomicInteger registerX, int delta, BiConsumer<Integer, Integer> cycleRegisterXConsumer) {
        for (int addxCycle = 0; addxCycle < 2; addxCycle++) {
            cycleRegisterXConsumer.accept(cycle.get(), registerX.get());
            cycle.incrementAndGet();
        }
        registerX.addAndGet(delta);
    }

}