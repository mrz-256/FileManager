package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.Context;
import com.example.filemanager.logic.Logic;
import com.example.filemanager.logic.exceptions.DeleteFileException;
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


    public DeleteFilesCommand(Context context) {
        super(context);
        save = new ArrayList<>();
        original = new ArrayList<>();
    }

    @Override
    public void execute() throws FileException {
        CommandHistory.addCommand(this, true);
        context.clearResult();

        StringBuilder error = new StringBuilder();


        for (var file : context.getWorking()) {
            saveFile(file);

            if (!file.delete()){
                error.append("Failed deleting file | ").append(file).append('\n');
            }
        }

        if (!error.isEmpty()){
            throw new DeleteFileException(error.toString());
        }
    }

    private void saveFile(File file){
        try {
            File tmp = File.createTempFile(file.getName(), null);

            if (Logic.copyFile(file, tmp)){
                tmp.deleteOnExit();
                save.add(tmp);
                original.add(file);
            }
        } catch (Exception ignored){}
    }

    @Override
    public void undo() {
        for (int i = 0; i < save.size(); i++) {
            if (save.get(i) != null && original.get(i) != null){
                Logic.copyFile(save.get(i), original.get(i));
            }
        }
    }

    @Override
    public String getID() {
        return "delete_file";
    }
}
