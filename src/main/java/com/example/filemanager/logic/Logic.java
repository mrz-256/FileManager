package com.example.filemanager.logic;

import com.example.filemanager.logic.commands.CommandHistory;
import com.example.filemanager.logic.sort_strategy.NameStrategy;
import com.example.filemanager.logic.sort_strategy.SortStrategy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


public class Logic {




    /**
     * A helper method used to copy file. Should only be used through commands.
     *
     * @param source      source of the file
     * @param destination the name to copy into
     * @return success
     */
    public static boolean copyFile(File source, File destination) {
        try {
            Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Returns a new file with name which doesn't exist yet.
     * Example: test-file.txt -> test-file(1).txt
     * If test-file(1).txt already exists, it will be test-file(2).txt and so on.
     * @param file the file name to turn into something unique
     * @return a new File with unique name similar to original
     */
    public static File inventUniqueName(File file){
        // It's possible the name with incremented name already exists too, so recursion is necessary.

        // file already has unique name
        if (!file.exists()) return file;

        // file doesn't have counter
        if (!file.getName().matches("^.*\\(\\d+\\)\\..+")){
            String new_name = file.getName().replaceAll("^(.*)(\\..+)$", "$1(1)$2");
            return inventUniqueName(new File(file.getParent() + "/" + new_name));
        }

        // get the existing counter
        String num = file.getName().replaceAll(".*\\((\\d+)\\)\\..+$", "$1");

        // create new name with incremented counter
        num = String.valueOf(Integer.parseInt(num) + 1);
        String new_name = file.getName().replaceAll("^(.*)\\(\\d+\\)(\\..+)$", "$1("+num+")$2");

        return inventUniqueName(new File(file.getParent() + "/" + new_name));
    }

    /**
     * Undoes last executed command.
     */
    public static void undoLastCommand() {
        CommandHistory.undoLast();
    }

    //region getters

    /**
     * Returns string path to home directory on given machine
     *
     * @return the path
     */
    public static String getHome() {
        return System.getProperty("user.home");
    }
}
