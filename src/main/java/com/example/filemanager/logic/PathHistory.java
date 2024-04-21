package com.example.filemanager.logic;

import java.io.File;
import java.util.Stack;


/**
 * A data structure for holding tab path history
 */
public class PathHistory {
    private final Stack<File> back;
    private final Stack<File> forth;

    public PathHistory() {
        back = new Stack<>();
        forth = new Stack<>();
    }

    /**
     * Add the current directory to history
     * @param file the current directory
     */
    public void add(File file){
        back.push(file);
        forth.clear();
    }

    /**
     * @return if there is history to go to
     */
    public boolean hasBack(){
        return !back.isEmpty();
    }

    /**
     * For safety check hasBack() before calling.
     * @return the last visited file from history
     */
    public File getBack(){
        var file = back.pop();
        forth.push(file);
        return file;
    }

    /**
     * @return if it's possible to move forward
     */
    public boolean hasForth(){
        return !forth.isEmpty();
    }

    /**
     * For safety call hasForth() before calling.
     * @return the last file user backed from.
     */
    public File getForth(){
        var file = forth.pop();
        back.push(file);
        return file;
    }

    @Override
    public String toString() {
        return "PathHistory{" +
                "back=" + back +
                ", forth=" + forth +
                '}';
    }
}
