package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.Context;
import com.example.filemanager.logic.LogicalConfiguration;
import com.example.filemanager.logic.exceptions.FileException;
import com.example.filemanager.logic.exceptions.NewFileException;

import java.io.File;
import java.util.ArrayList;

/**
 * A command to create the first file in context working (working[0])
 */
public class NewFileCommand extends FileCommand{
    private File creation;


    /**
     * Creates the file first file in working
     * @return null
     * @throws FileException when file already exists or creation fails
     */
    @Override
    public ArrayList<File> execute(
            File directory, LogicalConfiguration configuration, File[] working
    ) throws FileException {
        CommandHistory.addCommand(this, true);

        if (working == null || working.length == 0) return null;

        creation = working[0];

        if (creation.exists()) throw new NewFileException("File already exists", creation);

        try {
            if (!creation.createNewFile()) throw new Exception("not created");
        } catch (Exception e) {
            throw new NewFileException("Failed creating file (" + e.getMessage() + ")", creation);
        }

        return null;
    }

    /**
     * Deletes created file
     */
    @Override
    public void undo() {
        if (creation.exists()){
            creation.delete();
        }
    }

    @Override
    public String getID() {
        return "new_file";
    }
}
