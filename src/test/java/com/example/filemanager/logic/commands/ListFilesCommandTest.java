package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.Context;
import com.example.filemanager.logic.Logic;
import com.example.filemanager.logic.exceptions.FileException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.text.JTextComponent;
import java.io.File;
import java.util.Arrays;

class ListFilesCommandTest {

    Context context;

    @BeforeEach
    void setUp() {
        context = Context.defaultContext();
    }

    @Test
    void execute() throws FileException {
        FileCommand command = new ListFilesCommand(context);
        command.execute();
        System.out.println("All files: " + context.getResult());

        context.getConfiguration().showHiddenFiles = false;
        command.execute();
        System.out.println("Excluding hidden files: " + context.getResult());


        context.getConfiguration().showHiddenFiles = true;
        context.getConfiguration().filter = "sh";
        command.execute();
        System.out.println("Filtered files: " + context.getResult());

    }
}