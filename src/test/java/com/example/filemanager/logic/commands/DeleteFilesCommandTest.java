package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.Context;
import com.example.filemanager.logic.exceptions.FileException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class DeleteFilesCommandTest {
    Context context;

    @BeforeEach
    void setUp() {
        context = new Context(new File("src/test/java/com/example/filemanager/logic/commands/"));
    }

    @Test
    void test() throws FileException {
        context.addToWorking(new File(context.getDirectory(),"test-file.txt"));
        FileCommand command = new DeleteFilesCommand(context);

        assertTrue(context.getWorkingAt(0).exists());

        command.execute();

        assertFalse(context.getWorkingAt(0).exists());

        CommandHistory.undoLast();

        assertTrue(context.getWorkingAt(0).exists());
    }
}