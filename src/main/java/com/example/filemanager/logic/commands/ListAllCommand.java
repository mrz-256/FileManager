package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.LogicalConfiguration;
import com.example.filemanager.logic.exceptions.FileException;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class ListAllCommand extends FileCommand {

    /**
     * Lists all files in current directory
     *
     * @return found files
     * @throws FileException never
     */
    @Override
    public ArrayList<File> execute(
            File directory, LogicalConfiguration configuration, File[] working
    ) throws FileException {
        CommandHistory.addCommand(this, false);
        var result = new ArrayList<File>();

        var hidden_files = new LinkedList<File>();
        var files = new LinkedList<File>();
        var hidden_directories = new LinkedList<File>();
        var directories = new LinkedList<File>();

        var all = directory.listFiles();

        if (all == null) {
            return result;
        }

        for (var file : all) {
            if (file.isHidden()) {
                if (!configuration.showHiddenFiles) continue;

                if (file.isDirectory()) hidden_directories.add(file);
                else hidden_files.add(file);
            } else {
                if (file.isDirectory()) directories.add(file);
                else files.add(file);
            }
        }

        configuration.apply(hidden_directories);
        configuration.apply(hidden_files);
        configuration.apply(directories);
        configuration.apply(files);

        result.addAll(hidden_directories);
        result.addAll(directories);
        result.addAll(hidden_files);
        result.addAll(files);

        return result;
    }

    @Override
    public String getID() {
        return "list_all";
    }
}
