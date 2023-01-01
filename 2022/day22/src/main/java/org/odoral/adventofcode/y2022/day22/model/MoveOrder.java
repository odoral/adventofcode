package org.odoral.adventofcode.y2022.day22.model;

public class MoveOrder implements Order {
    int positions;

    public MoveOrder(int positions) {
        this.positions = positions;
    }

    public int getPositions() {
        return positions;
    }

    public void setPositions(int positions) {
        this.positions = positions;
    }
}
