package com.example.filemanager.logic.commands.commands;

import com.example.filemanager.logic.FUtil;
import com.example.filemanager.logic.commands.CommandContext;
import com.example.filemanager.logic.commands.CommandHistory;
import com.example.filemanager.logic.commands.FileCommandName;
import com.example.filemanager.logic.exceptions.FileException;
import com.example.filemanager.logic.exceptions.RenameFileException;

import java.io.File;
import java.util.ArrayList;

public class RenameFileCommand extends FileCommand {
    private File original;
    private File new_file;


    /**
     * Renames the first file in working to the second file in working
     *
     * @return null
     * @throws FileException when renaming don't work
     */
    @Override
    public ArrayList<File> execute(CommandContext context) throws FileException {
        CommandHistory.addCommand(this, true);

        if (context.working() == null || context.working().length != 2) {
            throw new RenameFileException("Invalid arguments");
        }

        original = context.working()[0];
        new_file = context.working()[1];

        if (new_file.exists()) throw new RenameFileException("File already exits.", new_file);
        if (original.equals(new_file)) throw new RenameFileException("New file is same as the old one.", new_file);

        FUtil.copyFile(original, new_file);

        original.delete();

        return null;
    }

    @Override
    public void undo() {
        if (!new_file.exists()) return;

        FUtil.copyFile(new_file, original);
        new_file.delete();
    }

    @Override
    public FileCommandName getID() {
        return FileCommandName.RENAME;
    }
}
