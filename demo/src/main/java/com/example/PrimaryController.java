package com.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class PrimaryController {
    // -------------------------- VARIABLES -------------------------- //
    @FXML
    private AnchorPane aPane;
    private Circle food;
    private Rectangle block;
    private Text scoreTxt;
    private Random r = new Random(); // Used in foods position
    private Snake s;
    private int direction;
    Canvas canvas = new Canvas(App.w, App.h);
    private GraphicsContext gc;
    private int score;
    private final int TIME = 100; // Milliseconds between each gametick
    private Timeline gameLoop;
    private ArrayList<Rectangle> layout = new ArrayList<Rectangle>();
    private ArrayList<Point> replayApple = new ArrayList<Point>();
    private ArrayList<Point> snakeCoordinates = new ArrayList<Point>();
    private Image scorenImage = new Image("/scoren.png");
    private Image pngImage = new Image("/scoren.png");
    private ImageView scorenImageView = new ImageView(scorenImage);
    private ImageView pngImageView = new ImageView(pngImage);

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    // -------------------------- Game logic --------------------------//
    /*
     * Here all the game logic will be this is how the game is controlled
     */
    // -------------------------- STARTUP -------------------------- //
    private void run(int state) { // Runs when the button is pressed, starts up the game
        clearFileContent("Replay.txt");
        clearFileContent("Apple.txt");
        App.setState(state);
        if (state == 0) {
            int size = 20;
            App.setSize(size);
            aPane.getChildren().add(canvas);
            gc = canvas.getGraphicsContext2D();
            drawBackground(gc);
            newSnake(state);
            newFood();
            gc.drawImage(scorenImage, 2, -50, 250, 120);
            scoreTxt = new Text(130, 47, score + "");
            scoreTxt.setFont(Font.font("Cooper Black", FontWeight.BOLD, 30));
            scoreTxt.setFill(Color.web("42ED47"));
            aPane.getChildren().add(scoreTxt);
            canvas.requestFocus(); // Ensure that the Canvas has focus
            startGameLoop(1);

        } else {
            aPane.getChildren().add(canvas);
            gc = canvas.getGraphicsContext2D();
            Grid g = new Grid();
            g.setLayout(state);
            App.setSize(g.getSize());
            drawBackground(gc);
            drawLevel(g.getLevel()); // Assuming level 1 for now, you can pass the appropriate level parameter
            newSnake(state);
            newFood();
            gc.drawImage(scorenImage, 2, -50, 250, 120);
            scoreTxt = new Text(130, 47, score + "");
            scoreTxt.setFont(Font.font("Cooper Black", FontWeight.BOLD, 30));
            scoreTxt.setFill(Color.web("42ED47"));
            aPane.getChildren().add(scoreTxt);
            canvas.requestFocus(); // Ensure that the Canvas has focus
            startGameLoop(g.getSpeed());

        }
    }

    public void newSnake(int state) {
        if (App.row % 2 == 0) {
            if (state == 2 || state == 4 || state == 5) {
                s = new Snake(1 * App.size + App.size / 2, 1 * App.size + App.size / 2, App.size / 2 * 0.90);
            } else {
                s = new Snake(App.w / 2 + App.size / 2, App.h / 2 + App.size / 2, App.size / 2 * 0.90);
            }
        } else {
            s = new Snake(App.w / 2, App.h / 2, App.size / 2 * 0.90);
        }
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
        replayApple.add(new Point(x, y));
        food.setFill(Color.web("5BA432"));
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

    private void startGameLoop(double speed) {
        KeyFrame frame = new KeyFrame(Duration.millis(TIME / s.getScaler() * speed), e -> gameLoopIteration());
        gameLoop = new Timeline(frame);
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        gameLoop.play();
    }

    private void stopGameLoop() {
        if (gameLoop != null) {
            gameLoop.stop();
            App.setReplay(true);
            appendTextToFile("Apple.txt", replayApple);
            appendTextToFile("Replay.txt", snakeCoordinates);
        }
    }

    private void gameLoopIteration() {
        s.step();
        adjustLocation();
        snakeCoordinates.add(new Point((int) s.getCenterX(), (int) s.getCenterY()));
        if (hit()) {
            score++;
            scoreTxt.setText(score + "");
            for (int i = 0; i < s.getScaler(); i++) {
                s.eat(food);
                newFood();
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
            s.setCenterX(App.w - App.size / 2);
        } else if (s.getCenterX() > App.w) {
            s.setCenterX(0 + App.size / 2);
        }
        if (s.getCenterY() < 0) {
            s.setCenterY(App.h - App.size / 2);
        } else if (s.getCenterY() > App.h) {
            s.setCenterY(0 + App.size / 2);
        }
    }

    // -----------------------File
    // Processing-------------------------------------------------------------------\\
    private static void appendTextToFile(String filePath, ArrayList<Point> array) {
        for (int i = 0; i < array.size(); i++) {
            int x = array.get(i).getX();
            int y = array.get(i).getY();
            String text = (x + " " + y);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                writer.write(text);
                writer.newLine(); // Writes a new line
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // -------------------------- MOVEMENT -------------------------- //
    /*
     * directions: 0 = UP, 1 = DOWN, 2 = LEFT, 3 = RIGHT
     */
    @FXML
    void moveSquareKeyPressed(KeyEvent event) throws IOException {
        if ((event.getCode().equals(KeyCode.W) && direction != 1)
                || (event.getCode().equals(KeyCode.UP) && direction != 1)) {
            direction = 0;
            s.setCurrentDirection(0);
        } else if ((event.getCode().equals(KeyCode.S) && direction != 0)
                || (event.getCode().equals(KeyCode.DOWN) && direction != 0)) {
            direction = 1;
            s.setCurrentDirection(1);
        } else if ((event.getCode().equals(KeyCode.A) && direction != 3)
                || (event.getCode().equals(KeyCode.LEFT) && direction != 3)) {
            direction = 2;
            s.setCurrentDirection(2);
        } else if ((event.getCode().equals(KeyCode.D) && direction != 2)
                || (event.getCode().equals(KeyCode.RIGHT) && direction != 2)) {
            direction = 3;
            s.setCurrentDirection(3);
        }
    }

    // -------------------------- LEVEL DRAWING -------------------------- //
    private void drawLevel(int level) {
        Grid g = new Grid();
        g.setLayout(level);

        for (int i = 0; i < g.getLayout().length; i++) {
            for (int j = 0; j < g.getLayout()[i].length; j++) {
                block = new Rectangle(i * App.size, j * App.size, App.size, App.size);
                if (g.getLayout()[i][j] == 1) {
                    // Set color or style for the blocks in the grid
                    block.setFill(Color.web("5E042D"));
                    aPane.getChildren().add(block);
                    layout.add(block);
                }
            }
        }
    }

    // --------------------------BACKGROUND -------------------------- //
    private void drawBackground(GraphicsContext gc) {
        for (int i = -1; i < App.row; i++) {
            for (int j = -1; j < App.row; j++) {
                if ((i + j) % 2 == 0) {
                    gc.setFill(Color.web("8A0641"));
                } else {
                    gc.setFill(Color.web("940646"));
                }
                gc.fillRect(i * App.size, j * App.size, App.size, App.size);
            }

        }

    }

    // -------------------------- BUTTONS -------------------------- //
    @FXML
    private void endless() {
        System.out.println("Current Working Directory: " + System.getProperty("user.dir"));
        run(0);
    }

    @FXML
    private void first() {
        run(1);
    }

    @FXML
    private void second() {
        run(2);
    }

    @FXML
    private void third() {
        run(3);
    }

    @FXML
    private void fourth() {
        run(4);
    }

    @FXML
    private void fifth() {
        run(5);
    }

    // Clears contents of file
    private void clearFileContent(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            // Overwrite the file with an empty string
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Button back
    @FXML
    private void switchToMain() throws IOException {
        App.setRoot("mainMenu");
    }

}
