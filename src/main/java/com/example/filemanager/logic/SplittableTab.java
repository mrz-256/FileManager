package com.example.filemanager.logic;

import java.io.File;
import java.util.ArrayList;

/**
 * A data structure to hold both splits of a split tab, in most cases only holds one tab.
 */
public class SplittableTab {
    /**
     * Main tab
     */
    private final Tab mainTab;
    /**
     * A duplicate of main tab, can be null.
     */
    private Tab secondaryTab;


    public SplittableTab(File directory) {
        mainTab = new Tab(directory);
        secondaryTab = null;
    }

    public void split(){
        secondaryTab = mainTab;
    }

    public void close(){
        secondaryTab = null;
    }

    public Tab getMainTab() {
        return mainTab;
    }

    public Tab getSecondaryTab() {
        return secondaryTab;
    }
}
