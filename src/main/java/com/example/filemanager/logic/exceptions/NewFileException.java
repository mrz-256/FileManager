package com.example.filemanager.logic.exceptions;

import java.io.File;

public class NewFileException extends FileException {
    public NewFileException(String message, File file) {
        super(message, file);
    }

    public NewFileException(String message) {
        super(message);
    }
}
