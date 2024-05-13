package com.example.filemanager.logic.commands.commands;

import com.example.filemanager.logic.FUtil;
import com.example.filemanager.logic.commands.CommandContext;
import com.example.filemanager.logic.commands.CommandHistory;
import com.example.filemanager.logic.commands.FileCommandName;
import com.example.filemanager.logic.exceptions.DeleteFileException;
import com.example.filemanager.logic.exceptions.DuplicateFileException;
import com.example.filemanager.logic.exceptions.FileException;

import java.io.File;
import java.util.ArrayList;

/**
 * A command that deletes all files from context working[],
 * temporally stores them so that action can be undone.
 */
public class DeleteFilesCommand extends FileCommand {
    private final ArrayList<File> save;
    private final ArrayList<File> original;


    public DeleteFilesCommand() {
        save = new ArrayList<>();
        original = new ArrayList<>();
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
            saveFile(file);

            try {
                FUtil.deepDelete(file);
            } catch (Exception e) {
                error.append(e);
            }

        }

        if (!error.isEmpty()) {
            throw new DeleteFileException(error.toString());
        }

        return null;
    }

    private void saveFile(File file) {
        try {
            File tmp = File.createTempFile(file.getName(), null);

            FUtil.deepCopy(file, tmp);
            tmp.deleteOnExit();
            save.add(tmp);
            original.add(file);

        } catch (Exception ignored) {
        }
    }

    @Override
    public void undo() throws DuplicateFileException {
        for (int i = 0; i < save.size(); i++) {
            if (save.get(i) != null && original.get(i) != null) {
                FUtil.deepCopy(save.get(i), original.get(i));
            }
        }
    }

    @Override
    public FileCommandName getID() {
        return FileCommandName.DELETE;
    }
}
