package com.example;

/**
 * Food
 */
public class Food {

    private double x;
    private double y;

    public Food(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public boolean isEaten(double x, double y) {
        if (this.x == x && this.y == y) {
            return true;
        } else {
            return false;
        }
    }
}