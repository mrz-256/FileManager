package com.example.filemanager.logic;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilFunctionsTest {

    @Test
    void inventUniqueName() {
        File A = new File("src/test/java/com/example/filemanager/logic/commands/test-file.txt");
        assertEquals("test-file(2).txt", FileUtilFunctions.inventUniqueName(A).getName());

        File B = new File("src/test/java/com/example/filemanager/logic/commands/.hidden-test-file");
        System.out.println(FileUtilFunctions.inventUniqueName(B));
    }
}