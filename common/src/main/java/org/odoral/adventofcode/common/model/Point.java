package org.odoral.adventofcode.common.model;

import java.util.Objects;

public class Point {
    public final int x;
    public final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Point{");
        sb.append("x=").append(x);
        sb.append(", y=").append(y);
        sb.append('}');
        return sb.toString();
    }

    public Point moveUp() {
        return new Point(x, y + 1);
    }

    public Point moveDown() {
        return new Point(x, y - 1);
    }

    public Point moveRight() {
        return new Point(x + 1, y);
    }

    public Point moveLeft() {
        return new Point(x - 1, y);
    }

    public double distance(Point other) {
        return Math.sqrt(Math.pow(Math.abs(other.x - x), 2) + Math.pow(Math.abs(other.y - y), 2));
    }
}
