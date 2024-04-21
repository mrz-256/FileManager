package com.example.filemanager.logic;

import com.example.filemanager.logic.commands.*;
import com.example.filemanager.logic.exceptions.FileException;
import com.example.filemanager.ui.display_strategy.DisplayStrategy;
import com.example.filemanager.ui.display_strategy.BoxStrategy;
import javafx.scene.control.Tab;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;


/**
 * A class representing a single tab withing file manager.
 * CommandHistory is a singleton common for all tabs.
 */
public class LogicalTab {
    private final Context context;
    private final ArrayList<File> filesToList;
    private final PathHistory pathHistory;

    private final Tab tab;
    private DisplayStrategy displayStrategy;
    private int zoom;
    private static final int DEFAULT_ICON_SIZE = 100;


    public LogicalTab(Tab tab, File directory, LinkedList<LogicalTab> parentList) {
        this.context = new Context(directory);
        this.filesToList = new ArrayList<>();
        this.tab = tab;
        this.pathHistory = new PathHistory();
        this.displayStrategy = new BoxStrategy();
        this.zoom = 100;

        pathHistory.add(directory);

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

        pathHistory.add(context.getDirectory());
        context.setDirectory(directory);
        updateListedFiles();
    }

    /**
     * Moves to the last open directory from pathHistory
     */
    public void moveBack(){
        if (pathHistory.hasBack()){
            context.setDirectory(pathHistory.getBack());
        }
    }
    //endregion

    //region file contents
    /**
     * Updates list of listed files.
     * @throws FileException when files can't be listed
     */
    public void updateListedFiles() throws FileException {
        FileCommand command = new ListAllCommand(context);
        command.execute();

        filesToList.clear();
        filesToList.addAll(context.getResult());
    }

    /**
     * Gives list of files visible in this Tab by given configuration of context.
     * Already calls for updateListedFiles().
     * @return the files to list
     */
    public ArrayList<File> getFilesToList() throws FileException {
        updateListedFiles();
        return filesToList;
    }
    //endregion

    public void updateTab(int width) {
        tab.setText(context.getDirectory().getAbsolutePath());
        displayStrategy.display(
                tab, this,
                DEFAULT_ICON_SIZE * zoom / 100, width
        );
    }

    /**
     * Executes text representation of a command on parameters params within this context.
     * @param command command to execute.
     * @param params optional params to use
     * @throws FileException when command can not be executed.
     */
    public void executeCommand(String command, File... params) throws FileException {
        context.clearWorking();
        context.addToWorking(params);

        switch (command.toLowerCase()) {
            case "undo" -> CommandHistory.undoLast();
            case "new_file" -> new NewFileCommand(context).execute();
            case "new_directory" -> new NewDirectoryCommand(context).execute();
            case "delete_files" -> new DeleteFilesCommand(context).execute();
            case "paste_files" -> new PasteFilesCommand(context).execute();
        }
    }

    //region getters
    /**
     * @return the context of logical tab
     */
    public Context getContext() {
        return context;
    }

    /**
     * @return the configuration of logical tab
     */
    public LogicalConfiguration getConfiguration(){
        return context.getConfiguration();
    }

    public File getDirectory(){
        return context.getDirectory();
    }
    //endregion

    //region setters
    public void setDisplayStrategy(DisplayStrategy displayStrategy) {
        this.displayStrategy = displayStrategy;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }
    //endregion
}
