package com.example.filemanager.logic.sort_strategy;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Strategy to sort files by name.
 */
public class NameStrategy implements SortStrategy{
    /**
     * Sorts files by name.
     * @param files the files to sort.
     */
    @Override
    public void sort(File[] files) {
        Arrays.sort(files, Comparator.comparing(File::getName));
    }
}
