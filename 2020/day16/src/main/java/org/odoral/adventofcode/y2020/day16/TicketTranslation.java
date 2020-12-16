package org.odoral.adventofcode.y2020.day16;

import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TicketTranslation {
    
    public static final Predicate<TicketNumber> COMPLIANT_WITH_ANY_RULE = t -> !t.passingRules.isEmpty();

    public static void main(String[] args) throws IOException {
        TicketTranslation ticketTranslation = new TicketTranslation();
        InputData inputData = ticketTranslation.loadInputData("/input.txt");
        int ticketScanningErrorRate = ticketTranslation.calculateTicketScanningErrorRate(inputData);
        log.info("Ticket scanning error rate: {}", ticketScanningErrorRate);
        long departureResult = ticketTranslation.getResultByField(inputData, text -> text.startsWith("departure"));
        log.info("Departure result: {}", departureResult);
    }

    public InputData loadInputData(String resource) throws IOException {
        InputData inputData = new InputData();

        //0: rules, 1: your ticket, 2: nearby tickets
        AtomicInteger parseStatus = new AtomicInteger(0);

        CommonUtils.loadResource(resource, line -> {
            if(line.isEmpty()){
                // Skip
            }else if(line.equals("your ticket:") || line.equals("nearby tickets:")){
                parseStatus.incrementAndGet();
            }else{
                switch (parseStatus.get()){
                    case 0:
                        inputData.rules.add(Rule.from(line));
                        break;
                    case 1:
                        inputData.yourTicket = Ticket.from(line);
                        break;
                    case 2:
                        inputData.nearbyTickets.add(Ticket.from(line));
                        break;
                    default:
                        throw new UnsupportedOperationException("Unsupported parse status: "+parseStatus.get());
                }
            }
            return null;
        });
        
        return inputData;
    }

    public int calculateTicketScanningErrorRate(InputData inputData) {
        processPassingRules(inputData);
        
        return inputData.nearbyTickets.stream()
            .flatMap(ticket -> ticket.numbers.stream())
            .filter(COMPLIANT_WITH_ANY_RULE.negate())
            .mapToInt(ticketNumber -> ticketNumber.number)
            .reduce(Integer::sum)
            .orElse(0);
    }

    public void processPassingRules(InputData inputData) {
        for (Ticket nearbyTicket : inputData.nearbyTickets) {
            nearbyTicket.numbers.forEach(ticketNumber -> ticketNumber.passingRules = inputData.rules
                .stream()
                .filter(rule -> rule.numberIsValid(ticketNumber.number))
                .collect(Collectors.toList()));
        }
    }

    public long getResultByField(InputData inputData, Predicate<String> isTargetField) {
        processPassingRules(inputData);
        
        Map<Integer, Map<Rule, Integer>> numberOfPassingRulesPerTicketPosition = new HashMap<>();
        AtomicInteger validTickets = new AtomicInteger(0);
        inputData.nearbyTickets.stream()
            .filter(Ticket::isValid)
            .forEach(ticket -> {
                validTickets.incrementAndGet();
                for (int position = 0; position < ticket.numbers.size(); position++) {
                    Map<Rule, Integer> passingRulesForPosition = numberOfPassingRulesPerTicketPosition.computeIfAbsent(position, k -> new HashMap<>());
                    for (Rule passingRule : ticket.numbers.get(position).passingRules) {
                        passingRulesForPosition.compute(passingRule, (k, v) -> {
                            if(Objects.isNull(v)){
                                return 1;
                            }else{
                                return v + 1;
                            }
                        });
                    }

                }
            });

        AtomicLong result = new AtomicLong(1);
        numberOfPassingRulesPerTicketPosition.entrySet()
            .stream()
            .map(e -> {
                Map<Rule, Integer> resultingMap = e.getValue()
                    .entrySet()
                    .stream()
                    .filter(entry -> entry.getValue() == validTickets.get())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                e.setValue(resultingMap);
                return e;
            })
            .sorted(Comparator.comparingInt(e -> e.getValue().size()))
            .forEach(e -> {
                Integer currentPosition = e.getKey();
                if(e.getValue().size() == 1){
                    Rule rule = e.getValue()
                        .keySet()
                        .stream()
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("WTF!"));
                    numberOfPassingRulesPerTicketPosition.entrySet()
                        .stream()
                        .filter(e1 -> !e1.getKey().equals(currentPosition))
                        .forEach(e1 -> e1.getValue().remove(rule));
                    if(isTargetField.test(rule.fieldName)){
                        log.info("Field name for position {} => {}", currentPosition, rule.fieldName);
                        result.set(result.get() * inputData.yourTicket.numbers.get(currentPosition).number);
                    }
                }else{
                    log.warn("Can't process: {}", e);
                }
            });

        return result.get();
    }

    public static class InputData{
        List<Rule> rules = new ArrayList<>();
        Ticket yourTicket;
        List<Ticket> nearbyTickets = new ArrayList<>();
    }

    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    public static class Rule {
        
        @EqualsAndHashCode.Include final String fieldName;
        final List<ValidRange> validRanges;
        
        public static Rule from(String ruleConfiguration){
            String [] fields = ruleConfiguration.split(":");
            List<ValidRange> validRanges = Arrays.stream(fields[1].split(" or "))
                .map(String::trim)
                .map(ValidRange::from)
                .collect(Collectors.toList());
            return new Rule(fields[0], validRanges);
        }

        public boolean numberIsValid(Integer number) {
            return validRanges.stream().anyMatch(validRange -> validRange.numberIsValid(number));
        }
    }

    @AllArgsConstructor
    @ToString
    public static class ValidRange {
        
        final int from;
        final int to;
        
        public static ValidRange from(String rangeConfiguration){
            String [] fields = rangeConfiguration.split("-");
            return new ValidRange(Integer.parseInt(fields[0]), Integer.parseInt(fields[1]));
        }

        public boolean numberIsValid(Integer number) {
            return number >= from && number <= to;
        }
    }
    
    @AllArgsConstructor
    @ToString
    public static class Ticket{
        final List<TicketNumber> numbers;
        
        public static Ticket from(String ticketConfiguration){
            return new Ticket(Arrays.stream(ticketConfiguration.split(","))
                .map(TicketNumber::from)
                .collect(Collectors.toList())
            );
        }
        
        public boolean isValid(){
            return numbers.stream()
                .noneMatch(ticketNumber -> ticketNumber.passingRules.isEmpty());
        }
    }
    
    @RequiredArgsConstructor
    public static class TicketNumber{
        final Integer number;
        List<Rule> passingRules;
        
        public static TicketNumber from(String number){
            return new TicketNumber(Integer.valueOf(number));
        }
    }
}
