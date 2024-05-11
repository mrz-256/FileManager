package com.example.filemanager.logic.exceptions;

import java.io.File;

public class PasteFilesException extends FileException {

    public PasteFilesException(String message, File file) {
        super(message, file);
    }

    public PasteFilesException(String message) {
        super(message);
    }
}
