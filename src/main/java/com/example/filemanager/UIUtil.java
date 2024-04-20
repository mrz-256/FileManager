package com.example.filemanager;

import com.example.filemanager.logic.LogicalTab;
import com.example.filemanager.logic.exceptions.FileException;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
}
