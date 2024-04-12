package com.example.filemanager.logic;

import com.example.filemanager.logic.sort_strategy.NameStrategy;
import com.example.filemanager.logic.sort_strategy.SortStrategy;

import java.io.File;


public class Logic {
    private static Logic instance;

    /** The current open directory */
    private File directory;
    /** The way to sort returned files and directories */
    private SortStrategy sortStrategy;
    /** Flag of showing hidden files or not */
    private boolean showHidden;
    /** Returned files must always contain this filter (matched as ".*{filter}.*") */
    private String filter;

    /** The files gathered after the last listing operation, already sorted. */
    private File[] currentResult;
    /** The file to perform an action on. Like delete, create etc. */
    private File workingFile;


    //region constructors
    private Logic() {
        directory = new File(getHome());
        sortStrategy = new NameStrategy();
        showHidden = true;
        currentResult = null;
        filter = "";
        workingFile = null;
    }

    public static Logic getInstance(){
        if (instance == null){
            instance = new Logic();
        }

        return instance;
    }
    //endregion

    //region setters
    public static void setSortStrategy(SortStrategy sortStrategy) {
        instance.sortStrategy = sortStrategy;
    }

    public static void showHidden(boolean show){
        instance.showHidden = show;
    }

    public static void setDirectory(File directory)
    {
        instance.directory = directory;
    }

    public static void setCurrentResult(File[] files){
        instance.currentResult = files;
    }

    public static void setFilter(String filter){
        instance.filter = filter;
    }

    public static void setWorkingFile(File file){
        instance.workingFile = file;
    }
    //endregion


    //region getters

    /**
     * Returns string path to home directory on given machine
     * @return the path
     */
    public static String getHome(){
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

    public File[] getCurrentResult(){
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
