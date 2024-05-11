package com.example.filemanager.logic;

import com.example.filemanager.UIController;
import com.example.filemanager.logic.commands.*;
import com.example.filemanager.logic.commands.commands.*;
import com.example.filemanager.logic.exceptions.FileException;
import com.example.filemanager.ui_logic.display_strategy.DisplayStrategy;
import com.example.filemanager.ui_logic.display_strategy.BoxStrategy;
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

    private final LogicalConfiguration configuration;
    private DisplayStrategy displayStrategy;
    private final PathHistory pathHistory;

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
        this.configuration = LogicalConfiguration.defaultConfiguration();
        this.listedFiles = new ArrayList<>();

        this.tab = tab;
        this.pathHistory = new PathHistory();
        this.displayStrategy = new BoxStrategy();
        this.zoom = DEFAULT_ZOOM;

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
        update();
    }

    /**
     * Updates list of listed files.
     */
    public void update() {
        try {
            executeCommand("list_all");
        } catch (FileException ignored) {
            // ListAllCommand never throws exceptions
        }
    }

    /**
     * Updates the ui contents of the javafx Tab. Maximal number of shown files is 512
     * because otherwise the loading is too slow.
     */
    public void updateTabDisplay() {
        tab.setText(directory.getAbsolutePath());

        if (listedFiles.size() > 512) {
            listedFiles = (ArrayList<File>) listedFiles.subList(0, 512);
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
        update();
    }

    /**
     * Applies the filter to the currently listed files.
     */
    public void applyFilter(String filter) {
        listedFiles.removeIf(file -> !file.getName().matches(filter));
    }

    /**
     * Executes text representation of a command on parameters `params` within this context.
     * Automatically updates tab display.
     *
     * @param command command to execute.
     * @param params  optional params to use
     * @throws FileException when invalid command is passed
     */
    public synchronized void executeCommand(String command, File... params) throws FileException {
        if (command.equals("undo")) {
            CommandHistory.undoLast();
            return;
        }

        FileCommand to_execute = switch (command.toLowerCase()) {
            case "list_all" -> new ListAllCommand();
            case "paste_files" -> new PasteFilesCommand();
            case "new_file" -> new NewFileCommand();
            case "open" -> new OpenFileCommand();
            case "new_directory" -> new NewDirectoryCommand();
            case "search" -> new SearchCommand();
            case "delete_files" -> new DeleteFilesCommand();
            case "rename" -> new RenameFileCommand();
            default -> null;
        };

        if (to_execute == null) {
            throw new FileException("Invalid command (" + command + ")");
        }

        var context = new CommandContext(directory, this, configuration, params);

        var files = to_execute.execute(context);
        if (files != null) {
            listedFiles = files;
            updateTabDisplay();
        } else {
            executeCommand("list_all");
        }
    }

    public void zoom(boolean in) {
        int change = ZOOM_SPEED * ((in) ? 1 : -1);
        zoom = Math.max(MIN_ZOOM, Math.min(MAX_ZOOM, zoom + change));
    }

    //region getters
    public LogicalConfiguration getConfiguration() {
        return configuration;
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

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    public void setTitle(String title) {
        tab.setText(title);
    }
    //endregion
}
