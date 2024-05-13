package com.example.filemanager;

import com.example.filemanager.logic.FUtil;
import com.example.filemanager.logic.LogicalConfig;
import com.example.filemanager.logic.LogicalTab;
import com.example.filemanager.logic.commands.CommandHistory;
import com.example.filemanager.logic.commands.FileCommandName;
import com.example.filemanager.logic.exceptions.FileException;
import com.example.filemanager.logic.sort_strategy.LastModifiedStrategy;
import com.example.filemanager.logic.sort_strategy.NameStrategy;
import com.example.filemanager.logic.sort_strategy.SizeStrategy;
import com.example.filemanager.ui_logic.display_strategy.BoxStrategy;
import com.example.filemanager.ui_logic.display_strategy.ListStrategy;
import com.example.filemanager.ui_logic.newdirectory.NewDirectoryDialogueCreator;
import com.example.filemanager.ui_logic.newfile.NewFileDialogueCreator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.LinkedList;

/**
 * A class that holds all tabs and helper static methods for work with App
 */
public class UIController {
    @FXML
    public Button newTabButton;
    @FXML
    public ToolBar filepathView;
    @FXML
    private CheckMenuItem showHiddenCheckbox;
    @FXML
    private CheckMenuItem sortSmallestFirst;

    @FXML
    private TabPane tabPane;
    @FXML
    private VBox places;

    @FXML
    public TextField findTextField;
    @FXML
    public ChoiceBox<String> findChoiceBox;
    @FXML
    public Button findConfirmButton;
    @FXML
    public Button findClearButton;

    @FXML
    public TitledPane filterTitledPane;
    @FXML
    public TextField filterTextField;

    /**
     * Singleton instance
     */
    private static UIController instance;

    /**
     * List of all Logical tabs in app
     */
    private static LinkedList<LogicalTab> tabs;
    private static final int MAX_TABS = 10;

    public static UIController getInstance() {
        return instance;
    }

    @FXML
    void initialize() {
        instance = this;

        CommandHistory.initialize();

        tabs = new LinkedList<>();
        UIUtil.createNewTab(tabPane, tabs, FUtil.getHomeDirectory());

        tabPane.widthProperty().addListener((observableValue, number, newNumber) -> UIController.updateCurrentTab());

        sortSmallestFirst.setSelected(true);

        UIUtil.fillPlacesList(places);

        updateCurrentTab();
    }

    public static void onClose() {
        CommandHistory.flushToFile();
    }


    /**
     * Updates content and display of current tab
     */
    public static void updateCurrentTab() {
        var tab = getInstance().getCurrentLogicalTab();
        tab.update();
    }

    public static int getTabPaneWidth() {
        return (int) getInstance().tabPane.getWidth();
    }

    /**
     * Sets the directory of current tab
     *
     * @param directory the directory to use
     * @throws FileException when the directory is invalid or can't be opened
     */
    public static void setDirectoryInCurrentTab(File directory) throws FileException {
        var tab = getInstance().getCurrentLogicalTab();
        tab.setDirectory(directory);
    }

    /**
     * @return the currently selected logical tab
     */
    public LogicalTab getCurrentLogicalTab() {
        var index = tabPane.getSelectionModel().getSelectedIndex();
        return tabs.get(index);
    }

    public static ToolBar getFilepathViewPane(){
        return getInstance().filepathView;
    }

    //region back button
    @FXML
    public void onBackClicked() {
        var tab = getCurrentLogicalTab();
        tab.moveBack();
    }
    //endregion

    //region show hidden files
    @FXML
    public void onShowHiddenClicked() {
        var tab = getCurrentLogicalTab();
        tab.getConfig().showHiddenFiles = showHiddenCheckbox.isSelected();
        tab.update();
    }
    //endregion

    //region sorting methods
    @FXML
    public void onSortFilesByName() {
        var tab = getCurrentLogicalTab();
        tab.getConfig().sortStrategy = new NameStrategy();
        tab.update();
    }

    @FXML
    public void onSortFilesBySize() {
        var tab = getCurrentLogicalTab();
        tab.getConfig().sortStrategy = new SizeStrategy();
        tab.update();
    }

    @FXML
    public void onSortFilesByLastModification() {
        var tab = getCurrentLogicalTab();
        tab.getConfig().sortStrategy = new LastModifiedStrategy();
        tab.update();
    }

    @FXML
    public void onSmallestFirstClicked() {
        var tab = getCurrentLogicalTab();
        tab.getConfig().sortSmallestFirst = sortSmallestFirst.isSelected();
        tab.update();
    }
    //endregion

    //region new tab
    @FXML
    public void onNewTabClicked() {
        if (tabs.size() >= MAX_TABS) {
            return;
        }

        UIUtil.createNewTab(tabPane, tabs, getCurrentLogicalTab().getDirectory());
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);

        if (tabs.size() == MAX_TABS) {
            newTabButton.setDisable(true);
        }

        updateCurrentTab();
    }

    public void onTabPaneUpdate() {
        if (tabPane.getTabs().size() == 1) {
            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        }
        if (tabs.size() < MAX_TABS) {
            newTabButton.setDisable(false);
        }

        // update settings to match selected tab and flush input fields
        var config = getCurrentLogicalTab().getConfig();
        showHiddenCheckbox.setSelected(config.showHiddenFiles);
        sortSmallestFirst.setSelected(config.sortSmallestFirst);

        findTextField.clear();
        filterTextField.clear();
        updateCurrentTab();
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
        var tab = getCurrentLogicalTab();
        var title = tab.getTitle();

        tab.applyFilter(filterTextField.getText());
        filterTextField.setText("");

        tab.updateTabDisplay();
        tab.setTitle("filtered " + title);
    }
    //endregion

    //region find
    @FXML
    public void onFindConfirm() {
        String value = findTextField.getText();
        if (value == null || value.matches("^\\s*$")) {
            return;
        }

        var tab = getCurrentLogicalTab();
        try {
            tab.executeCommand(FileCommandName.FIND, new File(value));
        } catch (Exception e) {
            var alert = UIUtil.createAlert(Alert.AlertType.ERROR, "Failed find.", e.getMessage());
            alert.show();
        } // doesn't matter - nothing happens
    }

    @FXML
    public void onClear() {
        findTextField.clear();
        filterTextField.clear();
        var tab = getCurrentLogicalTab();
        tab.clearFindMode();
        tab.update();
    }

    public void onFindChoice() {
        var tab = getCurrentLogicalTab();

        tab.getConfig().searchStart = (findChoiceBox.getValue().equals("start from here"))
                ? LogicalConfig.SearchStart.SEARCH_FROM_HERE
                : LogicalConfig.SearchStart.SEARCH_FROM_HOME;
    }

    //endregion

    //region paste
    @FXML
    public void onPasteFilesClick() {
        var tab = getCurrentLogicalTab();
        var files = FUtil.getFilesFromClipboard();
        try {
            tab.executeCommand(FileCommandName.PASTE, files);
        } catch (Exception e) {
            var alert = UIUtil.createAlert(Alert.AlertType.ERROR, "Failed pasting files.", e.getMessage());
            alert.show();
        }
        tab.update();
    }
    //endregion

    //region undo
    @FXML
    public void onUndoClicked() {
        var tab = getCurrentLogicalTab();
        try {
            tab.executeCommand(FileCommandName.UNDO);
        } catch (FileException e) {
            var alert = UIUtil.createAlert(Alert.AlertType.ERROR, "Failed undoing last action", e.getMessage());
            alert.show();
        }
        tab.update();
    }
    //endregion

    //region new
    public void onNewFileClicked() {
        var tab = getCurrentLogicalTab();
        var dialog = NewFileDialogueCreator.createNewFileDialog(tab);
        dialog.show();
    }

    public void onNewDirectoryClicked() {
        var tab = getCurrentLogicalTab();
        var dialog = NewDirectoryDialogueCreator.createNewDialog(tab);
        dialog.show();
    }
    //endregion

    //region shortcuts
    public void onKeyTyped(KeyEvent keyEvent) {
        var tab = getCurrentLogicalTab();

        if (keyEvent.isControlDown()) {
            switch (keyEvent.getCode()) {
                case PLUS, ADD -> tab.zoom(true);
                case MINUS, SUBTRACT -> tab.zoom(false);
                case Z -> onUndoClicked();
                case V -> onPasteFilesClick();
                case F -> onNewFileClicked();
                case D -> onNewDirectoryClicked();
                case T -> onNewTabClicked();
            }

        }

        tab.updateTabDisplay();
    }
    //endregion

    public void onGotoClicked(ActionEvent actionEvent) {

    }


}
