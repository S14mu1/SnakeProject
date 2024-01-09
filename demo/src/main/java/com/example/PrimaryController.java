package com.example;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class PrimaryController {
    // -------------------------- VARIABLES -------------------------- //
    @FXML
    private AnchorPane aPane;
    private Circle food;
    private Random r = new Random(); // Used in foods position
    private Snake s;
    private int direction;
    Canvas canvas = new Canvas(App.w, App.h);
    private GraphicsContext gc;
    private int score;
    private final int TIME = 100; // Milliseconds between each gametick
    private Timeline gameLoop;

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    // -------------------------- Game logic --------------------------//
    /*
     * Here all the game logic will be this is how the game is controlled
     */
    // -------------------------- STARTUP -------------------------- //
    @FXML
    private void run() { // Runs when button is pressed, runs starts up the game
        aPane.getChildren().add(canvas);
        gc = canvas.getGraphicsContext2D();
        drawBackground(gc);
        newSnake();
        newFood();
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        gc.fillText("SCORE: " + score, 247.5, 60);
        startGameLoop();
    }

    private void newSnake() {
        s = new Snake(App.w / 2, App.h / 2, App.size / 2 * 0.95);
        s.setFill(Color.BLACK); // Change the color of the snakes head
        aPane.getChildren().add(s);
        for (int i = 0; i < 1 * s.getScaler(); i++) {
            newFood();
            s.eat(food);
        }

    }

    // Makes the food.
    private void newFood() {
        int x, y;
        do {
            x = r.nextInt((App.w / App.size) - 2) + 1; // Generate x within [1, (App.w / App.size - 2)]
            y = r.nextInt((App.w / App.size) - 2) + 1; // Generate y within [1, (App.w / App.size - 2)]
        } while (isFoodOnSnake(x * App.size, y * App.size));

        food = new Circle(x * App.size, y * App.size, (App.size / 2) * 0.8);
        food.setFill(Color.AQUAMARINE);
        aPane.getChildren().add(food);
    }

    // --------------------------CHECKERS -------------------------- //

    private boolean isFoodOnSnake(double x, double y) {
        for (Circle segment : s.getSnakeBody()) {
            if (segment.intersects(x, y, App.size, App.size)) {
                return true;
            }
        }
        return false;
    }

    private boolean hit() {
        boolean isHit = food.intersects(s.getBoundsInLocal());
        if (isHit) {
            score++;
        }
        return isHit;
    }

    private boolean gameover() {
        if (s.selfCollide()) {
            return true;
        }
        return false;
    }

    // -------------------------- GAME LOOP CODE -------------------------- //

    private void startGameLoop() {
        KeyFrame frame = new KeyFrame(Duration.millis(TIME / s.getScaler()), e -> gameLoopIteration());
        gameLoop = new Timeline(frame);
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    }

    private void stopGameLoop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    private void gameLoopIteration() {
        s.step();
        adjustLocation();
        if (hit()) {
            for (int i = 0; i < s.getScaler(); i++) {
                s.eat(food);
                newFood();
                score++;
            }
        }
        if (gameover()) {
            stopGameLoop();
            try {
                App.setRoot("secondary");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // -------------------------- TORUS DEFINITION -------------------------- //
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

    // -------------------------- MOVEMENT -------------------------- //
    /*
     * directions: 0 = UP, 1 = DOWN, 2 = LEFT, 3 = RIGHT
     */
    @FXML
    void moveSquareKeyPressed(KeyEvent event) throws IOException {
        if (event.getCode().equals(KeyCode.W) && direction != 1) {
            direction = 0;
            s.setCurrentDirection(0);
        } else if (event.getCode().equals(KeyCode.S) && direction != 0) {
            direction = 1;
            s.setCurrentDirection(1);
        } else if (event.getCode().equals(KeyCode.A) && direction != 3) {
            direction = 2;
            s.setCurrentDirection(2);
        } else if (event.getCode().equals(KeyCode.D) && direction != 2) {
            direction = 3;
            s.setCurrentDirection(3);
        }
    }

    // --------------------------BACKGROUND -------------------------- //
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
