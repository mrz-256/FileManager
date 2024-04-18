package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.Context;
import com.example.filemanager.logic.exceptions.FileException;
import com.example.filemanager.logic.exceptions.NewFileException;

import java.io.File;

/**
 * A command to create the first file in context working as directory
 */
public class NewDirectoryCommand extends FileCommand{
    File directory;

    public NewDirectoryCommand(Context context) {
        super(context);
    }

    @Override
    public void execute() throws FileException {
        CommandHistory.addCommand(this, true);

        directory = context.getWorkingAt(0);

        if (directory.exists()) return;

        try {
            if (!directory.mkdir()) throw new Exception("not created");
        } catch (Exception e) {
            throw new NewFileException("Failed creating directory (" + e.getMessage() + ") | " + directory);
        }
    }

    @Override
    public void undo() {
        if (directory.exists()){
            directory.delete();
        }
    }

    @Override
    public String getID() {
        return null;
    }
}
