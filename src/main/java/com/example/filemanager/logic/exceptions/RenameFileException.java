package com.example.filemanager.logic.exceptions;

import java.io.File;

public class RenameFileException extends FileException{
    public RenameFileException(String message, File file) {
        super(message, file);
    }

    public RenameFileException(String message) {
        super(message);
    }
}
