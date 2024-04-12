package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.Logic;
import com.example.filemanager.logic.commands.ListFilesCommand;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ListFilesCommandTest {

    @Test
    void execute() {
        FileCommand com = new ListFilesCommand();
        com.execute();
        File[] files = Logic.getInstance().getCurrentResult();
        System.out.println("All files: "+Arrays.toString(files));

        Logic.showHidden(false);

        com.execute();
        files = Logic.getInstance().getCurrentResult();
        System.out.println("Excluding hidden files: "+Arrays.toString(files));


        Logic.showHidden(true);
        Logic.setFilter("sh");

        com.execute();
        files = Logic.getInstance().getCurrentResult();
        System.out.println("Filtered files: "+Arrays.toString(files));

    }
}