package com.example.filemanager.logic.commands.commands;

import com.example.filemanager.logic.FUtil;
import com.example.filemanager.logic.commands.CommandContext;
import com.example.filemanager.logic.commands.CommandHistory;
import com.example.filemanager.logic.commands.FileCommandName;
import com.example.filemanager.logic.exceptions.FileException;
import com.example.filemanager.logic.exceptions.PasteFilesException;

import java.io.File;
import java.util.ArrayList;

/**
 * A command to paste all files from context working[] into context directory.
 * Stores them temporally so that action can be undone
 */
public class PasteFilesCommand extends FileCommand {
    private ArrayList<File> copies;

    /**
     * Pastes all files into `directory`, if file with given name already exists, creates new name by appending (1)
     *
     * @return null
     * @throws FileException when specific files fail
     */
    @Override
    public ArrayList<File> execute(CommandContext context) throws FileException {
        CommandHistory.addCommand(this, true);

        if (context.working() == null || context.working().length == 0) {
            return null;
        }

        copies = new ArrayList<>();
        StringBuilder error = new StringBuilder();


        for (var file : context.working()) {
            if (file == null) {
                continue;
            }

            File new_file = FUtil.inventUniqueName(new File(context.directory(), file.getName()));
            copies.add(new_file);

            if (!FUtil.copyFile(file, new_file)) {
                error.append("Failed copying file | ").append(file).append('\n');
            }
        }

        if (!error.isEmpty()) {
            throw new PasteFilesException(error.toString());
        }

        return null;
    }

    /**
     * Deletes the added files.
     */
    @Override
    public void undo() {
        for (var file : copies) {
            if (file != null) file.delete();
        }
    }

    @Override
    public FileCommandName getID() {
        return FileCommandName.PASTE;
    }
}
