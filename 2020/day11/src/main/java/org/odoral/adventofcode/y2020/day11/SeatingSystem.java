package org.odoral.adventofcode.y2020.day11;

import org.odoral.adventofcode.common.CommonUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SeatingSystem {
    
    public static final char SEAT_EMPTY = 'L';
    public static final char SEAT_OCCUPIED = '#';
    public static final char FLOOR = '.';

    public static void main(String[] args) throws IOException {
        SeatingSystem seatingSystem = new SeatingSystem();
        Character[][] seatsLayout = seatingSystem.loadSeatLayout("/input.txt");
        seatingSystem.checkFinalOccupiedSeats(seatsLayout, 1, 4);
        seatingSystem.checkFinalOccupiedSeats(seatsLayout, Integer.MAX_VALUE, 5);
    }

    protected void checkFinalOccupiedSeats(Character[][] seatsLayout, int adjacentSeats, int occupiedSeatsThreshold) {
        IterationResult iterationResult;
        int loops = 0;
        while((iterationResult = iterateRound(seatsLayout, adjacentSeats, occupiedSeatsThreshold)).hasChanged){
            loops++;
            seatsLayout = iterationResult.seatLayout;
        }
        log.info("No changes after {} loops for {} adjacent seats.", loops, adjacentSeats);
        log.info("Occupied seats: {}.", countOccupiedSeats(seatsLayout));
        log.info("Final state:\n{}", printSeatLayout(seatsLayout));
    }

    protected String printSeatLayout(Character[][] seatsLayout) {
        return Stream.of(seatsLayout)
            .map(row -> Stream.of(row)
                .map(String::valueOf)
                .collect(Collectors.joining()))
            .collect(Collectors.joining("\n"));
    }

    public Character[][] loadSeatLayout(String resource) throws IOException {
        return CommonUtils.loadResource(resource, CommonUtils::toCharacterArray).toArray(new Character[0][]);
    }

    public IterationResult iterateRound(Character[][] seatLayout, int adjacentSeats, int occupiedSeatsThreshold) {
        Character[][] resultAfterIteration = new Character[seatLayout.length][];

        boolean hasChanged = false;
        for (int row = 0; row < seatLayout.length; row++) {
            resultAfterIteration[row] = new Character[seatLayout[row].length];
            for (int column = 0; column < seatLayout[row].length; column++) {
                Character seat = seatLayout[row][column];
                Character seatResult;
                switch (seat){
                    case SEAT_EMPTY:
                        seatResult = resultForEmptySeat(seatLayout, row, column, adjacentSeats);
                        break;
                    case SEAT_OCCUPIED:
                        seatResult = resultForOccupiedSeat(seatLayout, row, column, adjacentSeats, occupiedSeatsThreshold);
                        break;
                    case FLOOR:
                        seatResult = FLOOR;
                        break;
                    default:
                        throw new UnsupportedOperationException("Unsupported seat state "+seat+" in row: "+row+", column: "+column);
                }
                resultAfterIteration[row][column] = seatResult;
                if(!seat.equals(seatResult)){
                    hasChanged = true;
                }
            }
        }
        
        return new IterationResult(resultAfterIteration, hasChanged);
    }

    protected Character resultForEmptySeat(Character[][] seatLayout, int row, int column, int adjacentSeats) {
        int adjacentOccupied = getAdjacentOccupied(seatLayout, row, column, adjacentSeats);

        if(adjacentOccupied == 0){
            return SEAT_OCCUPIED;
        }else{
            return SEAT_EMPTY;
        }
    }

    protected Character resultForOccupiedSeat(Character[][] seatLayout, int row, int column, int adjacentSeats, int occupiedSeatsThreshold) {
        int adjacentOccupied = getAdjacentOccupied(seatLayout, row, column, adjacentSeats);

        if(adjacentOccupied >= occupiedSeatsThreshold){
            return SEAT_EMPTY;
        }else{
            return SEAT_OCCUPIED;
        }
    }

    protected int getAdjacentOccupied(Character[][] seatLayout, int row, int column, int adjacentSeats) {
        AtomicInteger adjacentOccupied = new AtomicInteger(0);
        Consumer<Character> seatConsumer = seat -> {
            if (seat == SEAT_OCCUPIED) {
                adjacentOccupied.incrementAndGet();
            }
        };
        getSeatInSight(seatLayout, row, column, adjacentSeats, -1, 0).ifPresent(seatConsumer);// North
        getSeatInSight(seatLayout, row, column, adjacentSeats, -1, 1).ifPresent(seatConsumer);// North-East
        getSeatInSight(seatLayout, row, column, adjacentSeats, 0, 1).ifPresent(seatConsumer);// East
        getSeatInSight(seatLayout, row, column, adjacentSeats, 1, 1).ifPresent(seatConsumer);// South-East
        getSeatInSight(seatLayout, row, column, adjacentSeats, 1, 0).ifPresent(seatConsumer);// South
        getSeatInSight(seatLayout, row, column, adjacentSeats, 1, -1).ifPresent(seatConsumer);// South-West
        getSeatInSight(seatLayout, row, column, adjacentSeats, 0, -1).ifPresent(seatConsumer);// West
        getSeatInSight(seatLayout, row, column, adjacentSeats, -1, -1).ifPresent(seatConsumer);// North-West
        
        return adjacentOccupied.get();
    }

    protected Optional<Character> getSeatInSight(Character[][] seatLayout, int row, int column, int adjacentSeats, int rowIncrement, int columnIncrement) {
        Optional<Character> result = Optional.empty();
        
        int count = 1;
        while(count <= adjacentSeats && 
            rowInsideBounds(seatLayout, row, rowIncrement, count) && 
            columnInsideBounds(seatLayout, row, column, columnIncrement, count)){
            Character seat = seatLayout[row + rowIncrement * count][column + columnIncrement * count];
            if(seat != FLOOR){
                result = Optional.of(seat);
                break;
            }
            count++;
        }
        
        return result;
    }

    protected boolean rowInsideBounds(Character[][] seatLayout, int row, int rowIncrement, int count) {
        int rowIndex = row + (rowIncrement * count);
        return rowIndex >= 0 && rowIndex < seatLayout.length;
    }

    protected boolean columnInsideBounds(Character[][] seatLayout, int row, int column, int columnIncrement, int count) {
        int columnIndex = column + (columnIncrement * count);
        return columnIndex >= 0 && columnIndex < seatLayout[row].length;
    }

    public long countOccupiedSeats(Character[][] seatLayout) {
        return Arrays.stream(seatLayout)
            .flatMap(Stream::of)
            .filter(seat -> seat == SEAT_OCCUPIED)
            .count();
    }
    
    public class IterationResult{
        
        Character[][] seatLayout;
        boolean hasChanged;

        public IterationResult(Character[][] seatLayout, boolean hasChanged) {
            this.seatLayout = seatLayout;
            this.hasChanged = hasChanged;
        }
    }
}
