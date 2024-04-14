package com.example.filemanager.logic;

import com.example.filemanager.logic.sort_strategy.NameStrategy;
import com.example.filemanager.logic.sort_strategy.SortStrategy;

/**
 * Stores the configuration of a context
 */
public class Configuration {
    public SortStrategy sortStrategy;
    public boolean showHiddenFiles;
    public String filter;


    public Configuration(SortStrategy sortStrategy, boolean showHiddenFiles, String filter) {
        this.sortStrategy = sortStrategy;
        this.showHiddenFiles = showHiddenFiles;
        this.filter = filter;
    }

    public static Configuration defaultConfiguration(){
        return new Configuration(new NameStrategy(), true, "");
    }
}
