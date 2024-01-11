package com.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class PrimaryController {
    // -------------------------- VARIABLES -------------------------- //
    @FXML
    private AnchorPane aPane;
    private Circle food;
    private Rectangle block;
    private Random r = new Random(); // Used in foods position
    private Snake s;
    private int direction;
    Canvas canvas = new Canvas(App.w, App.h);
    private GraphicsContext gc;
    private int score;
    private final int TIME = 100; // Milliseconds between each gametick
    static String filepath = ("demo\\src\\main\\java\\com\\example\\Replay.txt");
    private Timeline gameLoop;
    private ArrayList<Rectangle> layout = new ArrayList<Rectangle>();

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
            x = (int) (Math.random() * App.row) * App.size + App.size / 2;
            y = (int) (Math.random() * App.row) * App.size + App.size / 2;
        } while (isFoodTooCloseToSnake(x, y) || isFoodOnSnakeHead(x, y) || isFoodTooCloseToLayout(x, y));

        food = new Circle(x, y, (App.size / 2) * 0.8);
        food.setFill(Color.AQUAMARINE);
        aPane.getChildren().add(food);
    }

    // --------------------------CHECKERS -------------------------- //

    private boolean isFoodOnSnakeHead(double foodX, double foodY) { // Checks if food spawns on head
        double distanceThreshold = 5.0;
        double distance = Math.sqrt(Math.pow(foodX - s.getCenterX(), 2) + Math.pow(foodY - s.getCenterY(), 2));
        return distance < distanceThreshold;
    }

    private boolean isFoodTooCloseToSnake(double foodX, double foodY) { // Checks if food spawns on rest of body
        for (Circle bodyPart : s.getSnakeBody()) {
            double distanceThreshold = 15.0;
            double distance = Math
                    .sqrt(Math.pow(foodX - bodyPart.getCenterX(), 2) + Math.pow(foodY - bodyPart.getCenterY(), 2));
            if (distance < distanceThreshold) {
                return true;
            }
        }

        return false;
    }

    private boolean isFoodTooCloseToLayout(double foodX, double foodY) { // Checks if food spawns on the level walls
        for (Rectangle b : layout) {
            double distanceThreshold = App.size;
            double blockCenterX = b.getX() + b.getWidth() / 2;
            double blockCenterY = b.getY() + b.getHeight() / 2;

            double distance = Math.sqrt(Math.pow(foodX - blockCenterX, 2) + Math.pow(foodY - blockCenterY, 2));

            if (distance < distanceThreshold) {
                return true;
            }
        }

        return false;
    }

    // Collision between food and snake
    private boolean hit() {
        boolean isHit = food.intersects(s.getBoundsInLocal());
        if (isHit) {
            score++;
        }
        return isHit;
    }

    // Collision between snake and itself, and snake and level
    private boolean gameover() {
        if (s.selfCollide()) {
            return true;
        }
        for (int i = 0; i < layout.size(); i++) {
            if (layout.get(i).intersects(s.getBoundsInLocal())) {
                return true;
            }
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
        appendTextToFile(filepath, s.getSnakeCoordinates());
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

    // -----------------------File
    // Processing-------------------------------------------------------------------\\
    private static void appendTextToFile(String filePath, ArrayList<Point> array) {
        String text = ("" + array);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(text);
            writer.newLine(); // Writes a new line
        } catch (IOException e) {
            e.printStackTrace();
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
        for (int i = -1; i < App.row; i++) {
            for (int j = -1; j < App.row; j++) {
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