package org.odoral.adventofcode.common.model;

public class File extends Node {
    public long size;

    public File(String name, Node parent, long size) {
        super(name, parent);
        this.size = size;
    }
}
