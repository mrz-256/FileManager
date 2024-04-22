package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.Context;
import com.example.filemanager.logic.exceptions.FileException;

import java.io.File;
import java.util.ArrayList;

public class ListAllCommand extends FileCommand{
    public ListAllCommand(Context context) {
        super(context);
    }

    @Override
    public void execute() throws FileException {
        CommandHistory.addCommand(this, false);
        context.clearResult();

        FileCommand directoriesCommand = new ListDirectoriesCommand(context);
        directoriesCommand.execute();
        ArrayList<File> result = new ArrayList<>(context.getResult());

        FileCommand filesCommand = new ListFilesCommand(context);
        filesCommand.execute();
        result.addAll(context.getResult());

        context.setResult(result);
    }

    @Override
    public String getID() {
        return "list_all";
    }
}
