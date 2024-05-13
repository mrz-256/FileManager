package com.example.filemanager.logic.exceptions;

import java.io.File;

public class DuplicateFileException extends FileException {
    public DuplicateFileException(String message, File file) {
        super(message, file);
    }

    public DuplicateFileException(String message) {
        super(message);
    }
}
