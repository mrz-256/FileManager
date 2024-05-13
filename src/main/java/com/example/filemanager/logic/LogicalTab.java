package com.example.filemanager.logic;

import com.example.filemanager.UIController;
import com.example.filemanager.UIUtil;
import com.example.filemanager.logic.commands.CommandContext;
import com.example.filemanager.logic.commands.CommandHistory;
import com.example.filemanager.logic.commands.FileCommandName;
import com.example.filemanager.logic.commands.commands.FileCommand;
import com.example.filemanager.logic.exceptions.FileException;
import com.example.filemanager.logic.exceptions.InvalidLocationOfExecutionException;
import com.example.filemanager.ui_logic.display_strategy.BoxStrategy;
import com.example.filemanager.ui_logic.display_strategy.DisplayStrategy;
import javafx.scene.control.Tab;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;


/**
 * A class representing a single tab withing file manager.
 * CommandHistory is a singleton common for all tabs.
 */
public class LogicalTab {
    /**
     * The javafx tab containing files of this logical instance
     */
    private final Tab tab;
    /**
     * The open directory of this tab
     */
    private File directory;
    /**
     * The list of the files listed in this tab
     */
    private ArrayList<File> listedFiles;

    /**
     * Configuration influencing the results of commands performed in this tab
     */
    private final LogicalConfig config;
    /**
     * The display strategy to use in this tab
     */
    private DisplayStrategy displayStrategy;
    /**
     * One-way history of previously visited directories
     */
    private final PathHistory pathHistory;

    /**
     * If tab should display find results rather than the open directory; some functionality can
     * only be performed inside directory(such as paste files etc.)
     */
    private boolean isInFindMode;
    /**
     * The current match for find functionality
     */
    private File currentFileToFind;

    /**
     * Zoom [0;100] %
     */
    private int zoom;
    /**
     * The default size of an icon; always multiplied by `zoom`
     */
    private static final int DEFAULT_ICON_SIZE = 100;
    /**
     * The zoom change
     */
    private static final int ZOOM_SPEED = 10;
    /**
     * The minimal zoom
     */
    private static final int MIN_ZOOM = 60;
    /**
     * The maximal zoom
     */
    private static final int MAX_ZOOM = 120;
    /**
     * The default zoom (90%)
     */
    private static final int DEFAULT_ZOOM = 90;


    public LogicalTab(Tab tab, File directory, LinkedList<LogicalTab> parentList) {
        this.directory = directory;
        this.config = LogicalConfig.defaultConfiguration();
        this.listedFiles = new ArrayList<>();

        this.tab = tab;
        this.pathHistory = new PathHistory();
        this.displayStrategy = new BoxStrategy();
        this.zoom = DEFAULT_ZOOM;

        this.isInFindMode = false;
        this.currentFileToFind = null;

        tab.setOnClosed((x) -> parentList.remove(this));

        // updates the filepath view display
        UIUtil.filepathViewFillPath(directory);
    }


    /**
     * Sets current directory of this Tab.
     *
     * @param directory the directory to use
     * @throws FileException when directory is invalid or can't be opened
     */
    public void setDirectory(File directory) throws FileException {
        if (!directory.exists()) {
            throw new FileException("Directory does not exist.", directory);
        }
        if (!directory.isDirectory()) {
            throw new FileException("File is not a directory.", directory);
        }
        if (!directory.canRead()){
            throw new FileException("Can't access given directory.", directory);
        }

        pathHistory.add(this.directory);
        this.directory = directory;

        // updates the filepath view display
        UIUtil.filepathViewFillPath(directory);

        clearFindMode();
        update();
    }

    /**
     * Updates list of listed files.
     */
    public void update() {
        try {
            if (isInFindMode && currentFileToFind != null) {
                executeCommand(FileCommandName.FIND, currentFileToFind);
            } else {
                executeCommand(FileCommandName.LIST_ALL);
            }
        } catch (FileException ignored) {
            // ListAllCommand never throws exceptions
        }
    }

    /**
     * Updates the ui contents of the javafx Tab. Maximal number of shown files is 512
     * because otherwise the loading is too slow.
     */
    public void updateTabDisplay() {
        if (isInFindMode) {
            tab.setText("search \"" + currentFileToFind.getName() + "\"");
        } else {
            tab.setText(directory.getAbsolutePath());
        }

        displayStrategy.display(
                tab, this,
                (DEFAULT_ICON_SIZE * zoom) / 100, UIController.getTabPaneWidth()
        );
    }

    /**
     * Moves to the last open directory from pathHistory. Updates the files.
     */
    public void moveBack() {
        if (pathHistory.hasBack()) {
            directory = pathHistory.getBack();
        }

        // updates the filepath view display
        UIUtil.filepathViewFillPath(directory);

        clearFindMode();
        update();
    }

    /**
     * Applies the filter to the currently listed files.
     */
    public void applyFilter(String filter) {
        listedFiles.removeIf(file -> !file.getName().matches(filter));
    }

    /**
     * Executes a command on parameters `params` within this context.
     * Automatically updates tab display.
     *
     * @param command command to execute.
     * @param params  optional params to use
     * @throws FileException when invalid command is passed
     */
    public void executeCommand(FileCommandName command, File... params) throws FileException {
        if (isInFindMode && !command.isUniversalSafe()) {
            throw new InvalidLocationOfExecutionException("Can't perform operation " + command + " outside an directory.");
        }
        if (command == FileCommandName.UNDO) {
            CommandHistory.undoLast();
            return;
        }

        var to_execute = FileCommand.getByType(command);
        var context = new CommandContext(directory, this, config, params);

        var files = to_execute.execute(context);

        if (files != null) {
            listedFiles = files;

            if (to_execute.getID() == FileCommandName.FIND) {
                setFindMode(params[0]);
            }

            updateTabDisplay();
        } else {
            if (isInFindMode) {
                executeCommand(FileCommandName.FIND, currentFileToFind);
            } else {
                executeCommand(FileCommandName.LIST_ALL);
            }
        }
    }

    public void zoom(boolean in) {
        int change = ZOOM_SPEED * ((in) ? 1 : -1);
        zoom = Math.max(MIN_ZOOM, Math.min(MAX_ZOOM, zoom + change));
    }

    //region getters
    public LogicalConfig getConfig() {
        return config;
    }

    public File getDirectory() {
        return directory;
    }

    public Tab getTab() {
        return tab;
    }

    public String getTitle() {
        return tab.getText();
    }

    public ArrayList<File> getListedFiles() {
        return listedFiles;
    }
    //endregion

    //region setters
    public void setDisplayStrategy(DisplayStrategy displayStrategy) {
        this.displayStrategy = displayStrategy;
    }

    /**
     * Sets the tile of the javafx tab
     *
     * @param title the text title
     */
    public void setTitle(String title) {
        tab.setText(title);
    }

    /**
     * Return to normal directory mode
     */
    public void clearFindMode() {
        isInFindMode = false;
        currentFileToFind = null;
    }

    /**
     * Sets tab to toFind mode with provided toFind
     *
     * @param toFind the file to toFind for now
     */
    private void setFindMode(File toFind) {
        isInFindMode = true;
        currentFileToFind = toFind;
    }

    /**
     * @return if all commands can be executed in current state of the tab
     */
    public boolean isSafeForUniversalCommands() {
        return !isInFindMode;
    }
    //endregion
}
