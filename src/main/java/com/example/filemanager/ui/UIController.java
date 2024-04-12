package com.example.filemanager.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UIController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}