package com.example.filemanager;

import com.example.filemanager.logic.FileUtilFunctions;
import com.example.filemanager.logic.LogicalTab;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.LinkedList;

/**
 * A class that holds all tabs and helper static methods for work with App
 */
public class UIController {
    @FXML
    private VBox places;
    @FXML
    private VBox recent;
    /**
     * TabPane holding all javaFX tabs
     */
    @FXML
    private TabPane tabPane;

    /**
     * Singleton instance
     */
    private static UIController instance;

    /**
     * List of all Logical tabs in app
     */
    private static LinkedList<LogicalTab> tabs;

    /**
     * Initializes tabs linkedlist
     */
    @FXML
    void initialize(){
        tabs = new LinkedList<>();

        UIUtil.createNewTab(tabPane, tabs, new File(FileUtilFunctions.getHomeDirectory(), "Pictures"));
        tabPane.widthProperty().addListener((observableValue, number, newNumber) -> {
            UIController.updateAllTabs();
        });
        instance = this;
    }

    public static UIController getInstance() {
        return instance;
    }

    /**
     * Updates all tabs with tabPane width
     */
    public static void updateAllTabs(){
        for(var tab : tabs){
            tab.updateTab((int) getInstance().tabPane.getWidth());
        }
    }



}
