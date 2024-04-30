package com.example.filemanager.logic.commands;


import com.example.filemanager.logic.LogicalConfiguration;
import com.example.filemanager.logic.exceptions.FileException;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class OpenFileCommand extends FileCommand {

    public static class FileOpener extends Thread {
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
                    System.out.println("Log: can't open file " + file + " | " + e.getMessage());
                }
            }
        }
    }

    @Override
    public ArrayList<File> execute(File directory, LogicalConfiguration configuration, File[] working) throws FileException {
        if (working == null || working.length == 0){
            throw new FileException("Not provided any file.");
        }

        var file = working[0];

        if (!file.exists() || file.isDirectory()){
            throw new FileException("Invalid file.", file);
        }


        FileOpener fo = new FileOpener(working[0]);
        fo.start();



        return null;
    }

    @Override
    public String getID() {
        return "open";
    }
}
