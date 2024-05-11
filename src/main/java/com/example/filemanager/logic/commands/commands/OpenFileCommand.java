package com.example.filemanager.logic.commands.commands;


import com.example.filemanager.logic.LogicalConfiguration;
import com.example.filemanager.logic.commands.CommandContext;
import com.example.filemanager.logic.commands.CommandHistory;
import com.example.filemanager.logic.exceptions.FileException;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A command to open a file.
 */
public class OpenFileCommand extends FileCommand {

    private static class FileOpener extends Thread {
        private final File file;

        public FileOpener(File file) {
            this.file = file;
        }

        @Override
        public void run() {
            Desktop desktop = Desktop.getDesktop();

            if (desktop.isSupported(Desktop.Action.OPEN)){
                try {
                    desktop.open(file);
                } catch (IOException e) {
                    // todo: somehow propagate the error somewhere
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    /**
     * Opens the first file in `working` in the default application.
     * The file is opened in another thread so that worker thread executing this command isn't affected.
     *
     * @return null;
     * @throws FileException when opening fails.
     */
    @Override
    public ArrayList<File> execute(CommandContext context) throws FileException  {
        CommandHistory.addCommand(this, false);

        if (context.working() == null || context.working().length == 0){
            throw new FileException("Not provided any file.");
        }

        var file = context.working()[0];

        if (!file.exists() || file.isDirectory()){
            throw new FileException("Invalid file.", file);
        }

        //actually opens the file
        var fileOpener = new FileOpener(file);
        fileOpener.start();

        return null;
    }

    @Override
    public String getID() {
        return "open";
    }
}
