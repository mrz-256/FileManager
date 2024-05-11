package com.example.filemanager;

import com.example.filemanager.logic.LogicalConfiguration;
import com.example.filemanager.logic.commands.CommandContext;
import com.example.filemanager.logic.commands.commands.ListAllCommand;
import com.example.filemanager.logic.sort_strategy.NameStrategy;
import com.example.filemanager.ui_logic.controlmenu.ControlMenuCreator;
import com.example.filemanager.logic.FileUtilFunctions;
import com.example.filemanager.logic.LogicalTab;
import com.example.filemanager.logic.exceptions.FileException;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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

        ArrayList<File> files;
        try {
            files = new ListAllCommand().execute(new CommandContext(FileUtilFunctions.getHomeDirectory(), null, LogicalConfiguration.defaultConfiguration(), null));
        } catch (FileException e) {
            // todo error
            return;
        }

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
     * Used to load icon of a file.
     * Loads either the image if the filetype is png, jpg, bmp or a file icon in other cases.
     *
     * @param file the file to make an icon for
     * @param size the size of created icon
     * @return newly created icon
     * @throws FileException when file doesn't exist
     */
    public static ImageView loadImageIcon(File file, int size) throws FileException {
        if (!file.exists()) {
            throw new FileException("File doesn't exist - can't create icon", file);
        }

        String type = FileUtilFunctions.getFileType(file);

        // types which can be used as the icon are loaded here
        if (type.equals("png")
                || type.equals("jpg")
                || type.equals("gif")
                || type.equals("bmp")
                || type.equals("jpeg")
        ) {
            var uri = file.toURI().toString();
            var image = UIController.getLoadedImage(uri);

            if (image == null) {
                /// takes ~20% of the application execution
                image = new Image(uri, size, size, true, true);
                UIController.setLoadedImage(uri, image);
            }

            return new ImageView(image);
        }

        // if I have an icon drawn for given file type it is loaded
        var resource = UIController.class.getResource("/icons/file_icons/icon_" + type + ".png");
        if (resource != null) {
            return new ImageView(resource.toExternalForm());
        }

        // default 'unknown' (?) icon
        resource = UIController.class.getResource("/icons/file_icons/icon_unknown.png");
        if (resource != null) {
            return new ImageView(resource.toExternalForm());
        }
        return null;
    }

    /**
     * Creates an icon button for a file icon.
     *
     * @param file     the file to make an icon button from
     * @param size     the size of the icon button
     * @param styleCSS style of the icon button
     * @return newly created icon button
     */
    public static Button createIconButton(File file, int size, String styleCSS, double iconToButtonRation) {
        Button button = new Button();
        button.setPrefSize(size, size);
        button.setMinSize(size, size);

        button.setTooltip(new Tooltip(file.getAbsolutePath()));

        button.setStyle(styleCSS);

        try {
            button.setGraphic(UIUtil.loadImageIcon(file, (int) (size * iconToButtonRation)));
        } catch (FileException ignore) {
            // icon will be plain button with no icon
        }

        return button;
    }

    /**
     * Sets the onClick function so that it moves tab to given file if it's a directory or execute it if it's a file
     *
     * @param button     the button to bind the function to
     * @param logicalTab the logical tab of given tab
     * @param file       the file to use for action
     */
    public static void setOnFileClickFunction(Button button, LogicalTab logicalTab, File file) {
        button.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (file.isDirectory()) {
                    try {
                        logicalTab.setDirectory(file);
                        UIController.updateCurrentTab();
                    } catch (FileException e) {
                        var alert = createAlert(Alert.AlertType.ERROR, "Failed moving to directory", e.getMessage());
                        alert.show();
                    }
                } else {
                    try {
                        logicalTab.executeCommand("open", file);
                    } catch (FileException e) {
                        var alert = createAlert(Alert.AlertType.ERROR, "Failed opening file", e.getMessage());
                        alert.show();
                    }
                }
            } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                var menu = ControlMenuCreator.createControlContextMenu(logicalTab, file);
                button.setContextMenu(menu);
            }
        });
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
