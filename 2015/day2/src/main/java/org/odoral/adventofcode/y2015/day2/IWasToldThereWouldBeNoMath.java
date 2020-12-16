package org.odoral.adventofcode.y2015.day2;

import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IWasToldThereWouldBeNoMath {

    public static void main(String[] args) throws IOException {
        IWasToldThereWouldBeNoMath iWasToldThereWouldBeNoMath = new IWasToldThereWouldBeNoMath();

        List<Dimension> dimensions = iWasToldThereWouldBeNoMath.loadScenario("/input.txt");
        int paperNeeded = iWasToldThereWouldBeNoMath.calculatePaperNeeded(dimensions);
        log.info("Total paper needed: {}", paperNeeded);
        int ribbonNeeded = iWasToldThereWouldBeNoMath.calculateRibbonNeeded(dimensions);
        log.info("Total ribbon needed: {}", ribbonNeeded);
    }

    public List<Dimension> loadScenario(String resource) throws IOException {
        return CommonUtils.loadResource(resource, Dimension::from);
    }

    public int calculatePaperNeeded(List<Dimension> dimensions) {
        return dimensions.stream()
            .mapToInt(d -> d.neededPaper() + d.extraPaper())
            .sum();
    }

    public int calculateRibbonNeeded(List<Dimension> dimensions) {
        return dimensions.stream()
            .mapToInt(Dimension::neededRibbon)
            .sum();
    }

    @RequiredArgsConstructor
    public static class Dimension{
        final int l;
        final int w;
        final int h;

        public static Dimension from(String config) {
            String[] fields = config.split("x");
            return new Dimension(
                Integer.parseInt(fields[0]), 
                Integer.parseInt(fields[1]), 
                Integer.parseInt(fields[2]));
        }
        
        public int neededPaper(){
            return 2*l*w + 2*w*h + 2*h*l;
        }
        
        public int extraPaper(){
            return Stream.of(l, w, h)
                .sorted()
                .limit(2)
                .reduce(1, (i1, i2) -> i1 * i2);
        }
        
        public int neededRibbon(){
            return Stream.of(l, w, h)
                .sorted()
                .limit(2)
                .mapToInt(i -> i*2)
                .reduce(0, Integer::sum) + l*w*h;
        }
    }
}
