package org.odoral.adventofcode.y2021.day13;

import org.apache.commons.lang3.StringUtils;
import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.model.Point;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransparentOrigami {

    public static final char POINT_MARK = '#';

    public static void main(String[] args) throws IOException {
        TransparentOrigami transparentOrigami = new TransparentOrigami();
        TransparentOrigami.Instructions instructions = TransparentOrigami.loadInstructions("/input.txt");

        // Only first fold
        instructions = new Instructions(instructions.points, Arrays.asList(instructions.folds.get(0)));

        TransparentOrigami.Result result = transparentOrigami.calculateVisibleDots(instructions);
        log.info("Result has {} visible dots after first fold", result.getVisibleDots());

        result = transparentOrigami.calculateVisibleDots(TransparentOrigami.loadInstructions("/input.txt"));
        log.info("Result has {} visible dots after all folds:\n{}", result.getVisibleDots(), result.pageToString());
    }

    public static Instructions loadInstructions(String resource) throws IOException {
        Instructions instructions = new Instructions(new ArrayList<>(), new ArrayList<>());
        CommonUtils.loadResource(resource, TransparentOrigami::map)
                .stream()
                .filter(Objects::nonNull)
                .forEach(object -> {
                    if (object instanceof Point) {
                        instructions.points.add((Point) object);
                    } else if (object instanceof Instructions.Fold) {
                        instructions.folds.add((Instructions.Fold) object);
                    } else {
                        throw new IllegalArgumentException(object.getClass() + " is not expected.");
                    }
                });
        return instructions;
    }

    private static Object map(String line) {
        Object result = null;
        if (StringUtils.isNotBlank(line)) {
            if (line.contains(",")) {
                String[] fields = line.split(",");
                result = new Point(Integer.parseInt(fields[0]), Integer.parseInt(fields[1]));
            } else {
                String[] fields = line.split("\\s")[2].split("=");

                result = new Instructions.Fold();
                ((Instructions.Fold) result).position = Integer.parseInt(fields[1]);
                switch (fields[0]) {
                    case "x":
                        ((Instructions.Fold) result).along = Instructions.Fold.Along.VERTICAL;
                        break;
                    case "y":
                        ((Instructions.Fold) result).along = Instructions.Fold.Along.HORIZONTAL;
                        break;
                    default:
                        throw new IllegalArgumentException(fields[0] + " is not a valid fold option.");
                }
            }
        }
        return result;
    }

    public Result calculateVisibleDots(Instructions instructions) {
        Character [][] page = loadPage(instructions.points);

        for (Instructions.Fold fold : instructions.folds) {
            page = applyFold(page, fold);
        }

        return new Result(page);
    }

    protected Character[][] applyFold(Character[][] page, Instructions.Fold fold) {
        log.info("Folding page [{},{}] by {}", page[0].length, page.length, fold);
        int newYLength = fold.isHorizontalFold() ? page.length / 2 : page.length;
        int newXLength = fold.isVerticalFold() ? page[0].length / 2 : page[0].length;
        Character [][] foldedPage = new Character[newYLength][newXLength];

        for (int yIndex = 0; yIndex < newYLength; yIndex++) {
            int foldedYIndex = yIndex;
            if(fold.isHorizontalFold()){
                foldedYIndex = page.length - yIndex - 1;
            }
            for (int xIndex = 0; xIndex < newXLength; xIndex++) {
                int foldedXIndex = xIndex;
                if(fold.isVerticalFold()){
                    foldedXIndex = page[0].length - xIndex - 1;
                }
                foldedPage[yIndex][xIndex] = merge(page[yIndex][xIndex], page[foldedYIndex][foldedXIndex]);
            }
        }

        return foldedPage;
    }

    protected Character merge(Character c1, Character c2) {
        return Stream.of(c1, c2)
                .filter(Objects::nonNull)
                .findAny()
                .orElse(null);
    }

    protected Character[][] loadPage(List<Point> points) {
        int maxX = points.stream().mapToInt(p -> p.x).max().orElse(-1) + 1;
        int maxY = points.stream().mapToInt(p -> p.y).max().orElse(-1) + 1;

        Character [][] page = new Character[maxY][maxX];

        points.forEach(point -> page[point.y][point.x] = POINT_MARK);

        return page;
    }


    public static class Result {
        public final Character[][] page;

        public Result(Character[][] page) {
            this.page = page;
        }

        protected String pageToString(){
            StringBuilder sb = new StringBuilder();
            for (int yIndex = 0; yIndex < page.length; yIndex++) {
                for (int xIndex = 0; xIndex < page[yIndex].length; xIndex++) {
                    sb.append(Optional.ofNullable(page[yIndex][xIndex]).orElse('.'));
                }
                sb.append("\n");
            }

            return sb.toString();
        }

        protected long getVisibleDots() {
            return Arrays.stream(page)
                    .flatMap(Arrays::stream)
                    .filter(Objects::nonNull)
                    .filter(c -> c.equals(POINT_MARK))
                    .count();
        }

    }

    public static class Instructions {
        public final List<Point> points;
        public final List<Fold> folds;

        public Instructions(List<Point> points, List<Fold> folds) {
            this.points = points;
            this.folds = folds;
        }

        public static class Fold {
            public enum Along {
                VERTICAL,
                HORIZONTAL
            }

            Along along;
            int position;

            boolean isHorizontalFold() {
                return Along.HORIZONTAL.equals(along);
            }

            boolean isVerticalFold() {
                return Along.VERTICAL.equals(along);
            }

            @Override
            public String toString() {
                return "Fold{" +
                        "along=" + along +
                        ", position=" + position +
                        '}';
            }
        }
    }
}