package com.example.filemanager.logic.commands;


import com.example.filemanager.logic.LogicalConfiguration;
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
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    /**
     * Opens the first file in `working` in the default application.
     * The file is opened in another thread because weird error which I can't catch and which always freezes the main
     * thread.
     * @param directory the current directory (unused here)
     * @param configuration the configuration (unused here)
     * @param working the file to open
     * @return null
     * @throws FileException when opening fails.
     */
    @Override
    public ArrayList<File> execute(File directory, LogicalConfiguration configuration, File[] working) throws FileException {
        CommandHistory.addCommand(this, false);

        if (working == null || working.length == 0){
            throw new FileException("Not provided any file.");
        }

        var file = working[0];

        if (!file.exists() || file.isDirectory()){
            throw new FileException("Invalid file.", file);
        }


        var fo = new FileOpener(file);
        fo.start();

        return null;
    }

    @Override
    public String getID() {
        return "open";
    }
}
