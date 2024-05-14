package com.example.filemanager.logic;

import com.example.filemanager.logic.exceptions.DeleteFileException;
import com.example.filemanager.logic.exceptions.DuplicateFileException;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FUtilTest {

    @Test
    void inventUniqueName() {
        File A = new File("src/test/java/com/example/filemanager/logic/commands/test-file.txt");
        assertEquals("test-file copy.txt", FUtil.inventUniqueName(A).getName());
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
        assertEquals("0.0 B", FUtil.optimizeSizeFormat(0));
        assertEquals("500.0 B", FUtil.optimizeSizeFormat(500));
        assertEquals("2.0 KB", FUtil.optimizeSizeFormat(2048));
        assertEquals("1.17 MB", FUtil.optimizeSizeFormat(1234567));
    }

    @Test
    void deepCopy() {
        var source = new File("src/test/java/com/example/filemanager/logic/commands/test-file(1).txt");
        long size = source.length();

        var destination = new File("Nonexistent-file.txt");


        assertFalse(destination.exists());

        try {
            FUtil.deepCopy(source, destination);
        } catch (DuplicateFileException e) {
            fail(e.getMessage());
        }

        assertTrue(destination.exists());

        assertEquals(size, destination.length());

        try {
            FUtil.deepDelete(destination);
        } catch (DeleteFileException e) {
            fail(e.getMessage());
        }

        assertFalse(destination.exists());
    }
}