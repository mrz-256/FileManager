package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.Logic;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;

class ListDirectoriesCommandTest {

    @Test
    void execute() {
        try {
            new ListDirectoriesCommand().execute();
        } catch (com.example.filemanager.logic.exceptions.FileException e) {
            throw new RuntimeException(e);
        }
        File[] files = Logic.getCurrentResult();
        System.out.println("All directories: "+ Arrays.toString(files));

        Logic.showHidden(false);

        try {
            new ListDirectoriesCommand().execute();
        } catch (com.example.filemanager.logic.exceptions.FileException e) {
            throw new RuntimeException(e);
        }
        files = Logic.getCurrentResult();
        System.out.println("Excluding hidden directories: "+Arrays.toString(files));
    }
}