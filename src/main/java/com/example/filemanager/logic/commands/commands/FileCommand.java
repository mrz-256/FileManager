package com.example.filemanager.logic.commands.commands;

import com.example.filemanager.logic.LogicalConfiguration;
import com.example.filemanager.logic.commands.CommandContext;
import com.example.filemanager.logic.exceptions.FileException;

import java.io.File;
import java.util.ArrayList;

/**
 * A command class for logic commands.
 */
public abstract class FileCommand {

    /**
     * Method to execute command. Returns null if the command itself doesn't look up any files, in case of list
     * and search, returns found files.
     */
    public abstract ArrayList<File> execute(CommandContext context) throws FileException;

    /**
     * Method to undo given command
     */
    public void undo() {
    }

    /**
     * Method to get string name of command
     *
     * @return the string name of command
     */
    public abstract String getID();
}
