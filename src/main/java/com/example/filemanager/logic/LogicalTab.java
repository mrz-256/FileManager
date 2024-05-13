package com.example.filemanager.logic;

import com.example.filemanager.UIController;
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
    private final Tab tab;
    private File directory;
    private ArrayList<File> listedFiles;

    private final LogicalConfig config;
    private DisplayStrategy displayStrategy;
    private final PathHistory pathHistory;

    private boolean isInSearchMode;
    private File currentSearch;

    /**
     * Zoom [0;100] percent
     */
    private int zoom;
    private static final int DEFAULT_ICON_SIZE = 100;
    private static final int ZOOM_SPEED = 10;
    private static final int MIN_ZOOM = 60;
    private static final int MAX_ZOOM = 120;
    private static final int DEFAULT_ZOOM = 90;


    public LogicalTab(Tab tab, File directory, LinkedList<LogicalTab> parentList) {
        this.directory = directory;
        this.config = LogicalConfig.defaultConfiguration();
        this.listedFiles = new ArrayList<>();

        this.tab = tab;
        this.pathHistory = new PathHistory();
        this.displayStrategy = new BoxStrategy();
        this.zoom = DEFAULT_ZOOM;

        this.isInSearchMode = false;
        this.currentSearch = null;

        tab.setOnClosed((x) -> parentList.remove(this));
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

        pathHistory.add(this.directory);
        this.directory = directory;
        clearSearch();
        update();
    }

    /**
     * Updates list of listed files.
     */
    public void update() {
        try {
            if (isInSearchMode && currentSearch != null){
                executeCommand(FileCommandName.SEARCH, currentSearch);
            }
            else {
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
        if (isInSearchMode){
            tab.setText("search \"" + currentSearch.getName() + "\"");
        }
        else{
            tab.setText(directory.getAbsolutePath());
        }

        if (listedFiles.size() > config.maximalShownFiles) {
            listedFiles = (ArrayList<File>) listedFiles.subList(0, config.maximalShownFiles);
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
        clearSearch();
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
        if (isInSearchMode && !command.isUniversalSafe()){
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

            if (to_execute.getID() == FileCommandName.SEARCH){
                setSearch(params[0]);
            }

            updateTabDisplay();
        } else {
            if (isInSearchMode){
                executeCommand(FileCommandName.SEARCH, currentSearch);
            }
            else{
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
     * @param title the text title
     */
    public void setTitle(String title) {
        tab.setText(title);
    }

    /**
     * Return to normal directory mode
     */
    public void clearSearch(){
        isInSearchMode = false;
        currentSearch = null;
    }

    /**
     * Sets tab to search mode with provided search
     * @param search the file to search for now
     */
    private void setSearch(File search){
        isInSearchMode = true;
        currentSearch = search;
    }

    /**
     * @return if all commands can be executed in current state of the tab
     */
    public boolean isSafeForUniversalCommands(){
        return !isInSearchMode;
    }
    //endregion
}
