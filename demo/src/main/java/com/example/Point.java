package com.example;

public class Point {

    int x;
    int y;

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

    public void translate(int x, int y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public String toString() {
        return "|" + x + ";" + y + '|';
    }

}
