package com.example.filemanager.logic.sort_strategy;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Strategy to sort files by the time of last modification.
 */
public class LastModifiedStrategy implements SortStrategy{

    /**
     * Sorts files by the time of last modification.
     * @param files the files to sort.
     */
    @Override
    public void sort(File[] files) {
        Arrays.sort(files, Comparator.comparingLong(File::lastModified));
    }
}
