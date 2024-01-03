package com.example;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.aplication.Aplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestSnakeController {
    private static final int WIDTH = 640;
    private static final int HEIGHT = WIDTH;
    private static final int ROWS = 20;
    private static final int COLUMMS = ROWS;
    private static final int SQUARE_SIZE = WIDTH / ROWS;








    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
