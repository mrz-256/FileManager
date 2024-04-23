package com.example.filemanager.logic.commands;
import com.example.filemanager.logic.FileUtilFunctions;
import com.example.filemanager.logic.LogicalConfiguration;
import com.example.filemanager.logic.exceptions.FileException;
import com.example.filemanager.logic.exceptions.RenameFileException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RenameFileCommand extends FileCommand {
    private File original;
    private File new_file;


    /**
     * Renames the first file in working to the second file in working
     * @return null
     * @throws FileException when renaming don't work
     */
    @Override
    public ArrayList<File> execute(File directory, LogicalConfiguration configuration, File[] working) throws FileException {
        CommandHistory.addCommand(this, true);
        if (working == null || working.length != 2) throw new RenameFileException("Invalid arguments");

        original = working[0];
        new_file = working[1];

        if (new_file.exists()) throw new RenameFileException("File already exits.", new_file);
        if (original.equals(new_file)) throw new RenameFileException("New file is same as the old one.", new_file);

        FileUtilFunctions.copyFile(original, new_file);

        original.delete();

        return null;
    }

    @Override
    public void undo() {
        if (!new_file.exists()) return;

        FileUtilFunctions.copyFile(new_file, original);
        new_file.delete();
    }

    @Override
    public String getID() {
        return "rename";
    }
}
