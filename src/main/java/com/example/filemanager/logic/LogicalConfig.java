package com.example.filemanager.logic;

import com.example.filemanager.logic.sort_strategy.NameStrategy;
import com.example.filemanager.logic.sort_strategy.SortStrategy;

import java.io.File;
import java.util.List;

/**
 * Stores the config of a context.
 * This influences how are files gathered.
 */
public class LogicalConfig {
    public SortStrategy sortStrategy;
    public boolean showHiddenFiles;
    public int maximalShownFiles;
    public SearchStart searchStart;
    public boolean sortSmallestFirst;

    /**
     * Determines where the Find command starts its search.
     */
    public enum SearchStart {
        SEARCH_FROM_HERE, SEARCH_FROM_HOME
    }


    /**
     * The basic constructor
     * @param sortStrategy the sort strategy
     * @param showHiddenFiles choice of showing hidden files
     * @param maximalShownFiles the maximal number of files to list in one directory
     * @param sortSmallestFirst ascending/descending order of listing
     * @param searchStart the starting position of Find command
     */
    public LogicalConfig(SortStrategy sortStrategy, boolean showHiddenFiles, int maximalShownFiles, boolean sortSmallestFirst, SearchStart searchStart) {
        this.sortStrategy = sortStrategy;
        this.showHiddenFiles = showHiddenFiles;
        this.maximalShownFiles = maximalShownFiles;
        this.sortSmallestFirst = sortSmallestFirst;
        this.searchStart = searchStart;
    }

    /**
     * Used to retrieve the default configuration.
     * @return the default configuration
     */
    public static LogicalConfig defaultConfiguration() {
        // magical number 512
        return new LogicalConfig(new NameStrategy(), false, 512, true, SearchStart.SEARCH_FROM_HERE);
    }

    /**
     * Applies this configuration to the provided list of files.
     * @param files the files for appliance
     */
    public void apply(List<File> files) {

        sortStrategy.sort(files);

        if (!sortSmallestFirst) {
            for (int i = 0; i < files.size() / 2; i++) {
                var tmp = files.get(i);
                files.set(i, files.get(files.size() - i - 1));
                files.set(files.size() - i - 1, tmp);
            }
        }

    }
}
