package com.example.filemanager;

import com.example.filemanager.logic.FileUtilFunctions;
import com.example.filemanager.logic.LogicalTab;
import com.example.filemanager.logic.exceptions.FileException;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class UIUtil {
    /**
     * Creates a new tab with open directory `file`
     * @param tabPane the tabPane holding the newly created tab
     * @param file directory open in given tab
     */
    public static void createNewTab(TabPane tabPane, LinkedList<LogicalTab> tabs, File file) {

        FXMLLoader loader = new FXMLLoader(UIController.class.getResource("tab.fxml"));
        try {
            Tab tab = loader.load();
            tab.setText(file.getName());

            tabPane.getTabs().add(tab);

            var logicalTab = new LogicalTab(tab, file);
            tabs.add(logicalTab);

        } catch (IOException e) {
            // todo:
            System.out.println("TODO: FAILED TAB CREATION - " + e.getMessage());
        }
    }

    /**
     * Used to load icon of a file.
     * Loads either the image if the filetype is png, jpg, bmp or a file icon in other cases.
     * @param file the file to make an icon for
     * @param size the size of created icon
     * @return newly created icon
     * @throws FileException when file doesn't exist
     */
    public static ImageView loadImageIcon(File file, int size) throws FileException {
        if (!file.exists()) throw new FileException("File doesn't exist - can't create icon", file);

        if (file.isDirectory()){
            return new ImageView(
                    UIController.class.getResource("/icons/file_icons/icon_directory.png").toExternalForm()
            );
        }

        if (file.isHidden()){
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
        var resource = UIController.class.getResource("/icons/file_icons/icon_"+extension+".png");
        if (resource != null){
            return new ImageView(resource.toExternalForm());
        }

        // default 'unknown' (?) icon
        return new ImageView(
                UIController.class.getResource("/icons/file_icons/icon_unknown.png").toExternalForm()
        );
    }


    /**
     * A function which fills the 'Places' list of important directories with fields like Documents, Downloads
     * and such with.
     * @param pane the VBox list of the files to fill
     */
    public static void fillPlacesList(VBox pane){
        String[] names = {"", "Pictures", "Documents", "Downloads", "Music", "Videos", "Trash"};

        for(var name : names){
            var file = new File(FileUtilFunctions.getHomeDirectory(), name);
            if (file.exists() && file.isDirectory()){
                addFileToPlacesList(pane, file);
            }
        }

    }

    /**
     * A helper function of the fillPlacesList() function.
     * Adds a button leading to given directory to the list
     * @param pane the list of the files to fill
     * @param file the file to add
     */
    private static void addFileToPlacesList(VBox pane, File file){
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
}
