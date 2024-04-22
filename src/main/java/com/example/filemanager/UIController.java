package com.example.filemanager;

import com.example.filemanager.logic.FileUtilFunctions;
import com.example.filemanager.logic.LogicalConfiguration;
import com.example.filemanager.logic.LogicalTab;
import com.example.filemanager.logic.exceptions.FileException;
import com.example.filemanager.logic.sort_strategy.LastModifiedStrategy;
import com.example.filemanager.logic.sort_strategy.NameStrategy;
import com.example.filemanager.logic.sort_strategy.SizeStrategy;
import com.example.filemanager.ui.display_strategy.BoxStrategy;
import com.example.filemanager.ui.display_strategy.ListStrategy;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private TabPane tabPane;
    @FXML
    private VBox places;
    @FXML
    private VBox recent;

    @FXML
    private TitledPane searchPane;
    @FXML
    public TextField searchTextField;
    @FXML
    public ChoiceBox<String> searchChoiceBox;
    @FXML
    public Button searchConfirmButton;
    @FXML
    public Button searchClearButton;

    @FXML
    public TitledPane filterTitledPane;
    @FXML
    public TextField filterSearchField;

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
            tab.updateListedFiles();
            tab.updateTabDisplay((int) getInstance().tabPane.getWidth());
        }
    }

    /**
     * Updates selected(current) tab
     */
    public static void updateCurrentTab() {
        var tab = getInstance().getCurrentLogicalTab();
        tab.updateListedFiles();
        tab.updateTabDisplay((int) getInstance().tabPane.getWidth());
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

    //region back button
    @FXML
    public void onBackClicked() {
        var tab = getCurrentLogicalTab();
        tab.moveBack();
        tab.updateListedFiles();
        tab.updateTabDisplay((int) tabPane.getWidth());
    }
    //endregion

    //region show hidden files
    @FXML
    public void onShowHiddenClicked() {
        var tab = getCurrentLogicalTab();
        tab.getConfiguration().showHiddenFiles = showHiddenCheckbox.isSelected();
        tab.updateListedFiles();
        tab.updateTabDisplay((int) tabPane.getWidth());
    }
    //endregion

    //region sorting methods
    @FXML
    public void onSortFilesByName() {
        var tab = getCurrentLogicalTab();
        tab.getConfiguration().sortStrategy = new NameStrategy();
        tab.updateListedFiles();
        tab.updateTabDisplay((int) tabPane.getWidth());
    }

    @FXML
    public void onSortFilesBySize() {
        var tab = getCurrentLogicalTab();
        tab.getConfiguration().sortStrategy = new SizeStrategy();
        tab.updateListedFiles();
        tab.updateTabDisplay((int) tabPane.getWidth());
    }

    @FXML
    public void onSortFilesByLastModification() {
        var tab = getCurrentLogicalTab();
        tab.getConfiguration().sortStrategy = new LastModifiedStrategy();
        tab.updateListedFiles();
        tab.updateTabDisplay((int) tabPane.getWidth());
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

    //region show list/box
    @FXML
    public void onShowAsList() {
        getCurrentLogicalTab().setDisplayStrategy(new ListStrategy());
        updateCurrentTab();
    }

    @FXML
    public void onShowAsBoxes() {
        getCurrentLogicalTab().setDisplayStrategy(new BoxStrategy());
        updateCurrentTab();
    }
    //endregion

    //region filter
    @FXML
    public void onFilterUpdated() {
        getCurrentLogicalTab().getConfiguration().filter = filterSearchField.getText();
        updateCurrentTab();
    }
    //endregion

    @FXML
    public void onSearchConfirm() {
        String value = searchTextField.getText();
        if (value == null || value.matches("^\\s*$")) return;

        var tab = getCurrentLogicalTab();
        try {
            tab.executeCommand("search", new File(value));
        } catch (FileException ignored) {
            //todo
        }

        tab.updateTabDisplay((int) tabPane.getWidth());
        tab.setTitle("search for \"" + value + "\"");
    }

    @FXML
    public void onSearchClear() {
        searchTextField.clear();
        var tab = getCurrentLogicalTab();
        tab.updateListedFiles();
        tab.updateTabDisplay((int) tabPane.getWidth());
    }

    public void onSearchChoice() {
        var tab = getCurrentLogicalTab();

        tab.getConfiguration().searchStart = (searchChoiceBox.getValue().equals("start from here"))
                ? LogicalConfiguration.SearchStart.SEARCH_FROM_HERE
                : LogicalConfiguration.SearchStart.SEARCH_FROM_HOME;
    }
}
