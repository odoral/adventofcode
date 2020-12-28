package org.odoral.adventofcode.y2020.day24.model;

import lombok.Getter;

@Getter
public enum Directions {
    E("e","east", -2, 0),
    SE("se","southeast", -1, -1),
    SW("sw","southwest", 1, -1),
    W("w","west", 2, 0),
    NW("nw","northwest", 1, 1),
    NE("ne","northeast", -1, 1);

    final String shortname;
    final String description;
    final int x;
    final int y;

    Directions(String shortname, String description, int x, int y) {
        this.shortname = shortname;
        this.description = description;
        this.x = x;
        this.y = y;
    }
}
