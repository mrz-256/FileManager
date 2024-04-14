package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.Logic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class DeleteFileCommandTest {

    @Test
    void test(){
        File f = new File("src/test/java/com/example/filemanager/logic/commands/test-file.txt");
        Logic.setWorkingFile(f);

        FileCommand com = new DeleteFileCommand();

        assertTrue(f.exists());

        com.execute();

        assertFalse(f.exists());

        Logic.undoLastCommand();

        assertTrue(f.exists());
    }
}