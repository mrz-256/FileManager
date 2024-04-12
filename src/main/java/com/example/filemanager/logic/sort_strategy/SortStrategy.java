package com.example.filemanager.logic.sort_strategy;

import java.io.File;

/**
 * Strategy of sorting files
 */
public interface SortStrategy {

    /**
     * Sorts files in place.
     * @param files the files to sort.
     */
    void sort(File[] files);

}
