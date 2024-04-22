package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.LogicalConfiguration;
import com.example.filemanager.logic.exceptions.FileException;

import java.io.File;
import java.util.ArrayList;

/**
 * A command class for logic commands.
 */
public abstract class FileCommand {

    /**
     * Method to execute command
     */
    public abstract ArrayList<File> execute(
            File directory, LogicalConfiguration configuration, File[] working
    ) throws FileException;

    /**
     * Method to undo given command
     */
    public void undo(){}

    /**
     * Method to get string name of command
     * @return the string name of command
     */
    public abstract String getID();
}
