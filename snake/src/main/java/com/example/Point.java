package com.example;

// Point class - Defines the coordinates of the snake - is used for both the snake and replay
public class Point {

    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getXPos() {
        return x;
    }

    public double getYPos() {
        return y;
    }

    public void setXPos(double x) {
        this.x = x;
    }

    public void setYPos(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }//tests

}
