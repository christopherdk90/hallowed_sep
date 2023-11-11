package com.HallowedSepulchre;

public final class Coord {

    public Coord(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int x;
    public int y;

    public boolean Equals(int x, int y){

        return this.x == x && this.y == y;

    }
}
