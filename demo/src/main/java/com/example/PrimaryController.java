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

    // --- Game logic ---//
    public void newSnake() {
        s = new Snake(App.w / 2, App.h / 2, App.size / 2);
        aPane.getChildren().add(s);
        for (int i = 0; i < 5; i++) {
            newFood();
            s.eat(food);
        }

    }

    public void newFood() {
        int x = r.nextInt(App.w / App.size - 1);
        int y = r.nextInt(App.w / App.size - 1);
        food = new Circle(x * App.size, y * App.size, App.size / 2);
        food.setFill(Color.RED);
        aPane.getChildren().add(food);
    }

    private boolean hit() {
        return food.intersects(s.getBoundsInLocal());
    }

    @FXML
    private void run() {
        aPane.getChildren().add(canvas);
        gc = canvas.getGraphicsContext2D();
        drawBackground(gc);
        newSnake();
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

    @FXML
    public void move() {
        s.step();
        adjustLocation();
        if (hit()) {
            s.eat(food);
            newFood();
        }
    }

    @FXML
    void moveSquareKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.W)) {
            s.setCurrentDirection(0);
            move();
        } else if (event.getCode().equals(KeyCode.S)) {
            ;
            s.setCurrentDirection(1);
            move();
        } else if (event.getCode().equals(KeyCode.A)) {
            s.setCurrentDirection(2);
            move();
        } else if (event.getCode().equals(KeyCode.D)) {
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
