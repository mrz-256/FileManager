package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.Logic;
import com.example.filemanager.logic.exceptions.DeleteFileException;
import com.example.filemanager.logic.exceptions.FileException;

import java.io.File;

public class DeleteFileCommand extends FileCommand {
    private File[] save;
    private File[] original;

    @Override
    public void execute() throws FileException {
        CommandHistory.addCommand(this);

        StringBuilder error = new StringBuilder();


        File[] files = Logic.getWorkingFiles();
        save = new File[files.length];
        original = files.clone();

        for (int i = 0; i < files.length; i++) {
            saveFile(files[i], i);

            if (!files[i].delete()){
                error.append("Failed deleting file | ").append(files[i].getName()).append("\n");
            }
        }

        if (!error.isEmpty()){
            throw new DeleteFileException(error.toString());
        }
    }

    private void saveFile(File file, int position){
        try {
            File tmp = File.createTempFile(file.getName(), null);
            boolean success =  Logic.copyFile(file, tmp);

            if (success){
                tmp.deleteOnExit();
                save[position] = tmp;
                original[position] = file;
            }
        } catch (Exception ignored){}
    }

    @Override
    public void undo() {
        for (int i = 0; i < save.length; i++) {
            if (save[i] != null && original[i] != null)
                Logic.copyFile(save[i], original[i]);
        }

    }

    @Override
    public String getID() {
        return "delete_file_command";
    }
}
