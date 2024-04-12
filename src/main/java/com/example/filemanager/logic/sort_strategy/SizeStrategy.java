package com.example.filemanager.logic.sort_strategy;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Strategy to sort files by byte size
 */
public class SizeStrategy implements SortStrategy {

    /**
     * Sorts files by byte size.
     * @param files the files to sort.
     */
    @Override
    public void sort(File[] files) {
        Arrays.sort(files, Comparator.comparingLong(File::length));
    }
}
