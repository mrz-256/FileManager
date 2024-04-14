package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.Logic;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class DeleteFileCommandTest {

    @Test
    void test(){
        File[] f = new File[]{new File("src/test/java/com/example/filemanager/logic/commands/test-file.txt")};
        Logic.setWorkingFile(f);

        FileCommand com = new DeleteFileCommand();

        assertTrue(f[0].exists());

        try {
            com.execute();
        } catch (com.example.filemanager.logic.exceptions.FileException e) {
            throw new RuntimeException(e);
        }

        assertFalse(f[0].exists());

        Logic.undoLastCommand();

        assertTrue(f[0].exists());
    }
}