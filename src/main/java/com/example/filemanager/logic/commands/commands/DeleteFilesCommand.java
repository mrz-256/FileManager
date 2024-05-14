package com.example.filemanager.logic.commands.commands;

import com.example.filemanager.logic.FUtil;
import com.example.filemanager.logic.commands.CommandContext;
import com.example.filemanager.logic.commands.CommandHistory;
import com.example.filemanager.logic.commands.FileCommandName;
import com.example.filemanager.logic.exceptions.DeleteFileException;
import com.example.filemanager.logic.exceptions.DuplicateFileException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

/**
 * A command that deletes all files from context working[],
 * temporally stores them so that action can be undone.
 */
public class DeleteFilesCommand extends FileCommand {
    private final LinkedList<File> save;
    private final LinkedList<File> original;


    public DeleteFilesCommand() {
        save = new LinkedList<>();
        original = new LinkedList<>();
    }

    /**
     * Deletes all files in `working`
     *
     * @return always null
     * @throws DeleteFileException when specific deletion fails
     */
    @Override
    public ArrayList<File> execute(CommandContext context) throws DeleteFileException {
        CommandHistory.addCommand(this, true);

        StringBuilder error = new StringBuilder();


        for (var file : context.working()) {

            try {
                var tmp = File.createTempFile("save", ".temp");

                if (file.isFile()){
                    FUtil.copyFile(file, tmp);
                    tmp.deleteOnExit();
                    save.add(tmp);
                    original.add(file);
                }

                FUtil.deepDelete(file);
            } catch (Exception e) {
                error.append(e.getMessage());
            }

        }

        if (!error.isEmpty()) {
            throw new DeleteFileException(error.toString());
        }

        return null;
    }

    @Override
    public void undo() throws DuplicateFileException {
        for (int i = 0; i < save.size(); i++) {
            FUtil.deepCopy(save.get(i), original.get(i));
        }
    }

    @Override
    public FileCommandName getID() {
        return FileCommandName.DELETE;
    }
}
