package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.LogicalConfig;
import com.example.filemanager.logic.commands.commands.NewFileCommand;
import com.example.filemanager.logic.exceptions.FileException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class NewFileCommandTest {

    @BeforeEach
    void setUp() {
        CommandHistory.initialize();
    }

    @Test
    void execute() throws FileException {
        File directory = new File("src/test/java/com/example/filemanager/logic/commands/");
        File tocreate = new File(directory, "file-test-file.txt");

        NewFileCommand command = new NewFileCommand();

        assertFalse(tocreate.exists());

        command.execute(
                new CommandContext(
                        directory,
                        null,
                        LogicalConfig.defaultConfiguration(),
                        new File[]{tocreate}
                )
        );

        assertTrue(tocreate.exists());

        command.undo();

        assertFalse(tocreate.exists());


    }
}