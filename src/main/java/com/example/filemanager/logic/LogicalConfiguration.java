package com.example.filemanager.logic;

import com.example.filemanager.logic.sort_strategy.NameStrategy;
import com.example.filemanager.logic.sort_strategy.SortStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores the configuration of a context.
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

    public static LogicalConfiguration defaultConfiguration(){
        return new LogicalConfiguration(new NameStrategy(), false, true, SearchStart.SEARCH_FROM_HERE);
    }

    public ArrayList<File> apply(File[] files){
        if (files == null || files.length == 0) return null;

        sortStrategy.sort(files);

        if (!sortSmallestFirst) {
            for (int i = 0; i < files.length / 2; i++) {
                var tmp = files[i];
                files[i] = files[files.length - i - 1];
                files[files.length - i - 1] = tmp;
            }
        }

        return new ArrayList<>(List.of(files));
    }
}
