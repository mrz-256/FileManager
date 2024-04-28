package com.example.filemanager.logic.sort_strategy;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Strategy to sort files by byte size
 */
public class SizeStrategy implements SortStrategy {

    /**
     * Sorts files by byte size.
     * @param files the files to sort.
     */
    @Override
    public void sort(List<File> files) {
        files.sort(Comparator.comparingLong(File::length));
    }
}
