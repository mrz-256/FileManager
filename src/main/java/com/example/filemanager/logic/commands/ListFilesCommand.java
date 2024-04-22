package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.LogicalConfiguration;
import com.example.filemanager.logic.exceptions.FileException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A command to list files in context directory based on context configuration
 */
public class ListFilesCommand extends FileCommand {

    /**
     * Lists all files in directory
     * @return list of the files
     * @throws FileException never
     */
    @Override
    public ArrayList<File> execute(
            File directory, LogicalConfiguration configuration, File[] working
    )  throws FileException {
        CommandHistory.addCommand(this, false);

        File[] files = directory.listFiles(file -> {
            return file.isFile()
                    && (configuration.showHiddenFiles || !file.isHidden())
                    && file.getName().matches(".*" + configuration.filter + ".*");
        });

        if (files == null || files.length == 0) return null;

        configuration.sortStrategy.sort(files);

        return new ArrayList<>(List.of(files));
    }

    @Override
    public String getID() {
        return "list_files";
    }
}
