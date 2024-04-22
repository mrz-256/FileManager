package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.Context;
import com.example.filemanager.logic.FileUtilFunctions;
import com.example.filemanager.logic.LogicalConfiguration;
import com.example.filemanager.logic.exceptions.FileException;
import com.example.filemanager.logic.exceptions.PasteFilesException;

import java.io.File;
import java.util.ArrayList;

/**
 *  A command to paste all files from context working[] into context directory.
 *  Stores them temporally so that action can be undone
 */
public class PasteFilesCommand extends FileCommand{
    private ArrayList<File> copies;

    /**
     * Pastes all files into `directory`, if file with given name already exists, creates new name by appending (1)
     * @return null
     * @throws FileException when specific files fail
     */
    @Override
    public ArrayList<File> execute(
            File directory, LogicalConfiguration configuration, File[] working
    ) throws FileException {
        CommandHistory.addCommand(this, true);

        if (working == null || working.length == 0) return null;

        copies = new ArrayList<>();
        StringBuilder error = new StringBuilder();



        for (var file : working) {
            if (file == null) continue;

            File new_file = FileUtilFunctions.inventUniqueName(new File(directory, file.getName()));
            copies.add(new_file);

            if (!FileUtilFunctions.copyFile(file, new_file)){
                error.append("Failed copying file | ").append(file).append('\n');
            }
        }

        if (!error.isEmpty()){
            throw new PasteFilesException(error.toString());
        }

        return null;
    }

    /**
     * Deletes the added files.
     */
    @Override
    public void undo() {
        for(var file : copies){
            if (file != null) file.delete();
        }
    }

    @Override
    public String getID() {
        return "paste_files";
    }
}
