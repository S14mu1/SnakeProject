package com.example;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;

public class TestSnakeController {
    private static final int WIDTH = 640;
    private static final int HEIGHT = WIDTH;
    private static final int ROWS = 20;
    private static final int COLUMMS = ROWS;
    private static final int SQUARE_SIZE = WIDTH / ROWS;

    private static final int Right = 0;
    private static final int Left = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;

    






    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
