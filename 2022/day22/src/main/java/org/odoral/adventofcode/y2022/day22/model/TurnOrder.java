package org.odoral.adventofcode.y2022.day22.model;

public class TurnOrder implements Order {
    char direction;

    public TurnOrder(char direction) {
        this.direction = direction;
    }

    public char getDirection() {
        return direction;
    }

    public void setDirection(char direction) {
        this.direction = direction;
    }
}
