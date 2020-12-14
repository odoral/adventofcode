package org.odoral.adventofcode.y2020.day14;

import org.apache.commons.lang3.StringUtils;
import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DockingData {

    public static void main(String[] args) throws IOException {
        DockingData dockingData = new DockingData();
        List<DockingData.Instruction> instructions = dockingData.loadInstructions("/input.txt");
        
        Map<Long, String> addresses = dockingData.processInstructionsV1(instructions);
        log.info("Result for decode chip v1: {}", dockingData.calculateSum(addresses));
        
        addresses = dockingData.processInstructionsV2(instructions);
        log.info("Result for decode chip v2: {}", dockingData.calculateSum(addresses));
    }
    
    public List<Instruction> loadInstructions(String resource) throws IOException {
        return CommonUtils.loadResource(resource, Instruction::load);
    }

    public Map<Long, String> processInstructionsV1(List<Instruction> instructions) {
        Map<Long, String> result = new HashMap<>();
        String currentMask = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";

        for (Instruction instruction : instructions) {
            if(instruction.isMaskChange()){
                currentMask = instruction.value;
            }else{
                Integer address = instruction.getAddress();
                result.put(address.longValue(), logicV1(instruction.getMemoryValue(), currentMask));
            }
        }
        return result;
    }
    
    protected String logicV1(Long memValue, String mask) {
        StringBuilder result = new StringBuilder();

        String memBinaryValue = toBinary(memValue);
        for (int i = 0; i < 36; i++) {
            char maskChar = mask.charAt(i);
            if(maskChar == '0' || maskChar == '1'){
                result.append(maskChar);
            }else {
                result.append(memBinaryValue.charAt(i));
            }
        }
        
        return result.toString();
    }

    protected String toBinary(Long memValue) {
        return StringUtils.leftPad(Long.toBinaryString(memValue), 36, '0');
    }

    public long calculateSum(Map<Long, String> addresses){
        return addresses.values()
            .stream()
            .mapToLong(this::fromBinary)
            .sum();
    }

    protected Long fromBinary(String binaryNumber){
        return new BigInteger(binaryNumber, 2).longValue();
    }

    public Map<Long, String> processInstructionsV2(List<Instruction> instructions) {
        Map<Long, String> result = new HashMap<>();
        String currentMask = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";

        for (Instruction instruction : instructions) {
            if(instruction.isMaskChange()){
                currentMask = instruction.value;
            }else{
                Integer address = instruction.getAddress();
                Long memoryValue = instruction.getMemoryValue();
                List<Long> maskedAddresses = computeMaskedAddresses(address, currentMask);
                for (Long maskedAddress : maskedAddresses) {
                    result.put(maskedAddress, toBinary(memoryValue));
                }
            }
        }
        return result;
    }

    protected List<Long> computeMaskedAddresses(Integer address, String currentMask) {
        String binaryAddress = toBinary(address.longValue());
        List<StringBuilder> resultingAddresses = new ArrayList<>();
        resultingAddresses.add(new StringBuilder());
        for (int i = 0; i < currentMask.length(); i++) {
            char maskChar = currentMask.charAt(i);
            final char originalChar = binaryAddress.charAt(i);
            switch (maskChar){
                case '0':
                    resultingAddresses = resultingAddresses.stream()
                        .map(a -> a.append(originalChar))
                        .collect(Collectors.toList());
                    break;
                case '1':
                    resultingAddresses = resultingAddresses.stream()
                        .map(a -> a.append(maskChar))
                        .collect(Collectors.toList());
                    break;
                case 'X':
                    resultingAddresses = resultingAddresses.stream()
                        .flatMap(a -> {
                            StringBuilder other = new StringBuilder(a.toString());
                            a.append('0');
                            other.append('1');
                            return Stream.of(a, other);
                        })
                        .collect(Collectors.toList());
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported char for mask: "+maskChar);
            }
        }
        
        return resultingAddresses.stream()
            .map(StringBuilder::toString)
            .map(this::fromBinary)
            .collect(Collectors.toList());
    }

    @AllArgsConstructor
    public static class Instruction {
        final String type;
        final String value;
        
        public static Instruction load(String instruction){
            String[] fields = instruction.split("=");
            return new Instruction(fields[0].trim(), fields[1].trim());
        }
        
        public boolean isMaskChange(){
            return "mask".equals(type);
        }

        public Integer getAddress(){
            return Integer.valueOf(type.split("\\[")[1].split("]")[0]);
        }
        
        public Long getMemoryValue(){
            return Long.valueOf(value);
        }
    }
}
