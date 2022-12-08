package org.odoral.adventofcode.y2022.day8;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import org.apache.commons.lang3.ArrayUtils;
import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.exception.AdventOfCodeException;
import org.odoral.adventofcode.common.model.ValuedPoint;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TreetopTreeHouse {

    protected LoadingCache<ValuedPoint<Integer>, ValuedPoint<Integer>[]> columnCache;

    protected final BiFunction<ValuedPoint<Integer>[][], ValuedPoint<Integer>, Boolean> IS_VISIBLE = (forest, tree) -> getTreeEyeSights(forest, tree)
        .anyMatch(eyeSight -> eyeSight.length == 0 || Stream.of(eyeSight).allMatch(eyeSightTree -> eyeSightTree.value < tree.value));

    protected final BiFunction<ValuedPoint<Integer>[][], ValuedPoint<Integer>, Long> SCENIC_SCORE = (forest, tree) -> getTreeEyeSights(forest, tree)
        .mapToInt(eyeSight -> {
            int count = 0;

            for (int i = 0; i < eyeSight.length; i++) {
                if (i == 0 && eyeSight[i].value < tree.value) {
                    count++;
                } else if (eyeSight[i].value < tree.value) {
                    count++;
                } else {
                    count++;
                    break;
                }
            }
            return count;
        })
        .mapToObj(BigInteger::valueOf)
        .reduce(BigInteger.ONE, BigInteger::multiply)
        .longValue();

    public static void main(String[] args) throws IOException {
        TreetopTreeHouse treetopTreeHouse = new TreetopTreeHouse();
        ValuedPoint<Integer>[][] treeHeight = treetopTreeHouse.loadInputMap("/input.txt");
        treetopTreeHouse.initColumnCache(treeHeight);
        long visibleTrees = treetopTreeHouse.process(treeHeight, treetopTreeHouse.IS_VISIBLE)
            .stream()
            .filter(b -> b)
            .count();
        log.info("There are {} visible trees", visibleTrees);

        Long maxScenicScore = treetopTreeHouse.process(treeHeight, treetopTreeHouse.SCENIC_SCORE)
            .stream()
            .max(Comparator.naturalOrder())
            .orElseThrow(() -> new AdventOfCodeException("Not found"));
        log.info("Max scenic score: {}", maxScenicScore);
    }

    protected ValuedPoint<Integer>[][] loadInputMap(String resource) throws IOException {
        return CommonUtils.loadMatrixResource(resource, (xPos, content) -> {
            AtomicInteger yPos = new AtomicInteger();

            return Arrays.stream(CommonUtils.toIntegerArray(content))
                .map(value -> new ValuedPoint(xPos, yPos.getAndIncrement(), value))
                .toArray(ValuedPoint[]::new);
        }, ValuedPoint.class);
    }

    protected void initColumnCache(ValuedPoint<Integer>[][] treeHeight) {
        columnCache = CacheBuilder.newBuilder().build(new CacheLoader<ValuedPoint<Integer>, ValuedPoint<Integer>[]>() {
            @Override public ValuedPoint<Integer>[] load(ValuedPoint<Integer> key) {
                return CommonUtils.extractColumn(treeHeight, ValuedPoint.class, key.y);
            }
        });
    }

    protected <Result> List<Result> process(ValuedPoint<Integer>[][] treeHeight, BiFunction<ValuedPoint<Integer>[][], ValuedPoint<Integer>, Result> treeFunction) {
        return IntStream.range(0, treeHeight.length)
            .mapToObj(rowIndex -> {
                ValuedPoint<Integer>[] treesInRow = treeHeight[rowIndex];
                return IntStream.range(0, treesInRow.length)
                    .mapToObj(columnIndex -> {
                        ValuedPoint<Integer> tree = treeHeight[rowIndex][columnIndex];
                        return treeFunction.apply(treeHeight, tree);
                    });
            }).flatMap(Function.identity())
            .collect(Collectors.toList());
    }

    protected Stream<ValuedPoint<Integer>[]> getTreeEyeSights(ValuedPoint<Integer>[][] forest, ValuedPoint<Integer> tree) {
        ValuedPoint<Integer>[] rowTrees = Arrays.copyOfRange(forest[tree.x], 0, tree.y);
        ValuedPoint<Integer>[] rowTreesReversed = Arrays.copyOfRange(forest[tree.x], tree.y + 1, forest[tree.y].length);
        ArrayUtils.reverse(rowTrees);

        ValuedPoint<Integer>[] columnTrees = Arrays.copyOfRange(columnCache.getUnchecked(tree), 0, tree.x);
        ValuedPoint<Integer>[] columnTreesReversed = Arrays.copyOfRange(columnCache.getUnchecked(tree), tree.x + 1, columnCache.getUnchecked(tree).length);
        ArrayUtils.reverse(columnTrees);

        return Stream.of(rowTrees, rowTreesReversed, columnTrees, columnTreesReversed);
    }

}