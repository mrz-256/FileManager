package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.Context;
import com.example.filemanager.logic.exceptions.FileException;

import java.io.File;

/**
 * A command to list files in context directory based on context configuration
 */
public class ListFilesCommand extends FileCommand {

    public ListFilesCommand(Context context) {
        super(context);
    }

    @Override
    public void execute() throws FileException {
        CommandHistory.addCommand(this, false);
        context.clearResult();


        File[] files = context.getDirectory().listFiles(file -> {
            return file.isFile()
                    && (context.getConfiguration().showHiddenFiles || !file.isHidden())
                    && file.getName().matches(".*" + context.getConfiguration().filter + ".*");
        });

        if (files == null) return;

        context.getConfiguration().sortStrategy.sort(files);

        context.addToResult(files);
    }

    @Override
    public void undo() {
        // nothing to undo here
    }

    @Override
    public String getID() {
        return "list_files_command";
    }
}
