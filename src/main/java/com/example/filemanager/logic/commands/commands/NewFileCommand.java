package com.example.filemanager.logic.commands.commands;

import com.example.filemanager.logic.commands.CommandContext;
import com.example.filemanager.logic.commands.CommandHistory;
import com.example.filemanager.logic.commands.FileCommandName;
import com.example.filemanager.logic.exceptions.FileException;
import com.example.filemanager.logic.exceptions.NewFileException;

import java.io.File;
import java.util.ArrayList;

/**
 * A command to create the first file in context working (working[0])
 */
public class NewFileCommand extends FileCommand {
    private File creation;


    /**
     * Creates the file first file in working
     *
     * @return null
     * @throws FileException when file already exists or creation fails
     */
    @Override
    public ArrayList<File> execute(CommandContext context) throws FileException {
        CommandHistory.addCommand(this, true);

        if (context.working() == null || context.working().length == 0) {
            return null;
        }

        creation = context.working()[0];

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
    public void undo() throws NewFileException {
        if (creation.exists()) {
            if (!creation.delete()) {
                throw new NewFileException("Can't undo creation of file.", creation);
            }
        }
    }

    @Override
    public FileCommandName getID() {
        return FileCommandName.NEW_FILE;
    }
}
