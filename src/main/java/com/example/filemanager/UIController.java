package com.example.filemanager;

import com.example.filemanager.logic.FileUtilFunctions;
import com.example.filemanager.logic.LogicalTab;
import com.example.filemanager.logic.exceptions.FileException;
import com.example.filemanager.logic.sort_strategy.LastModifiedStrategy;
import com.example.filemanager.logic.sort_strategy.NameStrategy;
import com.example.filemanager.logic.sort_strategy.SizeStrategy;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.LinkedList;

/**
 * A class that holds all tabs and helper static methods for work with App
 */
public class UIController {
    @FXML
    private CheckMenuItem showHiddenCheckbox;
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


    public static UIController getInstance() {
        return instance;
    }

    /**
     * Initializes tabs linkedlist
     */
    @FXML
    void initialize() {
        tabs = new LinkedList<>();

        UIUtil.createNewTab(tabPane, tabs, FileUtilFunctions.getHomeDirectory());
        tabPane.widthProperty().addListener((observableValue, number, newNumber) -> {
            UIController.updateCurrentTab();
        });
        instance = this;

        UIUtil.fillPlacesList(places);
    }

    /**
     * Updates all tabs
     */
    public static void updateAllTabs() {
        for (var tab : tabs) {
            tab.updateTab((int) getInstance().tabPane.getWidth());
        }
    }

    /**
     * Updates selected(current) tab
     */
    public static void updateCurrentTab() {
        getInstance().getCurrentLogicalTab().updateTab((int) getInstance().tabPane.getWidth());
    }

    public static void setDirectoryInCurrentTab(File directory) throws FileException {
        getInstance().getCurrentLogicalTab().setDirectory(directory);
        updateCurrentTab();
    }

    /**
     * Returns the index of the selected tab in the tabPane
     *
     * @return the index
     */
    public int getCurrentTabIndex() {
        return tabPane.getSelectionModel().getSelectedIndex();
    }

    /**
     * @return the currently selected logical tab
     */
    public LogicalTab getCurrentLogicalTab() {
        return tabs.get(getCurrentTabIndex());
    }

    @FXML
    public void onBackClicked() {
        var tab = getCurrentLogicalTab();
        tab.moveBack();
        tab.updateTab((int) tabPane.getWidth());
    }

    @FXML
    public void onShowHiddenClicked() {
        var tab = getCurrentLogicalTab();
        tab.getConfiguration().showHiddenFiles = showHiddenCheckbox.isSelected();
        tab.updateTab((int) tabPane.getWidth());
    }

    //region sorting methods
    @FXML
    public void onSortFilesByName() {
        var tab = getCurrentLogicalTab();
        tab.getConfiguration().sortStrategy = new NameStrategy();
        tab.updateTab((int) tabPane.getWidth());
    }

    @FXML
    public void onSortFilesBySize() {
        var tab = getCurrentLogicalTab();
        tab.getConfiguration().sortStrategy = new SizeStrategy();
        tab.updateTab((int) tabPane.getWidth());
    }

    @FXML
    public void onSortFilesByLastModification() {
        var tab = getCurrentLogicalTab();
        tab.getConfiguration().sortStrategy = new LastModifiedStrategy();
        tab.updateTab((int) tabPane.getWidth());
    }
    //endregion

    //region new tab
    @FXML
    public void onNewTabClicked() {
        UIUtil.createNewTab(tabPane, tabs, getCurrentLogicalTab().getDirectory());
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        updateAllTabs();
    }

    public void onTabPaneUpdate() {
        if (tabPane.getTabs().size() == 1) {
            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        }
        updateAllTabs();
    }
    //endregion


}
