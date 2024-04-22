package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.FileUtilFunctions;
import com.example.filemanager.logic.LogicalConfiguration;
import com.example.filemanager.logic.exceptions.FileException;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SearchCommand extends FileCommand{

    /**
     * Searches for all files in depth either from current directory or from home directory depending on `configuration`.
     * Files that are added must have a name matching ```".*" + working[0].getName() + ".*"``` pattern
     * @return list of all found files
     * @throws FileException never
     */
    @Override
    public ArrayList<File> execute(
            File directory, LogicalConfiguration configuration, File[] working
    )throws FileException {
        CommandHistory.addCommand(this, false);
        var result = new ArrayList<File>();

        if (working == null || working.length == 0) return null;

        File toFind = working[0];
        File start = directory;

        if (configuration.searchStart == LogicalConfiguration.SearchStart.SEARCH_FROM_HOME){
            start = FileUtilFunctions.getHomeDirectory();
        }


        Queue<File> que = new LinkedList<>();
        que.add(start);


        while (!que.isEmpty() && result.size() < 512){
            var current = que.poll();

            // continue search deeper
            if (current.isDirectory()){
                File[] files = current.listFiles();
                if (files == null) continue;

                que.addAll(List.of(files));
            }

            // add matching file
            else if (current.getName().matches(".*" + toFind.getName() + ".*")){
                result.add(current);
            }
        }

        return result;
    }

    @Override
    public String getID() {
        return "search";
    }
}
