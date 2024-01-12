package com.example;

import java.io.*;
import java.util.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class ReplayController {

    @FXML
    private AnchorPane aPane;
    @FXML
    Canvas canvas = new Canvas(App.w, App.h);
    @FXML
    private GraphicsContext gc;
    private Rectangle block;
    private Timeline replayLoop;
    private Snake s;
    private final int TIME = 20; // Milliseconds between each gametick, "how fast do you want your replay sir?"
    private Circle food;
    private File f = new File("Replay.txt");
    private File a = new File("Apple.txt");
    private Scanner scanner;
    private Scanner aScanner;
    private ArrayList<Rectangle> layout = new ArrayList<Rectangle>();

    @FXML
    private void run() throws FileNotFoundException {
        if (App.state == 0) {
            scanner = new Scanner(f);
            aScanner = new Scanner(a);
            aPane.getChildren().add(canvas); // begins the replay
            gc = canvas.getGraphicsContext2D();
            drawBackground(gc);
            newSnake(0, aScanner);
            newFood(aScanner);
            startReplayLoop(scanner, aScanner);
        } else {
            scanner = new Scanner(f);
            aScanner = new Scanner(a);
            Grid g = new Grid();
            g.setLayout(App.state);
            aPane.getChildren().add(canvas); // begins the replay
            gc = canvas.getGraphicsContext2D();
            drawBackground(gc);
            drawLevel(App.state);
            newSnake(0, aScanner);
            newFood(aScanner);
            startReplayLoop(scanner, aScanner);
        }
    }

    public void newSnake(int state, Scanner aScanner) {
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
            newFood(aScanner);
            s.eat(food);
        }

    }

    private void newFood(Scanner aScanner) {
        int x = aScanner.nextInt();
        int y = aScanner.nextInt();
        food = new Circle(x, y, App.size / 2 * 0.8);
        food.setFill(Color.web("5BA432"));
        aPane.getChildren().add(food);
    }

    private void startReplayLoop(Scanner scanner, Scanner aScanner) throws FileNotFoundException {
        KeyFrame frame = new KeyFrame(Duration.millis(TIME / s.getScaler()), e -> {
            try {
                replayIteration(scanner, aScanner);
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        replayLoop = new Timeline(frame);
        replayLoop.setCycleCount(Timeline.INDEFINITE);
        replayLoop.play();
    }

    private void replayIteration(Scanner scanner, Scanner aScanner) throws IOException {
        if (scanner.hasNext()) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            s.replayStep(x, y);
            if (hit()) {
                for (int i = 0; i < s.getScaler(); i++) {
                    s.eat(food);
                    newFood(aScanner);
                }
            }
        } else {
            stopReplay();
        }
    }

    private void stopReplay() throws IOException {
        if (replayLoop != null) {
            replayLoop.stop();
            App.setRoot("mainMenu");
        }
    }

    private boolean hit() {
        boolean isHit = food.intersects(s.getBoundsInLocal());
        return isHit;
    }

    private void drawBackground(GraphicsContext gc) { // standard drawbackground method
        for (int i = -1; i < App.row + 1; i++) {
            for (int j = -1; j < App.row + 1; j++) {
                if ((i + j) % 2 == 0) {
                    gc.setFill(Color.web("8A0641"));
                } else {
                    gc.setFill(Color.web("940646"));
                }
                gc.fillRect(i * App.size, j * App.size, App.size, App.size);
            }

        }

    }

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
}