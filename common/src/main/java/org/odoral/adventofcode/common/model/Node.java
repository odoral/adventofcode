package org.odoral.adventofcode.common.model;

import java.util.Map;
import java.util.StringJoiner;

public class Node {

    public String name;
    public Node parent;
    public Map<String, Node> childs;

    public Node(String name, Node parent) {
        this.name = name;
        this.parent = parent;
    }

    @Override public String toString() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
            .add("name='" + name + "'")
            .toString();
    }
}
