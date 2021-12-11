package org.odoral.adventofcode.common.model;

public class ValuedPoint<E> extends Point {
    
    public final E value;

    public ValuedPoint(int x, int y, E value) {
        super(x, y);
        this.value = value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ValuedPoint{");
        sb.append("x=").append(x);
        sb.append(", y=").append(y);
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
