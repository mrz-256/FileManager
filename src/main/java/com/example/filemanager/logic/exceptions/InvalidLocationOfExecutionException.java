package com.example.filemanager.logic.exceptions;

import java.io.File;

public class InvalidLocationOfExecutionException extends FileException{

    public InvalidLocationOfExecutionException(String message, File file) {
        super(message, file);
    }

    public InvalidLocationOfExecutionException(String message) {
        super(message);
    }
}
