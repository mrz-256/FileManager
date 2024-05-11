package com.example.filemanager.logic;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FUtilTest {

    @Test
    void inventUniqueName() {
        File A = new File("src/test/java/com/example/filemanager/logic/commands/test-file.txt");
        assertEquals("test-file(2).txt", FUtil.inventUniqueName(A).getName());
    }

    @Test
    void getFileType() {
        assertEquals("txt", FUtil.getFileType(new File("hello.txt")));
        assertEquals("png", FUtil.getFileType(new File("hello.png")));
        assertEquals("hidden", FUtil.getFileType(new File(".config")));
        assertEquals("save", FUtil.getFileType(new File("mail.txt~")));
    }

    @Test
    void getOptimalSizeFormat() {
        assertEquals("0.0 B", FUtil.optimalizeSizeFormat(0));
        assertEquals("500.0 B", FUtil.optimalizeSizeFormat(500));
        assertEquals("2.0 KB", FUtil.optimalizeSizeFormat(2048));
        assertEquals("1.17 MB", FUtil.optimalizeSizeFormat(1234567));
    }
}