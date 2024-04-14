package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.Context;
import com.example.filemanager.logic.exceptions.FileException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class NewFileCommandTest {
    Context context;

    @BeforeEach
    void setUp() {
        context = new Context(new File("src/test/java/com/example/filemanager/logic/commands/"));
    }

    @Test
    void execute() throws FileException {
        context.addToWorking(new File(context.getDirectory(), "file-test-file.txt"));
        FileCommand command = new NewFileCommand(context);

        assertFalse(context.getWorkingAt(0).exists());

        command.execute();

        assertTrue(context.getWorkingAt(0).exists());

        command.undo();

        assertFalse(context.getWorkingAt(0).exists());


    }
}