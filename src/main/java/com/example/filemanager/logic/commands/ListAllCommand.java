package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.LogicalConfiguration;
import com.example.filemanager.logic.exceptions.FileException;

import java.io.File;
import java.util.ArrayList;

public class ListAllCommand extends FileCommand {

    /**
     * Lists all files in current directory
     * @return found files
     * @throws FileException never
     */
    @Override
    public ArrayList<File> execute(
            File directory, LogicalConfiguration configuration, File[] working
    ) throws FileException{
        CommandHistory.addCommand(this, false);
        var result = new ArrayList<File>();

        var directoriesCommand = new ListDirectoriesCommand();
        var directories = directoriesCommand.execute(directory,configuration,null);
        if (directories != null){
            result.addAll(directories);
        }

        var filesCommand = new ListFilesCommand();
        var files = filesCommand.execute(directory, configuration, null);
        if (files != null){
            result.addAll(files);
        }

        return result;
    }

    @Override
    public String getID() {
        return "list_all";
    }
}
