package org.odoral.adventofcode.y2020.day7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HandyHaversacks {

    public static final String BAG_ID_SHINY_GOLD = "shiny gold";
    public static final int MIN_AMOUNT_TO_SEARCH = 1;

    public static void main(String[] args) throws IOException {
        HandyHaversacks handyHaversacks = new HandyHaversacks();

        Map<String, Bag> bagMap = handyHaversacks.loadBagConfiguration("/input.txt");
        
        String bagIdToSearch = BAG_ID_SHINY_GOLD;
        long minAmountToSearch = MIN_AMOUNT_TO_SEARCH;

        long validBags = handyHaversacks.countHowManyContains(bagMap, bagIdToSearch, minAmountToSearch);

        log.info("Total bags to contain: {} => {}", bagIdToSearch, validBags);

        log.info("Total bags in: {} => {}", bagIdToSearch, handyHaversacks.totalBags(bagMap.get(bagIdToSearch), bagMap));
    }

    public long countHowManyContains(Map<String, Bag> bagMap, String bagIdToSearch, long minAmountToSearch) {
        return bagMap.entrySet().stream().filter(e -> {
            String bagID = e.getKey();
            Bag bagConfig = e.getValue();

            if(bagID.equals(bagIdToSearch)){
                return false;
            }else{
                return contains(bagConfig.contains, bagIdToSearch, minAmountToSearch);
            }
        }).count();
    }

    protected static boolean contains(List<NumberedBag> numberedBags, String bagID, long minAmountToSearch){
        for (NumberedBag numberedBag : numberedBags) {
            if (numberedBag.bag.bagID.equals(bagID) && numberedBag.amount >= minAmountToSearch ||
                contains(numberedBag.bag.contains, bagID, minAmountToSearch)) {
                return true;
            }
        }
        return false;
    }

    protected long totalBags(Bag bag, Map<String, Bag> bagMap){
        long totalContainedBags = 0;
        for (int i = 0; i < bag.contains.size(); i++) {
            totalContainedBags += totalBags(bag.contains.get(i), bagMap);
        }
        return totalContainedBags;
    }

    protected long totalBags(NumberedBag numberedBag, Map<String, Bag> bagMap){
        Bag bag = bagMap.get(numberedBag.bag.bagID);

        return numberedBag.amount + numberedBag.amount * totalBags(bag, bagMap);
    }

    protected Map<String, Bag> loadBagConfiguration(String inputPath) throws IOException {
        Map<String, Bag> bagConfiguration = new HashMap<>();

        try(BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(inputPath)))){
            br.lines()
                .forEach(bagConfig -> {
                    String [] fields = bagConfig.split(" contain ");
                    String bagID = getBagConfigID(fields[0]);

                    Bag bag = bagConfiguration.computeIfAbsent(bagID, Bag::new);

                    if(!bagConfig.endsWith("contain no other bags.")){
                        String [] bags = fields[1].split(", ");
                        for (String containedBagConfig : bags) {
                            fields = containedBagConfig.split(" ");
                            long amount = Long.parseLong(fields[0]);
                            bagID = String.join(" ", fields[1], fields[2]);
                            Bag containedBag = bagConfiguration.computeIfAbsent(bagID, Bag::new);

                            bag.contains.add(new NumberedBag(containedBag, amount));
                        }
                    }
                });
        }

        log.info("Loaded: {} bags", bagConfiguration.size());
        return bagConfiguration;
    }

    protected static String getBagConfigID(String config){
        String [] fields = config.split(" ");
        return String.join(" ", fields[0], fields[1]);
    }

    public static class Bag{
        final String bagID;
        List<NumberedBag> contains = new ArrayList<>();

        private Bag(String bagID){
            this.bagID=bagID;
        }
    }

    public static class NumberedBag{
        final Bag bag;
        final long amount;

        private NumberedBag(Bag bag, long amount){
            this.bag = bag;
            this.amount = amount;
        }
    }

}