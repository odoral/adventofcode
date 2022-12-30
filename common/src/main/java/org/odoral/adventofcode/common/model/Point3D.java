package org.odoral.adventofcode.common.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class Point3D {

    protected static final List<UnaryOperator<Point3D>> NEIGHBOURS = Arrays.asList(
        p -> new Point3D(p.x + 1, p.y, p.z),
        p -> new Point3D(p.x - 1, p.y, p.z),
        p -> new Point3D(p.x, p.y + 1, p.z),
        p -> new Point3D(p.x, p.y - 1, p.z),
        p -> new Point3D(p.x, p.y, p.z + 1),
        p -> new Point3D(p.x, p.y, p.z - 1)
    );

    public final int x;
    public final int y;
    public final int z;

    public Point3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point3D point3D = (Point3D) o;
        return x == point3D.x && y == point3D.y && z == point3D.z;
    }

    public List<Point3D> neighbours() {
        return NEIGHBOURS.stream()
            .map(neighbourOperator -> neighbourOperator.apply(this))
            .collect(Collectors.toList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Point3D{");
        sb.append("x=").append(x);
        sb.append(", y=").append(y);
        sb.append(", z=").append(z);
        sb.append('}');
        return sb.toString();
    }

}
