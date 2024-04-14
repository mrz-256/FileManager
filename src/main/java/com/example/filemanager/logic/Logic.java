package com.example.filemanager.logic;

import com.example.filemanager.logic.commands.DeleteFileCommand;
import com.example.filemanager.logic.commands.FileCommand;
import com.example.filemanager.logic.sort_strategy.NameStrategy;
import com.example.filemanager.logic.sort_strategy.SortStrategy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Stack;


public class Logic {
    private static Logic instance;

    /**
     * The current open directory
     */
    private File directory;
    /**
     * The way to sort returned files and directories
     */
    private SortStrategy sortStrategy;
    /**
     * Flag of showing hidden files or not
     */
    private boolean showHidden;
    /**
     * Returned files must always contain this filter (matched as ".*{filter}.*")
     */
    private String filter;

    /**
     * The files gathered after the last listing operation, already sorted.
     */
    private File[] currentResult;
    /**
     * The file to perform an action on. Like delete, create etc.
     */
    private File workingFile;
    private Stack<FileCommand> executedCommands;


    //region constructors
    private Logic() {
        directory = new File(getHome());
        sortStrategy = new NameStrategy();
        showHidden = true;
        currentResult = null;
        filter = "";
        workingFile = null;
        executedCommands = new Stack<>();
    }

    public static Logic getInstance() {
        if (instance == null) {
            instance = new Logic();
        }

        return instance;
    }
    //endregion

    //region setters
    public static void setSortStrategy(SortStrategy sortStrategy) {
        getInstance().sortStrategy = sortStrategy;
    }

    public static void showHidden(boolean show) {
        getInstance().showHidden = show;
    }

    public static void setDirectory(File directory) {
        getInstance().directory = directory;
    }

    public static void setCurrentResult(File[] files) {
        getInstance().currentResult = files;
    }

    public static void setFilter(String filter) {
        getInstance().filter = filter;
    }

    public static void setWorkingFile(File file) {
        getInstance().workingFile = file;
    }
    //endregion

    /**
     * A helper method used to copy file. Should only be used through commands.
     *
     * @param source      source of the file
     * @param destination the name to copy into
     * @return success
     */
    public static boolean copyFile(File source, File destination) {
        try {
            Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Undoes last executed command from executedCommands stack.
     */
    public static void undoLastCommand(){
        if (getInstance().executedCommands.empty()) return;

        getInstance().executedCommands.pop().undo();
    }

    public static void pushToExecutedCommands(FileCommand command){
        getInstance().executedCommands.push(command);
    }


    //region getters

    /**
     * Returns string path to home directory on given machine
     *
     * @return the path
     */
    public static String getHome() {
        return System.getProperty("user.home");
    }

    public File getDirectory() {
        return directory;
    }

    public SortStrategy getSortStrategy() {
        return sortStrategy;
    }

    public boolean isShowHidden() {
        return showHidden;
    }

    public File[] getCurrentResult() {
        return currentResult;
    }

    public String getFilter() {
        return filter;
    }

    public File getWorkingFile() {
        return workingFile;
    }

    //endregion


}
