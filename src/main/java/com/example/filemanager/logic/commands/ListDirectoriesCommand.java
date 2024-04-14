package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.Logic;
import com.example.filemanager.logic.exceptions.FileException;

import java.io.File;

public class ListDirectoriesCommand extends FileCommand{
    /**
     * Lists directories from Logic::directory, result is in Logic::currentResult
     */
    @Override
    public void execute() throws FileException {
        CommandHistory.addCommand(this);

        if (Logic.getWorkingFiles().length == 0 || Logic.getWorkingFiles()[0] == null) return;

        File[] directories = Logic.getWorkingFiles()[0].listFiles(file ->{
            return file.isDirectory()
                    && (Logic.isShowHidden() || !file.isHidden())
                    && file.getName().matches(".*" + Logic.getFilter() + ".*");
        });

        if (directories == null) return;

        Logic.getSortStrategy().sort(directories);

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

