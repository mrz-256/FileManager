package com.example.filemanager;

import com.example.filemanager.logic.FileUtilFunctions;
import com.example.filemanager.logic.LogicalTab;
import com.example.filemanager.logic.exceptions.FileException;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class UIUtil {
    /**
     * Creates a new tab with open directory `file`
     * Also creates the corresponding Logical tab
     *
     * @param tabPane the tabPane holding the newly created tab
     * @param file    directory open in given tab
     */
    public static void createNewTab(TabPane tabPane, LinkedList<LogicalTab> tabs, File file) {

        FXMLLoader loader = new FXMLLoader(UIController.class.getResource("tab.fxml"));
        try {
            Tab tab = loader.load();
            tab.setText(file.getName());

            tabPane.getTabs().add(tab);

            var logicalTab = new LogicalTab(tab, file, tabs);
            tabs.add(logicalTab);

        } catch (IOException e) {
            // todo:
            System.out.println("TODO: FAILED TAB CREATION - " + e.getMessage());
        }
    }


    /**
     * A function which fills the 'Places' list of important directories with fields like Documents, Downloads
     * and such with.
     *
     * @param pane the VBox list of the files to fill
     */
    public static void fillPlacesList(VBox pane) {
        String[] names = {"", "Pictures", "Documents", "Downloads", "Music", "Videos", "Trash"};

        for (var name : names) {
            var file = new File(FileUtilFunctions.getHomeDirectory(), name);
            if (file.exists() && file.isDirectory()) {
                addFileToPlacesList(pane, file);
            }
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

        button.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                try {
                    UIController.setDirectoryInCurrentTab(file);
                } catch (FileException e) {
                    // todo
                    System.out.println("TODO: UI UTIL ADD FILE TO PLACES ERROR " + e.getMessage());
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
        if (!file.exists()) throw new FileException("File doesn't exist - can't create icon", file);

        if (file.isDirectory()) {
            return new ImageView(
                    UIController.class.getResource("/icons/file_icons/icon_directory.png").toExternalForm()
            );
        }

        if (file.isHidden()) {
            return new ImageView(
                    UIController.class.getResource("/icons/file_icons/icon_hidden.png").toExternalForm()
            );
        }

        String filename = file.getName();
        String extension = filename.replaceFirst(".*\\.(.*)", "$1");

        //// image can be used as an icon
        if (extension.equals("png")
                || extension.equals("jpg")
                || extension.equals("gif")
                || extension.equals("bmp")
                || extension.equals("jpeg")
        ) {
            return new ImageView(new Image(file.toURI().toString(), size, size, true, true));
        }

        // there is an icon for given extension
        var resource = UIController.class.getResource("/icons/file_icons/icon_" + extension + ".png");
        if (resource != null) {
            return new ImageView(resource.toExternalForm());
        }

        // default 'unknown' (?) icon
        return new ImageView(
                UIController.class.getResource("/icons/file_icons/icon_unknown.png").toExternalForm()
        );
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
                        System.out.println("TODO: GRID STRATEGY ERROR " + e.getMessage());
                    }
                } else {
                    // todo: execute file
                }
            } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                //createInfoWindow(logicalTab, file).show();
                var menu = new ContextMenu();
                fillControlMenu(menu,logicalTab, file);

                button.setContextMenu(menu);
            }
        });
    }


    /**
     * Creates an info window to show parameters and a description of a file
     *
     * @param logicalTab the tab containing the file
     * @param file       the file of the window
     * @return created stage of a window
     */
    public static Stage createInfoWindow(LogicalTab logicalTab, File file) {
        var content = new ListView<String>();
        Scene scene = new Scene(content);
        Stage stage = new Stage();
        stage.setTitle("info");
        stage.setScene(scene);
        stage.setAlwaysOnTop(true);

        // ->>

        stage.focusedProperty().addListener(observable -> {
            if (!stage.isFocused()) stage.close();
        });

        // kind of dirty
        logicalTab.getTab().getTabPane().getScene().getWindow().setOnCloseRequest((x) -> {
            stage.close();
        });


        return stage;
    }

    public static void fillControlMenu(ContextMenu menu, LogicalTab logicalTab, File file) {
        var copy = new MenuItem("copy");
        var copyPath = new MenuItem("copy path");
        var cut = new MenuItem("cut");
        var delete = new MenuItem("delete");
        var duplicate = new MenuItem("duplicate");
        var rename = new MenuItem("rename");
        var parameters = new MenuItem("parameters");

        menu.getItems().addAll(
                copy, copyPath,
                new SeparatorMenuItem(), cut, delete, duplicate, rename,
                new SeparatorMenuItem(), parameters
        );

        //region action copy
        copy.setOnAction((x) -> {
            var clipboard = Clipboard.getSystemClipboard();
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putFiles(List.of(file));
            clipboard.setContent(clipboardContent);
        });

        copyPath.setOnAction((x) -> {
            var clipboard = Clipboard.getSystemClipboard();
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString(file.getAbsolutePath());
            clipboard.setContent(clipboardContent);
        });
        //endregion

        //region affecting file
        cut.setOnAction((x) -> {
            var clipboard = Clipboard.getSystemClipboard();
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putFiles(List.of(file));
            clipboard.setContent(clipboardContent);

            try {
                logicalTab.executeCommand("delete_files", file);
            } catch (FileException e) {
                // todo:
                System.out.println(e.getMessage());
            }

            UIController.updateCurrentTab();
        });

        delete.setOnAction((x) -> {
            try {
                logicalTab.executeCommand("delete_files", file);
            } catch (FileException e) {
                // todo:
                System.out.println(e.getMessage());
            }
            UIController.updateCurrentTab();
        });

        duplicate.setOnAction((x) -> {
            try {
                logicalTab.executeCommand("paste_files", file);
            } catch (FileException e) {
                //todo:
                System.out.println(e.getMessage());
            }
            UIController.updateCurrentTab();
        });
        //endregion

        rename.setOnAction((x) -> {
            // todo: this requires popup window
        });

        parameters.setOnAction((x) -> {
            // todo: this requires popup window
        });

    }
}
