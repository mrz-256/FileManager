package com.example.filemanager.logic.sort_strategy;

import java.io.File;
import java.util.Comparator;
import java.util.List;

/**
 * Strategy to sort files by name.
 */
public class NameStrategy implements SortStrategy {
    /**
     * Sorts files by name.
     *
     * @param files the files to sort.
     */
    @Override
    public void sort(List<File> files) {
        files.sort(Comparator.comparing(File::getName));
    }
}
