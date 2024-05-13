package com.example.filemanager.logic.commands.commands;

import com.example.filemanager.logic.commands.CommandContext;
import com.example.filemanager.logic.commands.FileCommandName;
import com.example.filemanager.logic.exceptions.FileException;

import java.io.File;
import java.util.ArrayList;

/**
 * A command class for logic commands.
 */
public abstract class FileCommand {

    /**
     * Returns the appropriate command implementation based on the provided name
     *
     * @param name the command to match
     * @return the command
     * @throws FileException the prohibited command type usage
     */
    public static FileCommand getByType(FileCommandName name) throws FileException {
        return switch (name) {
            case UNDO -> null;
            case DELETE -> new DeleteFilesCommand();
            case DUPLICATE -> new DuplicateFileCommand();
            case LIST_ALL -> new ListAllCommand();
            case NEW_DIRECTORY -> new NewDirectoryCommand();
            case NEW_FILE -> new NewFileCommand();
            case OPEN -> new OpenFileCommand();
            case PASTE -> new PasteFilesCommand();
            case RENAME -> new RenameFileCommand();
            case FIND -> new FindCommand();
            case _directory_only_start -> throw new FileException("Prohibited command type.");
        };
    }


    /**
     * Method to execute command. Returns null if the command itself doesn't look up any files, in case of list
     * and search, returns found files.
     */
    public abstract ArrayList<File> execute(CommandContext context) throws FileException;

    /**
     * Method to undo given command
     */
    public void undo() throws FileException {
    }

    /**
     * Method to get string name of command
     *
     * @return the string name of command
     */
    public abstract FileCommandName getID();
}
