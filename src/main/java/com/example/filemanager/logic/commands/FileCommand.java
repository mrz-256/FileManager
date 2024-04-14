package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.exceptions.FileException;

/**
 * A command class for logic commands.
 */
public abstract class FileCommand {

    /**
     * Method to execute command
     */
    public abstract void execute() throws FileException;

    /**
     * Method to undo given command
     */
    public abstract void undo();

    /**
     * Method to get string name of command
     * @return the string name of command
     */
    public abstract String getID();
}
