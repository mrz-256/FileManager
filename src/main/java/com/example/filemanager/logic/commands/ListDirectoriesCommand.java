package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.Logic;

import java.io.File;

public class ListDirectoriesCommand extends FileCommand{
    /**
     * Lists directories from Logic::directory, result is in Logic::currentResult
     */
    @Override
    public void execute() {
        Logic instance = Logic.getInstance();

        File[] directories = instance.getDirectory().listFiles(file ->{
            return file.isDirectory()
                    && (instance.isShowHidden() || !file.isHidden())
                    && file.getName().matches(".*" + instance.getFilter() + ".*");
        });

        if (directories == null) return;

        instance.getSortStrategy().sort(directories);

        Logic.setCurrentResult(directories);
    }

    @Override
    public void undo() {
        // nothing to undo here
    }

    @Override
    public String getID() {
        return "list_directories_command";
    }
}

