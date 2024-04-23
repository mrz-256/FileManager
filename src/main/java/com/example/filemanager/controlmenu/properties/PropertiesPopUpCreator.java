package com.example.filemanager.controlmenu.properties;

import com.example.filemanager.logic.LogicalTab;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.io.File;

/**
 * Creator of popup properties windows
 */
public class PropertiesPopUpCreator {

    /**
     * Creates a popup window with properties and information about a file
     *
     * @param logicalTab logical tab
     * @param file       the file whose properties are being displayed
     * @return a new popup window, closes when loses focus
     */
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
