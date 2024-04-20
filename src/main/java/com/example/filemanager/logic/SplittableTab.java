package com.example.filemanager.logic;

import com.example.filemanager.UIConfiguration;
import javafx.scene.control.Tab;

import java.io.File;

/**
 * A data structure to hold both splits of a split tab, in most cases only holds one tab.
 */
public class SplittableTab {
    /**
     * Main tab
     */
    private final LogicalTab mainLogicalTab;
    /**
     * A duplicate of main tab, can be null.
     */
    private LogicalTab secondaryLogicalTab;
    /**
     * The javaFX tab linked to LogicalTabs of this class.
     */
    private Tab tab;

    private UIConfiguration configuration;

    public SplittableTab(Tab tab, File directory) {
        this.tab = tab;
        mainLogicalTab = new LogicalTab(directory);
        secondaryLogicalTab = null;


    }

    public void updateTab(){

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
