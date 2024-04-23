package com.example.filemanager.properties;

import com.example.filemanager.logic.LogicalTab;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.io.File;

public class PropertiesPopUpCreator {

    public static Stage createPropertiesPopUp(LogicalTab logicalTab, File file) {
        var contents = new TabPane();
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

        fillPropertiesPopUp(contents, file);

        return stage;
    }

    private static void fillPropertiesPopUp(TabPane contents, File file) {
        PropertiesAddGeneral.addGeneralTab(contents, file);
        PropertiesAddChecksum.addChecksumTab(contents, file);
    }


}
