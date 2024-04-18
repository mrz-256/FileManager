package com.example.filemanager.logic;

import com.example.filemanager.logic.commands.*;
import com.example.filemanager.logic.exceptions.FileException;

import java.io.File;
import java.util.ArrayList;


/**
 * A class representing a single tab withing file manager.
 * CommandHistory is a singleton common for all tabs.
 */
public class Tab {
    private final Context context;
    private final ArrayList<File> filesToList;

    public Tab(File directory) {
        context = new Context(directory);
        filesToList = new ArrayList<>();
    }

    /**
     * Sets current directory of this Tab.
     * @param directory the directory to use
     * @throws FileException when directory is invalid or can't be opened
     */
    public void setDirectory(File directory) throws FileException {
        if (!directory.exists()) {
            throw new FileException("Directory does not exist.", directory);
        }
        if (!directory.isDirectory()) {
            throw new FileException("File is not a directory.", directory);
        }

        context.setDirectory(directory);
        updateListedFiles();
    }

    /**
     * Updates list of listed files.
     * @throws FileException when files can't be listed
     */
    public void updateListedFiles() throws FileException {
        FileCommand command = new ListAllCommand(context);
        command.execute();

        filesToList.clear();
        filesToList.addAll(context.getResult());
    }

    /**
     * Gives list of files visible in this Tab by given configuration of context.
     * @return the files to list
     */
    public ArrayList<File> getFilesToList() {
        return filesToList;
    }

    /**
     * Executes text representation of a command on parameters params within this context.
     * @param command command to execute.
     * @param params optional params to use
     * @throws FileException when command can not be executed.
     */
    public void executeCommand(String command, File... params) throws FileException {
        context.clearWorking();
        context.addToWorking(params);

        switch (command.toLowerCase()) {
            case "undo" -> CommandHistory.undoLast();
            case "new_file" -> new NewFileCommand(context).execute();
            case "new_directory" -> new NewDirectoryCommand(context).execute();
            case "delete_files" -> new DeleteFilesCommand(context).execute();
            case "paste_files" -> new PasteFilesCommand(context).execute();
        }
    }
}
