package com.example.filemanager.logic.sort_strategy;

import java.io.File;
import java.util.List;

/**
 * Strategy of sorting files
 */
public interface SortStrategy {

    /**
     * Sorts files in place.
     *
     * @param files the files to sort.
     */
    void sort(List<File> files);

}
