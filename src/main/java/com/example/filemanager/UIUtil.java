package com.example.filemanager;

import com.example.filemanager.logic.LogicalDoubleTab;
import com.example.filemanager.logic.exceptions.FileException;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

/**
 * A class that holds all tabs and helper static methods for work with App
 */
public class UIUtil {
    /**
     * List of all Logical tabs in app
     */
    private static LinkedList<LogicalDoubleTab> tabs;

    /**
     * Initializes tabs linkedlist
     */
    public static void init() {
        tabs = new LinkedList<>();
    }

    /**
     * Updates all tabs with given width
     * @param width the new width of the TabPane holding all tabs.
     */
    public static void updateAllTabs(int width){
        for(var tab : tabs){
            tab.updateTab(width);
        }
    }

    /**
     * Creates a new tab with open directory `file`
     * @param tabPane the tabPane holding the newly created tab
     * @param file directory open in given tab
     */
    public static void createNewTab(TabPane tabPane, File file) {

        FXMLLoader loader = new FXMLLoader(UIUtil.class.getResource("tab.fxml"));
        try {
            Tab tab = loader.load();
            tab.setText(file.getName());

            tabPane.getTabs().add(tab);

            LogicalDoubleTab logicalTab = new LogicalDoubleTab(tab, file);
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
    public static Image loadImageIcon(File file, int size) throws FileException {
        if (!file.exists()) throw new FileException("File doesn't exist - can't create icon", file);
/*
        if (file.isDirectory()){
            return new Image(
                    new File("icons/file_icons/icon_hidden.png").toURI().toString(),
                    size, size, true, true
            );
        }

        if (file.isHidden()){
            return new Image(
                    new File("icons/file_icons/icon_hidden.png").toURI().toString(),
                    size, size, true, true
            );
        }

        String filename = file.getName();
        String extension = filename.replaceFirst(".*\\.(.*)", "$1");

        // image can be used as an icon
        if (extension.equals("png") || extension.equals("jpg") || extension.equals("gif") || extension.equals("bmp")) {
            return new Image(file.getAbsolutePath(), size, size, true, true);
        }

        // there is an icon for given extension
        File icon = new File("icons/file_icons/icon_" + extension + ".png");
        if (icon.exists()) return new Image(icon.getAbsolutePath(), size, size, true, true);
*/
        return new Image(
                new File("icons/file_icons/icon_hidden.png").toURI().toString(),
                size, size, true, true
        );
    }

}
