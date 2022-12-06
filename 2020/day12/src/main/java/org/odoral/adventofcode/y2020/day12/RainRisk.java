package org.odoral.adventofcode.y2020.day12;

import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RainRisk {

    public static final String UNSUPPORTED_CHAR_FOR_NAVIGATION_INSTRUCTION = "Unsupported char %s for navigation instruction.";
    public static final String UNSUPPORTED_CHAR_FOR_FACING = "Unsupported char %s for Facing.";

    public enum Facing {
        N(0, 1),
        E(1, 0),
        S(0, -1),
        W(-1, 0);

        public final int xMov;
        public final int yMov;

        Facing(int xMov, int yMov) {
            this.xMov = xMov;
            this.yMov = yMov;
        }

        public Facing turn(char turn, int rotationDegrees) {
            Facing[] directions = Facing.values();
            int position = Arrays.binarySearch(directions, this);
            int rotationDirection = turn == 'R' ? 1 : -1;
            int rotation = (rotationDirection * rotationDegrees / 90) % 4;
            int finalPosition = (position + rotation) % 4;
            if (finalPosition < 0) {
                return directions[directions.length + finalPosition];
            } else {
                return directions[finalPosition];
            }
        }

        public static Facing fromChar(char facingCode) {
            switch (facingCode) {
                case 'N':
                    return N;
                case 'S':
                    return S;
                case 'E':
                    return E;
                case 'W':
                    return W;
                default:
                    throw new UnsupportedOperationException(String.format(UNSUPPORTED_CHAR_FOR_FACING, facingCode));
            }
        }

    }

    public static void main(String[] args) throws IOException {
        RainRisk rainRisk = new RainRisk();
        List<NavigationInstruction> navigationInstructions = rainRisk.loadNavigationInstructions("/input.txt");
        int[] destination = rainRisk.moveForPartOne(new int[]{0, 0}, Facing.E, navigationInstructions);
        log.info("Final position for first part: {}, {} => {}", destination[0], destination[1], rainRisk.calculateManhattanDistance(destination));
        destination = rainRisk.moveForPartTwo(new int[]{0, 0}, new int[]{10, 1}, navigationInstructions);
        log.info("Final position for second part: {}, {} => {}", destination[0], destination[1], rainRisk.calculateManhattanDistance(destination));
    }

    public List<NavigationInstruction> loadNavigationInstructions(String resource) throws IOException {
        return CommonUtils.loadResource(resource, NavigationInstruction::from);
    }

    public int[] moveForPartOne(int[] origin, Facing facing, List<NavigationInstruction> navigationInstructions) {
        int[] currentPosition = new int[]{origin[0], origin[1]};
        Facing currentFacing = facing;
        for (NavigationInstruction navigationInstruction : navigationInstructions) {
            switch (navigationInstruction.instruction) {
                case 'N':
                case 'S':
                case 'E':
                case 'W':
                    Facing moveDir = Facing.fromChar(navigationInstruction.instruction);
                    currentPosition[0] = currentPosition[0] + moveDir.xMov * navigationInstruction.value;
                    currentPosition[1] = currentPosition[1] + moveDir.yMov * navigationInstruction.value;
                    break;
                case 'L':
                case 'R':
                    currentFacing = currentFacing.turn(navigationInstruction.instruction, navigationInstruction.value);
                    break;
                case 'F':
                    currentPosition[0] = currentPosition[0] + currentFacing.xMov * navigationInstruction.value;
                    currentPosition[1] = currentPosition[1] + currentFacing.yMov * navigationInstruction.value;
                    break;
                default:
                    throw new UnsupportedOperationException(String.format(UNSUPPORTED_CHAR_FOR_NAVIGATION_INSTRUCTION, navigationInstruction.instruction));
            }
        }
        return currentPosition;
    }

    public int[] moveForPartTwo(int[] origin, int[] initialWaypoint, List<NavigationInstruction> navigationInstructions) {
        int[] currentPosition = new int[]{origin[0], origin[1]};
        int[] currentWaypoint = new int[]{initialWaypoint[0], initialWaypoint[1]};
        for (NavigationInstruction navigationInstruction : navigationInstructions) {
            switch (navigationInstruction.instruction) {
                case 'N':
                case 'S':
                case 'E':
                case 'W':
                    Facing moveDir = Facing.fromChar(navigationInstruction.instruction);
                    currentWaypoint[0] = currentWaypoint[0] + moveDir.xMov * navigationInstruction.value;
                    currentWaypoint[1] = currentWaypoint[1] + moveDir.yMov * navigationInstruction.value;
                    break;
                case 'L':
                    currentWaypoint = rotateWaypoint(currentWaypoint, navigationInstruction.value);
                    break;
                case 'R':
                    currentWaypoint = rotateWaypoint(currentWaypoint, -navigationInstruction.value);
                    break;
                case 'F':
                    currentPosition[0] = currentPosition[0] + currentWaypoint[0] * navigationInstruction.value;
                    currentPosition[1] = currentPosition[1] + currentWaypoint[1] * navigationInstruction.value;
                    break;
                default:
                    throw new UnsupportedOperationException(String.format(UNSUPPORTED_CHAR_FOR_NAVIGATION_INSTRUCTION, navigationInstruction.instruction));
            }
        }
        return currentPosition;
    }

    protected int[] rotateWaypoint(int[] currentWaypoint, int rotation) {
        int[] result = new int[]{currentWaypoint[0], currentWaypoint[1]};
        if (rotation % 360 != 0) {
            int rotations = Math.abs(rotation / 90);
            int[] r;
            if (rotation > 0) {
                r = new int[]{-1, 1};
            } else {
                r = new int[]{1, -1};
            }
            for (int i = 0; i < rotations; i++) {
                int finalWayPointX = result[1] * r[0];
                int finalWayPointY = result[0] * r[1];
                result = new int[]{finalWayPointX, finalWayPointY};
            }
        }
        return result;
    }

    protected int calculateManhattanDistance(int[] position) {
        return Arrays.stream(position)
            .map(Math::abs)
            .sum();
    }

    public static class NavigationInstruction {

        final char instruction;
        final int value;

        public NavigationInstruction(char instruction, int value) {
            this.instruction = instruction;
            this.value = value;
        }

        public static NavigationInstruction from(String code) {
            return new NavigationInstruction(code.charAt(0),
                Integer.parseInt(code.substring(1)));
        }
    }
}
