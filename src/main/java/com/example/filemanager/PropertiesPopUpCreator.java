package com.example.filemanager;

import com.example.filemanager.logic.LogicalTab;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class PropertiesPopUpCreator {

    public static void createPropertiesPopUp(LogicalTab logicalTab, File file){
        var contents = new VBox();
        var scene = new Scene(contents);
        var stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("properties of \"" + file.getName() + "\"");
        stage.setAlwaysOnTop(true);

        stage.focusedProperty().addListener(observable -> {
            if (!stage.isFocused()) stage.close();
        });

        // kind of dirty
        logicalTab.getTab().getTabPane().getScene().getWindow().setOnCloseRequest((x) -> {
            stage.close();
        });

    }
}
