package org.odoral.adventofcode.y2020.day19;

import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.y2020.day19.model.DefaultRule;
import org.odoral.adventofcode.y2020.day19.model.Rule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MonsterMessages {

    public static void main(String[] args) throws IOException {
        MonsterMessages monsterMessages = new MonsterMessages();
        InputData inputData = monsterMessages.loadInputData("/input.txt");
        List<String> filteredValidMessages = monsterMessages.filterValidMessages(inputData, 0);
        log.info("Valid messages found: {}", filteredValidMessages.size());

        inputData = monsterMessages.loadInputData("/input.txt");
        inputData.loadNewRule("8: 42 | 42 8");
        inputData.loadNewRule("11: 42 31 | 42 11 31");
        filteredValidMessages = monsterMessages.filterValidMessages(inputData, 0);
        log.info("Valid messages for second part: {}", filteredValidMessages.size());

    }

    public InputData loadInputData(String resource) throws IOException {
        InputData inputData = new InputData();
        CommonUtils.loadResource(resource, inputData::load);
        return inputData;
    }

    public List<String> filterValidMessages(InputData inputData, int rule) {
        Rule mainRule = inputData.rules.get(rule);
        
        return inputData.messages.stream()
            .filter(message -> {
                log.debug(message);
                return mainRule.pass(message, 0)
                    .stream()
                    .anyMatch(length -> message.length() == length);
            })
            .collect(Collectors.toList());
    }

    public static class InputData {
        int loadState = 0;
        Map<Integer, Rule> rules = new HashMap<>();
        List<String> messages = new ArrayList<>();

        public InputData load(String line) {
            if(line.isEmpty()){
                loadState++;
            }else{
                switch (loadState){
                    case 0:
                        loadNewRule(line);
                        break;
                    case 1:
                        messages.add(line);
                        break;
                    default:
                        throw new UnsupportedOperationException("Unsupported load state: "+loadState);
                }
            }
            return this;
        }

        protected void loadNewRule(String line) {
            String [] fields = line.split(":");
            int ruleNumber = Integer.parseInt(fields[0]);
            String rule = fields[1].trim().replaceAll("\"", "");
            rules.put(ruleNumber, new DefaultRule(rules, rule, ruleNumber));
        }

    }

}
