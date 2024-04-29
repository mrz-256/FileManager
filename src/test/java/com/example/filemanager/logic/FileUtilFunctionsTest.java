package com.example.filemanager.logic;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilFunctionsTest {

    @Test
    void inventUniqueName() {
        File A = new File("src/test/java/com/example/filemanager/logic/commands/test-file.txt");
        assertEquals("test-file(2).txt", FileUtilFunctions.inventUniqueName(A).getName());
    }

    @Test
    void getFileType() {
        assertEquals("txt", FileUtilFunctions.getFileType(new File("hello.txt")));
        assertEquals("png", FileUtilFunctions.getFileType(new File("hello.png")));
        assertEquals("hidden", FileUtilFunctions.getFileType(new File(".config")));
        assertEquals("save", FileUtilFunctions.getFileType(new File("mail.txt~")));
    }

    @Test
    void getOptimalSizeFormat() {
        assertEquals("0.0 B", FileUtilFunctions.getOptimalSizeFormat(0));
        assertEquals("500.0 B", FileUtilFunctions.getOptimalSizeFormat(500));
        assertEquals("2.0 KB", FileUtilFunctions.getOptimalSizeFormat(2048));
        assertEquals("1.17 MB", FileUtilFunctions.getOptimalSizeFormat(1234567));
    }
}