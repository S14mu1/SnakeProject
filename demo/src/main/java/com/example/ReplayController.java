package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
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
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.io.BufferedReader;

public class ReplayController {

    @FXML
    private AnchorPane aPane;
    @FXML
    Canvas canvas = new Canvas(App.w, App.h);
    @FXML
    private GraphicsContext gc;
    private Timeline replayLoop;
    private BufferedReader reader;
    private final int TIME = 30; // Milliseconds between each gametick, "how fast do you want your replay sir?" 20 seems to be
    private Timeline gameLoop;
    private Snake s;

    /*  ---------------TO-DO-List--------------------
        1. Add apples to the replay, should be pretty easy,
            its the same formula except for printing only one circle class every tick
            just remember to make a seperate .txt file for apples, that should be easier 
            checkout newfood, after it has checked its conditions add the coordinates to a "Point" ArrayList
            afterwards use appendtoFile to add it to a seperate file 
            afterwards parse using similar ways such as with the snake 
            and afterwards print it, only problem is how should the program remove the apple? probably use the same code as the actual snake does


        2. Decide on how replay should work, can you have multiple replays of different games? Replay of levels?
        
        Otherwise the replay code should be finished by tomorrow, afterwards i hope to work on either the document or the ui
        Maybe adding leaderboard, should also be easy


    */
    private void drawSnake(ArrayList<Point> snakePoints) {

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Clear canvas
        drawBackground(gc); // Redraw background

        for (Point point : snakePoints) { //extended forloop, very useful yes. 
            gc.setFill(Color.BLACK);
            gc.fillOval(point.getX(), point.getY(), App.size, App.size); //draws the individual segments of the snake
        }
    }

    @FXML
    private void run() {
        aPane.getChildren().add(canvas); //begins the replay
        gc = canvas.getGraphicsContext2D();
        // Initialize the reader
        try {
            reader = new BufferedReader(new FileReader("demo\\src\\main\\java\\com\\example\\Replay.txt"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        startReplayLoop();
    }

    private void startReplayLoop() { //Don't ask! Seriously! only the gods(and Janus) knows how this code works
        KeyFrame frame = new KeyFrame(Duration.millis(TIME), e -> replayIteration());
        replayLoop = new Timeline(frame);
        replayLoop.setCycleCount(Timeline.INDEFINITE);
        replayLoop.play();
    }

    private void replayIteration() { 
        try {
            if (reader.ready()) {
                String line = reader.readLine(); //takes the next line of text 
                ArrayList<Point> points = parseLineToPoint(line); //parses the data into an array of point classes
                drawSnake(points); // Draw all points of the snake
            } else {
                replayLoop.stop();
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawBackground(GraphicsContext gc) { //standard drawbackground method
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


    private ArrayList<Point> parseLineToPoint(String line) { //takes a line of data from replay.txt and parses it into values for coordinates
        ArrayList<Point> points = new ArrayList<>();
        String[] pointStrings = line.replace("[","").replace("]","").split("\\|"); // Split the line into individual point strings

        for (String pointString : pointStrings) {
           try{
            if (!pointString.trim().isEmpty()) {
                String[] parts = pointString.replaceAll("[^0-9;,]", "").split(";");
               
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                points.add(new Point(x, y));
            }
            } catch (NumberFormatException e) {
                // Handle parsing error for individual point
                //System.out.println("Warning: Invalid point format - " + pointString); //current error unable to find issue, but seems to work fine notwithstanding so?
            } catch (ArrayIndexOutOfBoundsException e) {
                // Handle error if parts array doesn't have 2 elements
                System.out.println("Warning: Incomplete point data - " + pointString);
            } 
        

       
    }
return points;
}


}


