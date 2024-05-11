package com.example.filemanager.logic;

import java.io.File;
import java.util.Stack;


/**
 * A data structure for holding tab path history
 */
public class PathHistory {
    private final Stack<File> back;

    public PathHistory() {
        back = new Stack<>();
    }

    /**
     * Add the current directory to history
     *
     * @param file the current directory
     */
    public void add(File file) {
        back.push(file);
    }

    /**
     * @return if there is history to go to
     */
    public boolean hasBack() {
        return !back.isEmpty();
    }

    /**
     * For safety check hasBack() before calling.
     *
     * @return the last visited file from history
     */
    public File getBack() {
        return back.pop();
    }
}
