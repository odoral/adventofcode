package org.odoral.adventofcode.common.model;

import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public class Point {

    public static final UnaryOperator<Point> INCREASE_Y = Point::increaseY;
    public static final UnaryOperator<Point> DECREASE_Y = Point::decreaseY;
    public static final UnaryOperator<Point> INCREASE_X = Point::increaseX;
    public static final UnaryOperator<Point> DECREASE_X = Point::decreaseX;

    public static final BinaryOperator<Point> ROTATE_HALF_PI = (point, axis) -> new Point(
        axis.x - (point.y - axis.y),
        axis.y + (point.x - axis.x)
    );

    public static final BinaryOperator<Point> ROTATE_PI = (point, axis) -> ROTATE_HALF_PI.apply(ROTATE_HALF_PI.apply(point, axis), axis);
    public static final BinaryOperator<Point> ROTATE_THREE_HALF_PI = (point, axis) -> ROTATE_PI.apply(ROTATE_HALF_PI.apply(point, axis), axis);
    

    public static final BinaryOperator<Point> TRANSLATE = (point, distance) -> new Point(point.x + distance.x, point.y + distance.y);

    public final int x;
    public final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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

    public Point increaseY() {
        return new Point(x, y + 1);
    }

    public Point decreaseY() {
        return new Point(x, y - 1);
    }

    public Point increaseX() {
        return new Point(x + 1, y);
    }

    public Point decreaseX() {
        return new Point(x - 1, y);
    }

    public double distance(Point other) {
        return Math.sqrt(Math.pow(Math.abs(other.x - x), 2) + Math.pow(Math.abs(other.y - y), 2));
    }
}
