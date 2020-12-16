package org.odoral.adventofcode.y2015.day1;

import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.y2015.day1.exception.InstructionException;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotQuiteLisp {

    public static void main(String[] args) throws IOException {
        NotQuiteLisp notQuiteLisp = new NotQuiteLisp();
        List<String> instructions = notQuiteLisp.loadInput("/input.txt");
        int finalFloor = notQuiteLisp.calculateTargetFloor(instructions, 0);
        log.info("Santa has to go to floor: {}", finalFloor);

        int number = notQuiteLisp.instructionsToReach(instructions, 0, -1);
        log.info("Santa needs {} instructions to reach basement", number);
    }

    public List<String> loadInput(String resource) throws IOException {
        return CommonUtils.loadResource(resource, Function.identity());
    }

    public int calculateTargetFloor(List<String> instructions, int startingFloor) {
        int floorToGo = instructions.stream()
            .flatMap(instructionLine -> Stream.of(CommonUtils.toCharacterArray(instructionLine)))
            .mapToInt(this::resolveMovement)
            .sum();
        
        return startingFloor + floorToGo;
    }

    public int resolveMovement(Character instruction) {
        switch (instruction) {
            case '(':
                return 1;
            case ')':
                return -1;
            default:
                throw new InstructionException("Unsupported instruction: " + instruction);
        }
    }

    public int instructionsToReach(List<String> instructions, int startingFloor, int targetFloor) {
        int instructionCount = 0;
        int currentFloor = startingFloor;

        for (String instructionLine : instructions) {
            for (char instruction : instructionLine.toCharArray()) {
                currentFloor+=resolveMovement(instruction);
                instructionCount++;
                if(currentFloor == targetFloor){
                    return instructionCount;
                }
            }
        }
        
        return instructionCount;
    }
}
