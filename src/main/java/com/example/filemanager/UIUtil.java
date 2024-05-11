package com.example.filemanager;

import com.example.filemanager.logic.LogicalConfiguration;
import com.example.filemanager.logic.commands.CommandContext;
import com.example.filemanager.logic.commands.commands.ListAllCommand;
import com.example.filemanager.logic.FileUtilFunctions;
import com.example.filemanager.logic.LogicalTab;
import com.example.filemanager.logic.exceptions.FileException;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.ArrayList;
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

        tab.setText(file.getName());
        tabPane.getTabs().add(tab);

        var logicalTab = new LogicalTab(tab, file, tabs);
        tabs.add(logicalTab);
    }


    /**
     * A function which fills the 'Places' list of important directories with fields like Documents, Downloads
     * and such with.
     *
     * @param pane the VBox list of the files to fill
     */
    public static void fillPlacesList(VBox pane) {

        var home = FileUtilFunctions.getHomeDirectory();

        ArrayList<File> files;
        try {
            var list_command = new ListAllCommand();
            var configuration = LogicalConfiguration.defaultConfiguration();
            var context = new CommandContext(home, null, configuration, null);

            files = list_command.execute(context);
        } catch (FileException e) {
            return;
        }

        addFileToPlacesList(pane, home);

        for (var file : files) {
            if (!file.isDirectory() || file.isHidden()) {
                continue;
            }
            addFileToPlacesList(pane, file);
        }
    }

    /**
     * A helper function of the fillPlacesList() function.
     * Adds a button leading to given directory to the list
     *
     * @param pane the list of the files to fill
     * @param file the file to add
     */
    private static void addFileToPlacesList(VBox pane, File file) {
        Button button = new Button();
        button.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-content-display: top;" +
                        "-fx-border-color: rgba(128,128,128,0.13);"
        );
        button.setText(file.getName());
        button.setPrefWidth(200);
        button.setAlignment(Pos.CENTER_LEFT);

        System.out.println();

        button.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                try {
                    UIController.setDirectoryInCurrentTab(file);
                } catch (FileException e) {
                    // ignored
                }
            }
        });

        pane.getChildren().add(button);
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


}
