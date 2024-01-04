package com.example;

import java.io.IOException;
import java.util.Random;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.skin.TextInputControlSkin.Direction;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.Scene;

public class PrimaryController {
    // --- VARIABLES --- //
    @FXML
    private AnchorPane aPane;
    private Circle food;
    private Random r = new Random();
    Point p = new Point(100, 100);
    private Snake s;
    private int direction;
    Canvas canvas = new Canvas(App.w, App.h);
    private GraphicsContext gc;

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    // -------------------------- Game logic --------------------------//
    public void newSnake() {
        s = new Snake(App.w / 2, App.h / 2, App.size / 2);
        aPane.getChildren().add(s);
        for (int i = 0; i < 5; i++) {
            newFood();
            s.eat(food);
        }

    }

    public void newFood() {
        int x, y;
        do {
            x = r.nextInt(App.w / App.size - 1);
            y = r.nextInt(App.w / App.size - 1);
        } while (isFoodOnSnake(x * App.size, y * App.size));

        food = new Circle(x * App.size, y * App.size, App.size / 2 - 3);
        food.setFill(Color.RED);
        aPane.getChildren().add(food);
    }

    private boolean isFoodOnSnake(double x, double y) {
        for (Circle segment : s.getSnakeBody()) {
            if (segment.intersects(x, y, App.size, App.size)) {
                return true;
            }
        }
        return false;
    }

    private boolean hit() {
        return food.intersects(s.getBoundsInLocal());
    }

    private boolean gameover(){
        if (s.selfCollide()) {
            return true; 
        }
        return false;
    }

    // --- STARTUP --- //
    @FXML
    private void run() {
        aPane.getChildren().add(canvas);
        gc = canvas.getGraphicsContext2D();
        drawBackground(gc);
        newSnake();
        newFood();
    }

    public void adjustLocation() {
        if (s.getCenterX() < 0) {
            s.setCenterX(App.w);
        } else if (s.getCenterX() > App.w) {
            s.setCenterX(0);
        }
        if (s.getCenterY() < 0) {
            s.setCenterY(App.h);
        } else if (s.getCenterY() > App.h) {
            s.setCenterY(0);
        }
    }

    // --- THE GAME LOOP --- //
    @FXML
    public void move() throws IOException {
        s.step();
        adjustLocation();
        if (hit()) {
            s.eat(food);
            newFood();

        }
        if (gameover()) {
            App.setRoot("secondary");
            
        }
    }

    // --- MOVEMENNT--- //
    /*
     * directions: 0 = UP, 1 = DOWN, 2 = LEFT, 3 = RIGHT
     */
    @FXML
    void moveSquareKeyPressed(KeyEvent event) throws IOException {
        if (event.getCode().equals(KeyCode.W) && direction != 1) {
            direction = 0;
            s.setCurrentDirection(0);
            move();
        } else if (event.getCode().equals(KeyCode.S) && direction != 0) {
            direction = 1;
            s.setCurrentDirection(1);
            move();
        } else if (event.getCode().equals(KeyCode.A) && direction != 3) {
            direction = 2;
            s.setCurrentDirection(2);
            move();
        } else if (event.getCode().equals(KeyCode.D) && direction != 2) {
            direction = 3;
            s.setCurrentDirection(3);
            move();
        }
    }

    // --- Background --- //
    private void drawBackground(GraphicsContext gc) {
        for (int i = -1; i < App.row + 1; i++) {
            for (int j = -1; j < App.row + 1; j++) {
                if ((i + j) % 2 == 0) {
                    gc.setFill(Color.web("AA0751"));
                } else {
                    gc.setFill(Color.web("A20751"));
                }
                gc.fillRect(i * App.size + App.size / 2, j * App.size + App.size / 2, App.size, App.size);
            }

        }
    }
}
