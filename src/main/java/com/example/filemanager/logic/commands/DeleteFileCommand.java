package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.Logic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class DeleteFileCommand extends FileCommand {
    private File save;
    private File original;
    private boolean saved;

    @Override
    public void execute() {
        Logic instance = Logic.getInstance();

        File file = instance.getWorkingFile();

        // creates a copy in tmp/ which is deleted on the exit of the program
        try {
            save = File.createTempFile(file.getName(), null);
        } catch (IOException ignored) {
        }
        saved = Logic.copyFile(file, save);
        if (saved) {
            save.deleteOnExit();
        }

        file = instance.getWorkingFile();
        file.delete();
        original = instance.getWorkingFile();
    }

    @Override
    public void undo() {
        if (saved){
            Logic.copyFile(save, original);
        }
    }

    @Override
    public String getID() {
        return "delete_file_command";
    }
}
