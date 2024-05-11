package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.LogicalConfiguration;
import com.example.filemanager.logic.commands.commands.DeleteFilesCommand;
import com.example.filemanager.logic.exceptions.FileException;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class DeleteFilesCommandTest {

    @Test
    void test() throws FileException {
        DeleteFilesCommand command = new DeleteFilesCommand();
        File directory = new File("src/test/java/com/example/filemanager/logic/commands/");
        File todelete = new File(directory, "test-file.txt");

        command.execute(
                new CommandContext(
                        directory,
                        null,
                        LogicalConfiguration.defaultConfiguration(),
                        new File[]{todelete}
                )
        );

        assertFalse(todelete.exists());

        CommandHistory.undoLast();

        assertTrue(todelete.exists());
    }
}