package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.Logic;
import com.example.filemanager.logic.exceptions.FileException;

import java.io.File;

public class ListFilesCommand extends FileCommand{
    /**
     * Lists files from Logic::directory, result is in Logic::currentResult
     */
    @Override
    public void execute() throws FileException {
        CommandHistory.addCommand(this);


        File[] files = Logic.getDirectory().listFiles(file ->{
            return file.isFile()
                    && (Logic.isShowHidden() || !file.isHidden())
                    && file.getName().matches(".*" + Logic.getFilter() + ".*");
        });

        if (files == null) return;

        Logic.getSortStrategy().sort(files);

        Logic.setCurrentResult(files);
    }

    @Override
    public void undo() {
        // nothing to undo here
    }

    @Override
    public String getID() {
        return "list_files_command";
    }
}
