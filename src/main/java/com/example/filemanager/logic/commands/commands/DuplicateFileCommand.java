package com.example.filemanager.logic.commands.commands;

import com.example.filemanager.logic.commands.CommandContext;
import com.example.filemanager.logic.commands.CommandHistory;
import com.example.filemanager.logic.commands.FileCommandName;
import com.example.filemanager.logic.exceptions.FileException;

import java.io.File;
import java.util.ArrayList;

/**
 * A convenient wrapper around PASTE command for pasting from source to source, ie, duplicating in same directory.
 */
public class DuplicateFileCommand extends FileCommand {


    /**
     * Pastes all files in working in current directory. If the files already were in current directory, it is effectively
     * a duplication.
     *
     * @return null
     * @throws FileException when specific files fail
     */
    @Override
    public ArrayList<File> execute(CommandContext context) throws FileException {
        // the called sub-command PASTE is undoable, if this one was marked undoable too, the other command would only
        // be undone on the second undo call
        CommandHistory.addCommand(this, false);

        if (context.working() == null || context.working().length == 0) {
            return null;
        }

        new PasteFilesCommand().execute(context);

        return null;
    }

    @Override
    public FileCommandName getID() {
        return FileCommandName.DUPLICATE;
    }
}
