package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.Context;
import com.example.filemanager.logic.LogicalConfiguration;
import com.example.filemanager.logic.exceptions.FileException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A command to list directories in context directory based on context configuration
 */
public class ListDirectoriesCommand extends FileCommand {

    /**
     * Lists all directories in current directory
     * @return list of directories
     * @throws FileException never
     */
    @Override
    public ArrayList<File> execute(
            File directory, LogicalConfiguration configuration, File[] working
    ) throws FileException {
        CommandHistory.addCommand(this, false);


        File[] directories = directory.listFiles(file -> {
            return file.isDirectory()
                    && (configuration.showHiddenFiles || !file.isHidden())
                    && file.getName().matches(".*" + configuration.filter + ".*");
        });
        if (directories == null || directories.length == 0) return null;

        configuration.sortStrategy.sort(directories);

        return new ArrayList<>(List.of(directories));
    }

    @Override
    public String getID() {
        return "list_directories";
    }
}

