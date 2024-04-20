package com.example.filemanager.logic;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Stores the context and configuration to use for commands
 */
public class Context {
    private final LogicalConfiguration logicalConfiguration;
    private File directory;
    private final ArrayList<File> pathHistory;
    private ArrayList<File> working;
    private ArrayList<File> result;


    public Context(File directory) {
        logicalConfiguration = LogicalConfiguration.defaultConfiguration();

        this.directory = directory;
        working = new ArrayList<>();
        result = new ArrayList<>();
        pathHistory = new ArrayList<>();
    }

    public static Context defaultContext(){
        return new Context(FileUtilFunctions.getHomeDirectory());
    }

    //region adding to params
    public void addToWorking(File file){
        working.add(file);
    }

    public void addToWorking(File[] files){
        working.addAll(Arrays.asList(files));
    }

    public void addToResult(File file){
        result.add(file);
    }

    public void addToResult(File[] files){
        result.addAll(Arrays.asList(files));
    }

    public void clearWorking(){
        working.clear();
    }

    public void clearResult(){
        result.clear();
    }
    //endregion

    //region getters and setters
    public int getWorkingSize(){
        return working.size();
    }
    public int getResultSize(){
        return result.size();
    }
    public File getWorkingAt(int index){
        return working.get(index);
    }
    public File getResultAt(int index){
        return result.get(index);
    }

    public LogicalConfiguration getConfiguration() {
        return logicalConfiguration;
    }

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        pathHistory.add(0, this.directory);
        if (pathHistory.size() > 10) pathHistory.remove(10);
        this.directory = directory;
    }

    public ArrayList<File> getWorking() {
        return working;
    }

    public void setWorking(ArrayList<File> working) {
        this.working = working;
    }

    public ArrayList<File> getResult() {
        return result;
    }

    public void setResult(ArrayList<File> result) {
        this.result = result;
    }

    //endregion
}
