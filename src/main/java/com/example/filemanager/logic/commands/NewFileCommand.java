package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.Context;
import com.example.filemanager.logic.exceptions.FileException;
import com.example.filemanager.logic.exceptions.NewFileException;

import java.io.File;

/**
 * A command to create the first file in context working (working[0])
 */
public class NewFileCommand extends FileCommand{
    private File file;

    public NewFileCommand(Context context) {
        super(context);
    }

    @Override
    public void execute() throws FileException {
        CommandHistory.addCommand(this, true);

        file = context.getWorkingAt(0);

        if (file.exists()) return;

        try {
            if (!file.createNewFile()) throw new Exception("not created");
        } catch (Exception e) {
            throw new NewFileException("Failed creating file (" + e.getMessage() + ") | " + file);
        }

    }

    @Override
    public void undo() {
        if (file.exists()){
            file.delete();
        }
    }

    @Override
    public String getID() {
        return "new_file_command";
    }
}
