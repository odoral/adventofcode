package org.odoral.adventofcode.y2021.day12;

import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PassagePathing {

    public static final String START = "start";
    public static final String END = "end";
    public static final Predicate<List<String>> PATH_IS_FINISHED = path -> !path.isEmpty() && END.equals(path.get(path.size() - 1));
    public static final String PATH_SEPARATOR = "-";

    public static final BiPredicate<List<String>, String> SKIP_IF_SMALL_AND_ALREADY_IN_PATH = (path, cave) ->
            !(isSmallCave(cave) && path.contains(cave));

    public static final BiPredicate<List<String>, String> SKIP_IF_SMALL_AND_VISITED_TWICE_IN_PATH = (path, cave) -> {
        if (isSmallCave(cave)) {
            long visits = countVisits(path, cave);
            long maxSingleCaveVisits = countSingleCaveVisits(path);
            if (isStartCave(cave)) {
                return visits < 1;
            } else {
                if (maxSingleCaveVisits >= 2) {
                    return visits < 1;
                } else {
                    return visits < 2;
                }
            }
        } else {
            return true;
        }
    };

    public static void main(String[] args) throws IOException {
        PassagePathing passagePathing = new PassagePathing();

        Map<String, Set<String>> caveMap = PassagePathing.loadCaveMap("/input.txt");
        PassagePathing.Result result = passagePathing.calculatePaths(caveMap, SKIP_IF_SMALL_AND_ALREADY_IN_PATH);
        log.info("Found {} different paths.", result.paths.size());

        result = passagePathing.calculatePaths(caveMap, SKIP_IF_SMALL_AND_VISITED_TWICE_IN_PATH);
        log.info("Found {} different paths.", result.paths.size());
    }

    public Result calculatePaths(Map<String, Set<String>> caveMap, BiPredicate<List<String>, String> skipFunction) {
        List<List<String>> paths = calculatePaths(caveMap, new ArrayList<>(), START, skipFunction);

        return new Result(paths.stream()
                .filter(PATH_IS_FINISHED)
                .map(p -> String.join(",", p))
                .distinct()
                .collect(Collectors.toList()));
    }

    public List<List<String>> calculatePaths(Map<String, Set<String>> caveMap, List<String> accumulatedPath, String previousCave, BiPredicate<List<String>, String> skipFunction) {
        if (PATH_IS_FINISHED.test(accumulatedPath)) {
            return Collections.singletonList(accumulatedPath);
        }

        Set<String> nextCaves = caveMap.get(previousCave);

        return nextCaves.stream()
                .filter(nextCave -> skipFunction.test(accumulatedPath, previousCave))
                .flatMap(nextCave -> {
                    ArrayList<String> previousPath = new ArrayList<>(accumulatedPath);
                    previousPath.add(previousCave);
                    return calculatePaths(caveMap, previousPath, nextCave, skipFunction).stream();
                }).collect(Collectors.toList());
    }

    public static Map<String, Set<String>> loadCaveMap(String resource) throws IOException {
        List<String> unions = CommonUtils.loadResource(resource, Function.identity());
        return unions.stream()
                .flatMap(u -> {
                    String[] nodes = u.split(PATH_SEPARATOR);

                    return Stream.of(
                            String.join(PATH_SEPARATOR, nodes[0], nodes[1]),
                            String.join(PATH_SEPARATOR, nodes[1], nodes[0]));
                }).collect(Collectors.groupingBy(
                        u -> u.split(PATH_SEPARATOR)[0],
                        Collectors.mapping(u -> u.split(PATH_SEPARATOR)[1], Collectors.toSet())));
    }

    protected static long countSingleCaveVisits(List<String> path) {
        return path.stream()
                .filter(PassagePathing::isSmallCave)
                .collect(Collectors.groupingBy(Function.identity()))
                .values()
                .stream()
                .mapToInt(List::size)
                .max()
                .orElse(0);
    }

    protected static long countVisits(List<String> path, String cave) {
        return path.stream()
                .filter(cave::equals)
                .count();
    }

    public static boolean isStartCave(String cave) {
        return START.equals(cave);
    }

    public static boolean isSmallCave(String cave) {
        return cave.equals(cave.toLowerCase());
    }

    public static class Result {
        public final List<String> paths;

        public Result(List<String> paths) {
            this.paths = paths;
            log.info("Found {} paths: {}", paths.size(), paths.stream().sorted().collect(Collectors.joining("\n")));
        }
    }
}