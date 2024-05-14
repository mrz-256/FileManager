package com.example.filemanager.logic;


import com.example.filemanager.logic.exceptions.DeleteFileException;
import com.example.filemanager.logic.exceptions.DuplicateFileException;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.util.Pair;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;

/**
 * A class with static functions focused on logic for working with files.
 */
public class FUtil {


    /**
     * A helper method used to copy file. Should only be used through commands.
     *
     * @param source      source of the file
     * @param destination the name to copy into
     * @return success
     */
    public static boolean copyFile(File source, File destination) {
        try {
            Files.copy(
                    source.toPath(),
                    destination.toPath(),
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.COPY_ATTRIBUTES
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Makes deep copy of source to destination. Skips failed files and continues at work.
     *
     * @param source      the source to copy
     * @param destination the destination to copy to
     * @throws DuplicateFileException to inform of failed copies
     */
    public static void deepCopy(File source, File destination) throws DuplicateFileException {
        var err = new StringBuilder();

        Queue<Pair<File, File>> que = new LinkedList<>();
        que.add(new Pair<>(source, destination));

        while (!que.isEmpty()) {
            var current = que.poll();

            if (current.getKey().isFile()) {
                try {
                    if (!FUtil.copyFile(current.getKey(), current.getValue())) {
                        throw new Exception("failed copying file");
                    }
                } catch (Exception e) {
                    err.append("Error copying file(").append(e.getMessage()).append("): ").append(current.getValue()).append('\n');
                }
            } else {
                if (!current.getValue().mkdir()) {
                    err.append("Error making subdirectory: ").append(current.getValue()).append('\n');
                    continue;
                }

                var files = current.getKey().listFiles();
                if (files == null || files.length == 0) {
                    continue;
                }

                for (var f : files) {
                    var duplicate_file = new File(current.getValue(), f.getName());
                    que.add(new Pair<>(f, duplicate_file));
                }
            }
        }

        if (!err.isEmpty()) {
            throw new DuplicateFileException(err.toString());
        }
    }

    /**
     * Deletes a file and, if file is directory, all its contents. Continues even on fail and only reports error at the
     * end.
     *
     * @param file the file to delete
     * @throws DeleteFileException when some files fail to be deleted.
     */
    public static void deepDelete(File file) throws DeleteFileException {
        var err = new StringBuilder();
        var stack = new Stack<File>();
        stack.push(file);

        while (!stack.empty()) {
            var current = stack.pop();

            if (current.isFile()) {
                if (!current.delete()) {
                    err.append("Failed deleting file.").append(file).append('\n');
                }
                continue;
            }

            var subfiles = current.listFiles();
            if (subfiles == null || subfiles.length == 0) {
                if (!current.delete()) {
                    err.append("Failed deleting directory.").append(file).append('\n');
                }
                continue;
            }

            stack.push(current);
            Collections.addAll(stack, subfiles);
        }

        if (!err.isEmpty()) {
            throw new DeleteFileException(err.toString());
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
        if (!file.exists()) {
            return file;
        }
        var name = file.getName();
        String new_name;

        // if name has extension
        if (name.matches("^.*\\..+$")) {
            new_name = name.replaceAll("^(.*)(\\..+)$", "$1 copy$2");
        } else {
            new_name = name + " copy";
        }

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
        var name = file.getName();

        if (file.isDirectory()) {
            return "directory";
        }

        if (file.isHidden() || name.charAt(0) == '.') {
            return "hidden";
        }

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
    public static String optimizeSizeFormat(long byteSize) {
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
    public static String simplifyDateFormat(long epochTime) {
        LocalDate date = LocalDate.ofEpochDay(epochTime / 1000 / 60 / 60 / 24);
        return date.toString();
    }

    /**
     * Clears ISO time of T, Z and decimal part
     *
     * @param time the string iso time
     * @return simplified time
     */
    public static String simplifyDateTime(String time) {
        return time.replaceAll("((\\..*Z)|T)", " ");
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
