package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.Context;
import com.example.filemanager.logic.exceptions.FileException;

public class SearchCommand extends FileCommand{
    public SearchCommand(Context context) {
        super(context);
    }

    @Override
    public void execute() throws FileException {

    }

    @Override
    public String getID() {
        return null;
    }
}
