package com.example.filemanager.logic.commands.commands;

import com.example.filemanager.logic.FUtil;
import com.example.filemanager.logic.LogicalConfiguration;
import com.example.filemanager.logic.commands.CommandContext;
import com.example.filemanager.logic.commands.CommandHistory;
import com.example.filemanager.logic.commands.FileCommandName;
import com.example.filemanager.logic.exceptions.FileException;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SearchCommand extends FileCommand {

    /**
     * Searches for all files in depth either from current directory or from home directory depending on `config`.
     * Files that are added must have a name matching provided pattern
     *
     * @return found files
     * @throws FileException never
     */
    @Override
    public ArrayList<File> execute(CommandContext context) throws FileException {
        CommandHistory.addCommand(this, false);
        var result = new ArrayList<File>();

        if (context.working() == null || context.working().length == 0) {
            //return empty list
            return result;
        }

        File toFind = context.working()[0];
        File start = context.directory();

        if (context.config().searchStart == LogicalConfiguration.SearchStart.SEARCH_FROM_HOME) {
            start = FUtil.getHomeDirectory();
        }


        Queue<File> que = new LinkedList<>();
        que.add(start);


        while (!que.isEmpty() && result.size() < context.config().maximalShownFiles) {
            var current = que.poll();

            // continue search deeper
            if (current.isDirectory()) {
                File[] files = current.listFiles();
                if (files == null) continue;

                que.addAll(List.of(files));
            }

            // add matching file
            else if (current.getName().matches(toFind.getName())) {
                result.add(current);
            }
        }

        return result;
    }

    @Override
    public FileCommandName getID() {
        return FileCommandName.SEARCH;
    }
}
