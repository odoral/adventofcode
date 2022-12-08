package org.odoral.adventofcode.common.model;

import java.util.HashMap;

public class Directory extends Node {

    public Directory(String name, Node parent) {
        super(name, parent);
        childs = new HashMap<>();
    }

    public Long size() {
        return childs.values()
            .stream()
            .mapToLong(node -> {
                if (node instanceof File) {
                    File file = (File) node;
                    return file.size;
                } else {
                    return ((Directory) node).size();
                }
            }).sum();
    }
}
