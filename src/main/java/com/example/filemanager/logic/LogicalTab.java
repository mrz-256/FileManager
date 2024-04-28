package com.example.filemanager.logic;

import com.example.filemanager.logic.commands.*;
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
    private File directory;
    private final LogicalConfiguration configuration;
    private final PathHistory pathHistory;
    private ArrayList<File> listedFiles;

    private final Tab tab;
    private DisplayStrategy displayStrategy;
    private int zoom;
    private static final int DEFAULT_ICON_SIZE = 100;


    public LogicalTab(Tab tab, File directory, LinkedList<LogicalTab> parentList) {
        this.directory = directory;
        this.configuration = LogicalConfiguration.defaultConfiguration();
        this.listedFiles = new ArrayList<>();

        this.tab = tab;
        this.pathHistory = new PathHistory();
        this.displayStrategy = new BoxStrategy();
        this.zoom = 100;

        //pathHistory.add(directory);

        tab.setOnClosed( (x) -> {parentList.remove(this);});
    }

    //region moving directory
    /**
     * Sets current directory of this Tab.
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
        updateListedFiles();
    }

    /**
     * Moves to the last open directory from pathHistory
     */
    public void moveBack(){
        if (pathHistory.hasBack()){
            directory = pathHistory.getBack();
        }
    }
    //endregion

    //region file contents
    /**
     * Updates list of listed files.
     */
    public void updateListedFiles() {
        ListAllCommand command = new ListAllCommand();
        try {
            listedFiles = command.execute(directory, configuration, null);
        } catch (FileException ignored) {
            // ListAllCommand never throws exceptions
        }
    }

    /**
     * Gives list of files visible in this Tab by given configuration of context.
     * Already calls for updateListedFiles().
     * @return the files to list
     */
    public ArrayList<File> getListedFiles() {
        return listedFiles;
    }
    //endregion

    /**
     * Updates the ui contents of the javafx Tab. Maximal number of shown files is 512
     * because otherwise the loading is too slow.
     * @param width the width of tab pane
     */
    public void updateTabDisplay(int width) {
        tab.setText(directory.getAbsolutePath());

        if (listedFiles.size() > 512){
            listedFiles = (ArrayList<File>) listedFiles.subList(0, 512);
        }

        displayStrategy.display(
                tab, this,
                DEFAULT_ICON_SIZE * zoom / 100, width
        );
    }

    /**
     * Applies the filter to the currently listed files.
     */
    public void applyFilter(String filter)
    {
        listedFiles.removeIf(file -> !file.getName().matches(filter));
    }

    /**
     * Executes text representation of a command on parameters params within this context.
     * @param command command to execute.
     * @param params optional params to use
     * @throws FileException when command fails
     */
    public void executeCommand(String command, File... params) throws FileException {

        switch (command.toLowerCase()) {
            case "undo" -> CommandHistory.undoLast();
            case "new_file" -> new NewFileCommand().execute(directory, configuration, params);
            case "new_directory" -> new NewDirectoryCommand().execute(directory, configuration, params);
            case "delete_files" -> new DeleteFilesCommand().execute(directory, configuration, params);
            case "paste_files" -> new PasteFilesCommand().execute(directory, configuration, params);
            case "search" -> {
                var found = new SearchCommand().execute(directory, configuration, params);
                if (found != null && !found.isEmpty()){
                    listedFiles = found;
                } else {
                    listedFiles.clear();
                }
            }
            case "rename" -> new RenameFileCommand().execute(directory, configuration, params);
        }
    }

    //region getters
    /**
     * @return the configuration of logical tab
     */
    public LogicalConfiguration getConfiguration(){
        return configuration;
    }

    public File getDirectory(){
        return directory;
    }

    public Tab getTab() {
        return tab;
    }

    public String getTitle(){
        return tab.getText();
    }
    //endregion

    //region setters
    public void setDisplayStrategy(DisplayStrategy displayStrategy) {
        this.displayStrategy = displayStrategy;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    public void setTitle(String title){
        tab.setText(title);
    }
    //endregion
}
