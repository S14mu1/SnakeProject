package com.example;

public class Grid {
    private int size;
    private int level;
    private int apples;
    private double speed;
    private int[][] layout;

    public Grid() {

    }

    public int[][] getLayout() {
        return layout;
    }

    public int getSize() {
        return size;
    }

    public double getSpeed() {
        return speed;
    }

    public int getLevel() {
        return level;
    }

    public void setLayout(int level) {

        switch (level) {
            case 1:
                this.size = 5;
                this.speed = 2;
                int[][] temp = { { 1, 0, 0, 0, 1 },
                        { 0, 0, 0, 0, 0 },
                        { 0, 0, 0, 0, 0 },
                        { 0, 0, 0, 0, 0 },
                        { 1, 0, 0, 0, 1, } };
                layout = temp;
                break;

            case 2:
                this.size = 10;
                this.speed = 1.5;
                int[][] temp1 = { { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
                        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
                        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
                        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
                        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
                        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
                        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
                        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
                        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
                        { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 } };
                layout = temp1;
                break;
        }

    }
}
