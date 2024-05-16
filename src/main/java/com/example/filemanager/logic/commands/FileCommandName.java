package com.example.filemanager.logic.commands;

/**
 * Lists the names of possible commands.
 */
public enum FileCommandName {
    UNDO,
    DELETE,
    DUPLICATE,
    LIST_ALL,
    OPEN,
    RENAME,
    FIND,
    /**
     * Divides the command into the universally-safe ones and the one that may only be executed in directory.
     * Should never be used as a command.
     */
    _directory_only_start,
    NEW_DIRECTORY,
    NEW_FILE,
    PASTE;

    /**
     * Checks if command is safe in any context, i.e. inside search results rather than in directory
     *
     * @return false when command can only be executed inside directory, otherwise true
     */
    public boolean isUniversalSafe() {
        return ordinal() < _directory_only_start.ordinal();
    }
}
