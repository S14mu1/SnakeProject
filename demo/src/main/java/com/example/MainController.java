package com.example;


import java.io.IOException;

import javafx.fxml.FXML;

public class MainController {
            @FXML
    public void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    public void switchtoReplays() throws IOException {
        App.setRoot("Replays");
    }

    @FXML
    public void switchToSaves() throws IOException {
        App.setRoot("Saves");
    }
    
}