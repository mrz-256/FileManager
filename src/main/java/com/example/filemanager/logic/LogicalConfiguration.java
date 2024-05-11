package com.example.filemanager.logic;

import com.example.filemanager.logic.sort_strategy.NameStrategy;
import com.example.filemanager.logic.sort_strategy.SortStrategy;

import java.io.File;
import java.util.List;

/**
 * Stores the config of a context.
 * This influences how are files gathered.
 */
public class LogicalConfiguration {
    public SortStrategy sortStrategy;
    public boolean showHiddenFiles;
    public SearchStart searchStart;
    public boolean sortSmallestFirst;

    public enum SearchStart {
        SEARCH_FROM_HERE, SEARCH_FROM_HOME
    }


    public LogicalConfiguration(SortStrategy sortStrategy, boolean showHiddenFiles, boolean sortSmallestFirst, SearchStart searchStart) {
        this.sortStrategy = sortStrategy;
        this.showHiddenFiles = showHiddenFiles;
        this.sortSmallestFirst = sortSmallestFirst;
        this.searchStart = searchStart;
    }

    public static LogicalConfiguration defaultConfiguration() {
        return new LogicalConfiguration(new NameStrategy(), false, true, SearchStart.SEARCH_FROM_HERE);
    }

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
