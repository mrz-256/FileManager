package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.Logic;
import com.example.filemanager.logic.exceptions.FileException;

import java.io.File;

public class PasteFilesCommand extends FileCommand{
    private File[] copies;


    @Override
    public void execute() throws FileException {
        CommandHistory.addCommand(this);

        File[] files = Logic.getWorkingFiles();

    }

    @Override
    public void undo() {

    }

    @Override
    public String getID() {
        return null;
    }
}
