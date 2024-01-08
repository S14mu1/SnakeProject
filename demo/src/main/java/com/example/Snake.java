package com.example;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.*;

public class Snake extends Circle {

    // -------------------------- VARIABLES -------------------------- //
    private static int scaler = 5; //Adjusts how smooth the movement will be, high for smooth, low for jagged --- MAX 10 MIN 1 - Pick between 5 and 10 for best experience
    private static int updateScaler = scaler; // Same as scaler but updates
    private ArrayList<Circle> snakeBody;
    private int length = 0;
    private static final int STEP = App.size/updateScaler; //Spacing between each segment
    private int currentDirection;
    private Point pos;
    private int bufferedDirection = 0;
    private int colorChange = 0;
    private int c = 0;
    

    // -------------------------- CONSTRUCTOR USES CIRCLE SUPERCLASS -------------------------- //
    public Snake(double d, double d1, double d2) {
        super(d, d1, d2);
        snakeBody = new ArrayList<>();
        currentDirection = 0;
        pos = new Point(App.w / 2, App.h / 2);
    }

    // -------------------------- MOVEMENT OF THE SNAKE -------------------------- //
    public void step() {
        int x = pos.getX();
        int y = pos.getY();

        for (int i = length - 1; i >= 0; i--) {
            if (i == 0) {
                snakeBody.get(i).setCenterX(getCenterX());
                snakeBody.get(i).setCenterY(getCenterY());
            } else {
                snakeBody.get(i).setCenterX(snakeBody.get(i - 1).getCenterX());
                snakeBody.get(i).setCenterY(snakeBody.get(i - 1).getCenterY());
            }

        }

        if (currentDirection == 0) { // Steps with the current direction
            setCenterY(getCenterY() - STEP);
            y = y - STEP;
        } else if (currentDirection == 1) {
            setCenterY(getCenterY() + STEP);
            y = y + STEP;
        } else if (currentDirection == 2) {
            setCenterX(getCenterX() - STEP);
            x = x - STEP;
        } else if (currentDirection == 3) {
            setCenterX(getCenterX() + STEP);
            x = x + STEP;
        }
        pos.translate(x, y);
        
        updateScaler--;

        if(updateScaler == 0){
            updateScaler = scaler;
            currentDirection = bufferedDirection;
        }
    }

    // -------------------------- COLLISION WITH ITSELF -------------------------- //
    public boolean selfCollide() {
        for (int i = 0; i < length; i++) {
            if (this.getCenterX() == snakeBody.get(i).getCenterX()
                    && this.getCenterY() == snakeBody.get(i).getCenterY()) {
                return true;
            }
        }
        return false;
    }

    private Circle endTail() {
        if (length == 0) {
            return this;
        }
        return snakeBody.get(length - 1);
    }

    // -------------------------- EATING FUNCTION -------------------------- ///
    public void eat(Circle f) {
        
        Circle tail = endTail();
        f.setCenterX(tail.getCenterX());
        f.setCenterY(tail.getCenterY());
        f.setFill(Color.rgb(0, 0 + c * 3, 0 + c * 3));
        snakeBody.add(length++, f);    
        colorChange++;

        // Handles the gradient of the snake, ensures that the value 255 is not exceeded
        if(colorChange == scaler){
            c++;
            colorChange = 0;
        }

    }

    // -------------------------- GETTERS AND SETTERS -------------------------- //

    public int getLength() {
        return length;
    }

    public int getCurrentDirection() {
        return currentDirection;
    }

    public Point getPos() {
        return this.pos;
    }

    public void setCurrentDirection(int d) {
        bufferedDirection = d;
    }

    public ArrayList<Circle> getSnakeBody() {
        return snakeBody;
    }

    public int getScaler(){
        return updateScaler;
    }

    

}
