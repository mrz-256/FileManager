package com.example.filemanager.logic.sort_strategy;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Strategy to sort files by the time of last modification.
 */
public class LastModifiedStrategy implements SortStrategy{

    /**
     * Sorts files by the time of last modification.
     * @param files the files to sort.
     */
    @Override
    public void sort(List<File> files) {
        files.sort(Comparator.comparingLong(File::lastModified));
    }
}
