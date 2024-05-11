package com.example.filemanager.logic;


import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

/**
 * A class with static functions focused on logic for working with files.
 */
public class FileUtilFunctions {


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
     * <br>
     * It's possible the name with incremented name already exists too, so recursion is necessary.
     *
     * @param file the file name to turn into something unique
     * @return a new File with unique name similar to original
     */
    public static File inventUniqueName(File file) {
        // crude division into two methods, solves the problem
        if (file.isFile()) {
            return inventUniqueFileName(file);
        } else {
            return inventUniqueDirectoryName(file);
        }
    }

    private static File inventUniqueFileName(File file) {
        if (!file.exists()) return file;
        var name = file.getName();

        // in case where file name doesn't have a counter yet, it appends it in between the name and extension.
        // New name may already exist, so function must recursively check for that too
        if (!name.matches("^.*\\(\\d+\\)\\..+$")) {
            String new_name = name.replaceAll("^(.*)(\\..+)$", "$1(1)$2");
            return inventUniqueName(new File(file.getParent(), new_name));
        }

        // this is the already existing counter in the filename
        String num = name.replaceAll(".*\\((\\d+)\\)\\..+$", "$1");

        // create new name with incremented counter
        num = String.valueOf(Integer.parseInt(num) + 1);
        String new_name = name.replaceAll("^(.*)\\(\\d+\\)(\\..+)$", "$1(" + num + ")$2");

        // check the new name
        return inventUniqueName(new File(file.getParent(), new_name));
    }

    private static File inventUniqueDirectoryName(File file) {
        if (!file.exists()) return file;
        var name = file.getName();

        // in case where file name doesn't have a counter yet, it appends it in between the name and extension.
        // New name may already exist, so function must recursively check for that too
        if (!name.matches("^.*\\(\\d+\\)$")) {
            String new_name = name.replaceAll("^(.*)$", "$1(1)");
            return inventUniqueName(new File(file.getParent(), new_name));
        }

        // this is the already existing counter in the filename
        String num = name.replaceAll(".*\\((\\d+)\\)$", "$1");

        // create new name with incremented counter
        num = String.valueOf(Integer.parseInt(num) + 1);
        String new_name = name.replaceAll("^(.*)\\(\\d+\\)$", "$1(" + num + ")");

        // check the new name
        return inventUniqueName(new File(file.getParent(), new_name));
    }

    /**
     * Returns the file extension. First checks for special types like directory and hidden before checking
     * actual extension.
     *
     * @param file the file
     * @return the type
     */
    public static String getFileType(File file) {
        if (file.isDirectory()) {
            return "directory";
        }
        if (file.isHidden()) {
            return "hidden";
        }

        var name = file.getName();

        if (name.charAt(name.length() - 1) == '~') {
            return "save";
        }
        if (!name.matches(".*\\..+")) {
            return "unknown";
        }
        return name.replaceFirst(".*\\.(.*)", "$1");
    }

    /**
     * Returns string path to home directory on given machine
     *
     * @return the path
     */
    public static String getHomePath() {
        return System.getProperty("user.home");
    }

    /**
     * Returns directory of the system home
     *
     * @return the directory of "user.home" on given system
     */
    public static File getHomeDirectory() {
        return new File(getHomePath());
    }


    /**
     * Transform numerical size in bytes into minimal string representation
     *
     * @param byteSize the size in bytes
     * @return the optimal string representation
     */
    public static String getOptimalSizeFormat(long byteSize) {
        String[] units = {"", "K", "M", "G", "T", "P"};
        int unit_index = 0;

        double size = byteSize;

        while (size > 1024) {
            size /= 1024;
            unit_index++;
        }
        size = Math.floor(size * 100) / 100; // rounds to two decimal places
        return size + " " + units[unit_index] + "B";
    }

    /**
     * Writes epoch time as a string date
     *
     * @param epochTime milliseconds since epoch
     * @return yyyy/mm/dd formatted date
     */
    public static String getSimplifiedDate(long epochTime) {
        LocalDate date = LocalDate.ofEpochDay(epochTime / 1000 / 60 / 60 / 24);
        return date.toString();
    }

    /**
     * Stores files to clipboard
     *
     * @param files the files to store
     */
    public static void storeFileToClipboard(File... files) {
        var clipboard = Clipboard.getSystemClipboard();
        var clipboardContent = new ClipboardContent();

        clipboardContent.putFiles(List.of(files));
        clipboard.setContent(clipboardContent);
    }

    /**
     * Stores text to clipboard
     *
     * @param text the text to store
     */
    public static void storeTextToClipboard(String text) {
        var clipboard = Clipboard.getSystemClipboard();
        var clipboardContent = new ClipboardContent();

        clipboardContent.putString(text);
        clipboardContent.putHtml(text);
        clipboard.setContent(clipboardContent);
    }

    /**
     * Gets the file contents from keyboard
     *
     * @return the files from keyboard
     */
    public static File[] getFilesFromClipboard() {
        var clipboard = Clipboard.getSystemClipboard();
        return clipboard.getFiles().toArray(new File[0]);
    }

}
