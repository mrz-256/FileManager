package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.Logic;

import java.io.File;

public class DeleteFileCommand extends FileCommand{


    @Override
    public void execute() {
        Logic instance = Logic.getInstance();

        File save = new File(instance.getWorkingFile().getAbsolutePath());

    }

    @Override
    public void undo() {

    }

    @Override
    public String getID() {
        return null;
    }
}
