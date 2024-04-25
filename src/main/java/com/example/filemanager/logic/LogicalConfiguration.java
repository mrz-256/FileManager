package com.example.filemanager.logic;

import com.example.filemanager.logic.sort_strategy.NameStrategy;
import com.example.filemanager.logic.sort_strategy.SortStrategy;

/**
 * Stores the configuration of a context.
 * This influences how are files gathered.
 */
public class LogicalConfiguration {
    public SortStrategy sortStrategy;
    public boolean showHiddenFiles;
    public String filter;
    public SearchStart searchStart;
    public enum SearchStart {
        SEARCH_FROM_HERE, SEARCH_FROM_HOME
    }


    public LogicalConfiguration(SortStrategy sortStrategy, boolean showHiddenFiles, String filter, SearchStart searchStart) {
        this.sortStrategy = sortStrategy;
        this.showHiddenFiles = showHiddenFiles;
        this.filter = filter;
        this.searchStart = searchStart;
    }

    public static LogicalConfiguration defaultConfiguration(){
        return new LogicalConfiguration(new NameStrategy(), false, "", SearchStart.SEARCH_FROM_HERE);
    }
}
