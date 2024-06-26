package com.example.filemanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The main app.
 */
public class FileManagerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FileManagerApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 675);
        stage.setTitle("File Manager");
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest((x) -> UIController.onClose());
    }

    public static void main(String[] args) {
        launch();
    }
}