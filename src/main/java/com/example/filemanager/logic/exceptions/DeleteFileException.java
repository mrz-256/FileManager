package com.example.filemanager.logic.exceptions;

import java.io.File;

public class DeleteFileException extends FileException {

    public DeleteFileException(String string, File file) {
        super("Failed deleting file", file);
    }

    public DeleteFileException(String message) {
        super(message);
    }
}
