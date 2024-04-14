package com.example.filemanager.logic.exceptions;

import java.io.File;

public class FileException extends Exception{

    public FileException(String message, File file) {
        super(message + " | " + file.getAbsolutePath());
    }

    public FileException(String message) {
        super(message);
    }
}
