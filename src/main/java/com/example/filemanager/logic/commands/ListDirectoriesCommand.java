package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.Context;
import com.example.filemanager.logic.exceptions.FileException;

import java.io.File;

/**
 * A command to list directories in context directory based on context configuration
 */
public class ListDirectoriesCommand extends FileCommand {

    public ListDirectoriesCommand(Context context) {
        super(context);
    }

    @Override
    public void execute() throws FileException {
        CommandHistory.addCommand(this, false);
        context.clearResult();


        File[] directories = context.getDirectory().listFiles(file -> {
            return file.isDirectory()
                    && (context.getConfiguration().showHiddenFiles || !file.isHidden())
                    && file.getName().matches(".*" + context.getConfiguration().filter + ".*");
        });

        if (directories == null) return;

        context.getConfiguration().sortStrategy.sort(directories);

        context.addToResult(directories);
    }

    @Override
    public void undo() {
        // nothing to undo here
    }

    @Override
    public String getID() {
        return "list_directories";
    }
}

