package com.example.filemanager.logic.commands;

public enum FileCommandName{
    UNDO, DELETE, LIST_ALL, OPEN, RENAME, SEARCH, _directory_only_start, NEW_DIRECTORY, NEW_FILE, PASTE;

    /**
     * Checks if command is safe in any context, i.e. inside search results rather than in directory
     * @return false when command can only be executed inside directory, otherwise true
     */
    public boolean isUniversalSafe(){
        return ordinal() < _directory_only_start.ordinal();
    }
}
