package org.odoral.adventofcode.y2022.day22;

import org.apache.commons.lang3.StringUtils;
import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.exception.AdventOfCodeException;
import org.odoral.adventofcode.common.model.Point;
import org.odoral.adventofcode.common.model.ValuedPoint;
import org.odoral.adventofcode.y2022.day22.model.MoveOrder;
import org.odoral.adventofcode.y2022.day22.model.Order;
import org.odoral.adventofcode.y2022.day22.model.TurnOrder;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MonkeyMap {

    public static final char WALL = '#';

    public static int DIRECTION_RIGHT = 0;
    public static int DIRECTION_DOWN = 1;
    public static int DIRECTION_LEFT = 2;
    public static int DIRECTION_UP = 3;

    public static final UnaryOperator<Point>[] DIRECTIONS = new UnaryOperator[]{
        Point.INCREASE_X,
        Point.INCREASE_Y,
        Point.DECREASE_X,
        Point.DECREASE_Y
    };

    public static final IntUnaryOperator TURN_RIGHT = face -> (face + 1 + DIRECTIONS.length) % DIRECTIONS.length;
    public static final IntUnaryOperator TURN_LEFT = face -> (face - 1 + DIRECTIONS.length) % DIRECTIONS.length;
    public static final IntUnaryOperator TURN_OPPOSITE = face -> (face + 2 + DIRECTIONS.length) % DIRECTIONS.length;

    public static BiFunction<State, Input, State> getNextStatePart1() {
        return (currentState, input) -> {
            Point targetPoint = DIRECTIONS[currentState.direction].apply(currentState.position);
            Point targetPointValue = input.map.get(targetPoint);

            if (targetPointValue == null) {
                Point wrap = currentState.position;
                while (input.map.get(DIRECTIONS[TURN_OPPOSITE.applyAsInt(currentState.direction)].apply(wrap)) != null) {
                    wrap = DIRECTIONS[TURN_OPPOSITE.applyAsInt(currentState.direction)].apply(wrap);
                }
                log.debug("{} wrap to {}", currentState.position, wrap);
                targetPoint = wrap;
            }

            return new State(targetPoint, currentState.direction);
        };
    }

    protected static Point getRotationPoint(Point position, int sideLength) {
        return new Point(position.x - position.x % sideLength, position.y - position.y % sideLength);
    }

    public static BiFunction<State, Input, State> getNextStatePart2() {
        return (currentState, input) -> {
            Point rotationPoint = getRotationPoint(currentState.position, input.sideLength);
            Integer cubeFace = input.rotationPointPerCubeFace.get(rotationPoint);
            Point targetPoint = DIRECTIONS[currentState.direction].apply(currentState.position);
            Point targetPointValue = input.map.get(targetPoint);

            State targetState = null;
            if (targetPointValue == null) {
                // targetState = TRANSLATORS[cubeFace][currentState.direction].apply(targetPoint, rotationPoint, input.sideLength);
                log.debug("Cube: {} -> {}+{} translate to {}", cubeFace, currentState.position, currentState.direction, targetState.position);
            } else {
                targetState = new State(targetPoint, currentState.direction);
            }
            return targetState;
        };
    }

    public static void main(String[] args) throws IOException {
        MonkeyMap monkeyMap = new MonkeyMap();
        log.info("Final password is: {}", monkeyMap.calculateFinalPassword(getNextStatePart1()));
    }

    public int calculateFinalPassword(BiFunction<State, Input, State> nextStateFunction) throws IOException {
        Input input = Input.load();
        ValuedPoint<Character> leftMostOpenTile = getLeftMostOpenTile(input);
        log.debug("Leftmost tile: {}", leftMostOpenTile);
        State currentState = new State(leftMostOpenTile, 0);
        for (Order order : input.orders) {
            currentState = move(order, currentState, input, nextStateFunction);
        }
        log.info("Last state: {}", currentState);
        return getFinalPassword(currentState.position.y + 1, currentState.position.x + 1, currentState.direction);
    }

    protected ValuedPoint<Character> getLeftMostOpenTile(Input input) {
        int x = 0;
        int y = 0;

        ValuedPoint<Character> leftMostOpenTile;

        while (Optional.ofNullable(leftMostOpenTile = input.map.get(new Point(x, y)))
            .map(vp -> vp.value)
            .orElse(WALL) == WALL) {
            x++;
        }

        assert '.' == leftMostOpenTile.value;

        return leftMostOpenTile;
    }

    protected State move(Order order, State state, Input input, BiFunction<State, Input, State> nextStateFunction) {
        if (order instanceof TurnOrder) {
            return determineNewFaceState((TurnOrder) order, state);
        } else if (order instanceof MoveOrder) {
            return determineNewPositionState((MoveOrder) order, state, input, nextStateFunction);
        } else {
            throw new AdventOfCodeException("Unsupported order: " + order.getClass().getSimpleName());
        }
    }

    protected State determineNewFaceState(TurnOrder order, State state) {
        IntUnaryOperator turnOperator;
        switch (order.getDirection()) {
            case 'R':
                turnOperator = TURN_RIGHT;
                break;
            case 'L':
                turnOperator = TURN_LEFT;
                break;
            default:
                throw new AdventOfCodeException("Unsupported turn order: " + order.getDirection());
        }

        return new State(state.position, turnOperator.applyAsInt(state.direction));
    }

    protected State determineNewPositionState(MoveOrder order, State initialState, Input input, BiFunction<State, Input, State> nextStateFunction) {
        int moveCount = 0;
        State currentState = new State(initialState.position, initialState.direction);
        State nextState;

        while (moveCount < order.getPositions() &&
            input.map.get((nextState = getNextState(currentState, input, nextStateFunction)).position).value != WALL) {
            currentState = nextState;
            moveCount++;
        }

        return currentState;
    }

    protected State getNextState(State currentState, Input input, BiFunction<State, Input, State> nextStateFunction) {
        return nextStateFunction.apply(currentState, input);
    }

    protected int getFinalPassword(int row, int column, int finalFacing) {
        return 1000 * row + 4 * column + finalFacing;
    }

    public static class Input {
        Map<Point, Integer> rotationPointPerCubeFace;
        Map<Point, ValuedPoint<Character>> map;
        List<Order> orders;
        int sideLength;

        private Input() {
            map = new HashMap<>();
            orders = new ArrayList<>();
        }

        public int getFace(Point point) {
            if (!map.containsKey(point)) {
                throw new AdventOfCodeException("Point out of scope");
            }
            return rotationPointPerCubeFace.get(getRotationPoint(point, sideLength));
        }

        protected static Map<? extends Point, ValuedPoint<Character>> parseMap(String line, int yIndex) {
            Map<Point, ValuedPoint<Character>> map = new HashMap<>();
            for (int xIndex = 0; xIndex < line.length(); xIndex++) {
                if (!StringUtils.isBlank(line.substring(xIndex, xIndex + 1))) {
                    map.put(new Point(xIndex, yIndex), new ValuedPoint<>(xIndex, yIndex, line.charAt(xIndex)));
                }
            }
            return map;
        }

        public static Input load() throws IOException {
            Input input = new Input();
            AtomicBoolean mapLoading = new AtomicBoolean(true);
            AtomicInteger yIndex = new AtomicInteger();
            CommonUtils.loadResource("/input.txt", Function.identity())
                .forEach(line -> {
                    if (line.isEmpty()) {
                        mapLoading.set(false);
                    } else {
                        if (mapLoading.get()) {
                            input.map.putAll(parseMap(line, yIndex.getAndIncrement()));
                        } else {
                            input.orders.addAll(parseOrders(line));
                        }
                    }
                });

            input.sideLength = input.map.keySet()
                .stream()
                .flatMap(p -> Stream.of(p.x, p.y * 10_000))
                .collect(Collectors.toMap(Function.identity(), i -> 1, Math::addExact))
                .values()
                .stream()
                .mapToInt(i -> i)
                .min()
                .orElse(-1);

            AtomicInteger cubeFace = new AtomicInteger(1);
            input.rotationPointPerCubeFace = input.map.keySet()
                .stream()
                .sorted(Comparator.comparing(Point::getY).thenComparing(Point::getX))
                .map((Point position) -> getRotationPoint(position, input.sideLength))
                .distinct()
                .collect(Collectors.toMap(Function.identity(), p -> cubeFace.getAndIncrement()));

            input.rotationPointPerCubeFace.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(Map.Entry::getValue))
                .forEach(e -> log.info("Cube {} -> {}", e.getValue(), e.getKey()));

            return input;
        }

        protected static Collection<? extends Order> parseOrders(String line) {
            List<Order> orders = new ArrayList<>();
            StringBuilder sb = new StringBuilder();

            for (int index = 0; index < line.length(); index++) {
                String letter = line.substring(index, index + 1);
                if (StringUtils.isNumeric(letter)) {
                    sb.append(letter);
                } else {
                    if (!sb.toString().isEmpty()) {
                        orders.add(new MoveOrder(Integer.parseInt(sb.toString())));
                        sb = new StringBuilder();
                    }
                    orders.add(new TurnOrder(line.charAt(index)));
                }
            }

            if (!sb.toString().isEmpty()) {
                orders.add(new MoveOrder(Integer.parseInt(sb.toString())));
            }

            return orders;
        }
    }

    public static class State {
        Point position;
        int direction;

        public State(Point position, int direction) {
            this.position = position;
            this.direction = direction;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("State{");
            sb.append("position=").append(position);
            sb.append(", facing=").append(direction);
            sb.append('}');
            return sb.toString();
        }
    }

}