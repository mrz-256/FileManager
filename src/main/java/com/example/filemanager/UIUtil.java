package com.example.filemanager;

import com.example.filemanager.logic.LogicalTab;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.LinkedList;

/**
 * A util class which mostly contains static functions that help create javafx constructs, i.e. it focuses on UI rather
 * than logic.
 */
public class UIUtil {

    /**
     * Creates a new tab with open directory `file` and adds it into the tabPane.
     * Also creates the corresponding Logical tab and adds it into the `tabs` list.
     *
     * @param tabPane the tabPane holding the newly created tab
     * @param file    directory open in given tab
     */
    public static void createNewTab(TabPane tabPane, LinkedList<LogicalTab> tabs, File file) {
        var grid = new GridPane();
        var scroll = new ScrollPane();
        var tab = new Tab();

        tab.setContent(scroll);
        scroll.setContent(grid);

        tab.setText(file.getAbsolutePath());
        tabPane.getTabs().add(tab);

        var logicalTab = new LogicalTab(tab, file, tabs);
        tabs.add(logicalTab);
    }

    /**
     * Creates an Alert of given type with given message.
     *
     * @param type   the type of alert
     * @param header the header text
     * @param body   the `text` text
     * @return the new alert
     */
    public static Alert createAlert(Alert.AlertType type, String header, String body) {
        var dialogue = new Alert(type);
        dialogue.setTitle(type.name());
        dialogue.setHeaderText(header);
        dialogue.setContentText(body);
        return dialogue;
    }

    /**
     * Either moves current directory in UIController to provided directory or shows Alert.
     * @param directory the directory to move to in the current tab.
     */
    public static void tryMovingToDirectory(File directory){
        try {
            UIController.setDirectoryInCurrentTab(directory);
        } catch (Exception e){
            var alert = createAlert(Alert.AlertType.ERROR, "Failed moving to directory.", e.getMessage());
            alert.show();
        }
    }
}
