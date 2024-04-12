package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.Logic;

import java.io.File;

public class ListFilesCommand extends FileCommand{
    /**
     * Lists files from Logic::directory, result is in Logic::currentResult
     */
    @Override
    public void execute() {
        Logic instance = Logic.getInstance();

        File[] files = instance.getDirectory().listFiles(file ->{
            return file.isFile()
                    && (instance.isShowHidden() || !file.isHidden())
                    && file.getName().matches(".*" + instance.getFilter() + ".*");
        });

        if (files == null) return;

        instance.getSortStrategy().sort(files);

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
