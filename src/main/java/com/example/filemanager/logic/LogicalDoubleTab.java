package com.example.filemanager.logic;

import com.example.filemanager.ui.display_strategy.DisplayStrategy;
import com.example.filemanager.ui.display_strategy.GridStrategy;
import javafx.scene.control.Tab;

import java.io.File;

/**
 * A data structure to hold both splits of a split tab, in most cases only holds one tab.
 */
public class LogicalDoubleTab {
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
    private DisplayStrategy displayStrategy;
    private int zoom;
    private static final int DEFAULT_ICON_SIZE = 64;


    public LogicalDoubleTab(Tab tab, File directory) {
        this.tab = tab;
        mainLogicalTab = new LogicalTab(directory);
        displayStrategy = new GridStrategy();
        secondaryLogicalTab = null;
        zoom = 100;
    }

    public void updateTab(int width){
        displayStrategy.display(
                tab, mainLogicalTab,
                DEFAULT_ICON_SIZE * zoom / 100, width
        );
    }


    public void split(){
        secondaryLogicalTab = mainLogicalTab;
    }

    public void close(){
        secondaryLogicalTab = null;
    }

    //region getters
    public LogicalTab getMainTab() {
        return mainLogicalTab;
    }

    public LogicalTab getSecondaryTab() {
        return secondaryLogicalTab;
    }

    public Tab getTab() {
        return tab;
    }

    public int getZoom() {
        return zoom;
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
