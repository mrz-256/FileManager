package com.example.filemanager.logic;

import java.io.File;

/**
 * A data structure to hold both splits of a split tab, in most cases only holds one tab.
 */
public class LogicalSplittableTab {
    /**
     * Main tab
     */
    private final LogicalTab mainLogicalTab;
    /**
     * A duplicate of main tab, can be null.
     */
    private LogicalTab secondaryLogicalTab;


    public LogicalSplittableTab(File directory) {
        mainLogicalTab = new LogicalTab(directory);
        secondaryLogicalTab = null;
    }

    public void split(){
        secondaryLogicalTab = mainLogicalTab;
    }

    public void close(){
        secondaryLogicalTab = null;
    }

    public LogicalTab getMainTab() {
        return mainLogicalTab;
    }

    public LogicalTab getSecondaryTab() {
        return secondaryLogicalTab;
    }
}
