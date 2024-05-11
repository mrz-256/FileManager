package com.example.filemanager.logic.commands.commands;

import com.example.filemanager.logic.FileUtilFunctions;
import com.example.filemanager.logic.commands.CommandContext;
import com.example.filemanager.logic.commands.CommandHistory;
import com.example.filemanager.logic.commands.FileCommandName;
import com.example.filemanager.logic.exceptions.DeleteFileException;

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

            if (!file.delete()) {
                error.append("Failed deleting file | ").append(file).append('\n');
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

            if (FileUtilFunctions.copyFile(file, tmp)) {
                tmp.deleteOnExit();
                save.add(tmp);
                original.add(file);
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void undo() {
        for (int i = 0; i < save.size(); i++) {
            if (save.get(i) != null && original.get(i) != null) {
                FileUtilFunctions.copyFile(save.get(i), original.get(i));
            }
        }
    }

    @Override
    public FileCommandName getID() {
        return FileCommandName.DELETE;
    }
}
