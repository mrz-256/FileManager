package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.Logic;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ListDirectoriesCommandTest {

    @Test
    void execute() {
        new ListDirectoriesCommand().execute();
        File[] files = Logic.getInstance().getCurrentResult();
        System.out.println("All directories: "+ Arrays.toString(files));

        Logic.showHidden(false);

        new ListDirectoriesCommand().execute();
        files = Logic.getInstance().getCurrentResult();
        System.out.println("Excluding hidden directories: "+Arrays.toString(files));
    }
}