package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.Logic;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;

class ListFilesCommandTest {

    @Test
    void execute() {
        FileCommand com = new ListFilesCommand();
        Logic.setWorkingFile(new File[]{new File(Logic.getHome())});
        try {
            com.execute();
        } catch (com.example.filemanager.logic.exceptions.FileException e) {
            throw new RuntimeException(e);
        }
        File[] files = Logic.getCurrentResult();
        System.out.println("All files: "+Arrays.toString(files));

        Logic.showHidden(false);

        try {
            com.execute();
        } catch (com.example.filemanager.logic.exceptions.FileException e) {
            throw new RuntimeException(e);
        }
        files = Logic.getCurrentResult();
        System.out.println("Excluding hidden files: "+Arrays.toString(files));


        Logic.showHidden(true);
        Logic.setFilter("sh");

        try {
            com.execute();
        } catch (com.example.filemanager.logic.exceptions.FileException e) {
            throw new RuntimeException(e);
        }
        files = Logic.getCurrentResult();
        System.out.println("Filtered files: "+Arrays.toString(files));

    }
}