package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.Context;
import com.example.filemanager.logic.Logic;
import com.example.filemanager.logic.exceptions.FileException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;

class ListDirectoriesCommandTest {
    Context context;

    @BeforeEach
    void setUp() {
        context = Context.defaultContext();
    }

    @Test
    void execute() throws FileException {
        FileCommand command = new ListDirectoriesCommand(context);
        command.execute();
        System.out.println("All directories: " + context.getResult());

        context.getConfiguration().showHiddenFiles = false;
        command.execute();
        System.out.println("Excluding hidden directories: " + context.getResult());
    }
}