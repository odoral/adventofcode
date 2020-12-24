package org.odoral.adventofcode.y2020.day20.model;

import java.util.List;

public class TiledImage implements SquareTile{

    final SquareTile[][] tiles;
    final int tileCountWidth;
    final int tileWidth;
    final long number;

    public TiledImage(List<SquareTile> tiles) {
        this(tiles, -1);
    }
    
    public TiledImage(List<SquareTile> tiles, long number) {
        this.tileCountWidth = (int) Math.sqrt(tiles.size());
        this.tiles = loadTiles(tiles);
        this.tileWidth = tiles.get(0).width();
        this.number = number;
    }

    protected SquareTile[][] loadTiles(List<SquareTile> tiles) {
        SquareTile[][] result = new SquareTile[this.tileCountWidth][];
        for (int i = 0; i < tiles.size(); i++) {
            int y=i/tileCountWidth;
            int x=i%tileCountWidth;
            if(x == 0){
                result[y] = new SquareTile[this.tileCountWidth];
            }
            
            result[y][x] = tiles.get(i);
        }
        return result;
    }

    @Override
    public long number() {
        return number;
    }

    @Override
    public int width() {
        return tileWidth * tileCountWidth;
    }

    @Override
    public char at(int x, int y) {
        return tiles[y / tileWidth][x / tileWidth].at(x % tileWidth, y % tileWidth);
    }
}
