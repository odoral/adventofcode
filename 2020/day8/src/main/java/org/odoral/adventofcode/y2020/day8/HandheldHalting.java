package org.odoral.adventofcode.y2020.day8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HandheldHalting {

    public static final String OPERATION_ACC = "acc";
    public static final String OPERATION_JMP = "jmp";
    public static final String OPERATION_NOP = "nop";

    public static void main(String [] args) throws Exception{
        HandheldHalting handheldHalting = new HandheldHalting();
        List<Instruction> instructions = handheldHalting.loadInstructions(handheldHalting.getClass().getResourceAsStream("/input.txt"));
        log.info("Loaded {} instructions.", instructions.size());
        long accumulatorBeforeLoop = handheldHalting.calculateAccumulatorBeforeLoop(instructions).accumulator;
        log.info("Accumulator before loop is: {}", accumulatorBeforeLoop);

        Result result = handheldHalting.fixInfiniteLoop("/input.txt");
        log.info("Accumulator after fix: {} [{}]", result.accumulator, result.infiniteLoop);
    }

    public List<Instruction> loadInstructions(InputStream inputStream) throws IOException{
        try(BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            return br.lines()
                .map(Instruction::loadInstruction)
                .collect(Collectors.toList());
        }
    }
    
    public Result calculateAccumulatorBeforeLoop(List<Instruction> instructions){
        long accumulator = 0;
        int lastIndexBeforeInfiniteLoop = -1;
        int lastIndex = -1;
        int index = 0;
        
        while(index >= 0 && index < instructions.size()){
            Instruction instruction = instructions.get(index);
            log.info("Processing: {} | {}", index, instruction);
            if(instruction.used){
                lastIndex = index;
                index = -1;
            }else{
                instruction.used = true;
                switch (instruction.operation){
                    case OPERATION_ACC:
                        lastIndexBeforeInfiniteLoop = index;
                        index++;
                        accumulator+=instruction.argument;
                        break;
                    case OPERATION_JMP:
                        lastIndexBeforeInfiniteLoop = index;
                        index+=instruction.argument;
                        break;
                    case OPERATION_NOP:
                        lastIndexBeforeInfiniteLoop = index;
                        index++;
                        break;
                    default:
                        throw new UnsupportedOperationException("Unsupported instruction: "+instruction);
                }
            }
        }
        
        if(index == -1) {
            log.info("Infinite loop starts at {} which came from {}", lastIndex, lastIndexBeforeInfiniteLoop);
        }
        
        return new Result(accumulator, index == -1);
    }

    public Result fixInfiniteLoop(String inputPath) throws IOException {
        Result result = new Result(-1, true);
        int index = 0;
        List<Instruction> instructions = loadInstructions(getClass().getResourceAsStream(inputPath));
        while (result.infiniteLoop && index < instructions.size()){
            instructions = loadInstructions(getClass().getResourceAsStream(inputPath));
            for (; index < instructions.size(); index++) {
                Instruction instruction = instructions.get(index);
                boolean updated = false;
                if (instruction.operation.equals(OPERATION_NOP)){
                    instruction.operation = OPERATION_JMP;
                    result = calculateAccumulatorBeforeLoop(instructions);
                    index++;
                    updated = true;
                }else if(instruction.operation.equals(OPERATION_JMP)){
                    instruction.operation = OPERATION_NOP;
                    result = calculateAccumulatorBeforeLoop(instructions);
                    index++;
                    updated = true;
                }
                if(updated){
                    break;
                }
            }
        }
        
        log.info("Fixed at: {}", index);
        
        return result;
    }
    
    public static class Instruction{

        String operation;
        final long argument;
        boolean used;

        public Instruction(String operation, long argument){
            this.operation = operation;
            this.argument = argument;
            this.used = false;
        }

        public static Instruction loadInstruction(String config){
            String [] fields = config.split(" ");
            return new Instruction(fields[0], Long.parseLong(fields[1]));
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Instruction{");
            sb.append("operation='").append(operation).append('\'');
            sb.append(", argument=").append(argument);
            sb.append(", used=").append(used);
            sb.append('}');
            return sb.toString();
        }
    }
    
    public class Result{
        final long accumulator;
        final boolean infiniteLoop;

        public Result(long accumulator, boolean infiniteLoop) {
            this.accumulator = accumulator;
            this.infiniteLoop = infiniteLoop;
        }
    }

    
}