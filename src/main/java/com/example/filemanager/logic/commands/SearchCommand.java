package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.Context;
import com.example.filemanager.logic.FileUtilFunctions;
import com.example.filemanager.logic.LogicalConfiguration;
import com.example.filemanager.logic.exceptions.FileException;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SearchCommand extends FileCommand{
    public SearchCommand(Context context) {
        super(context);
    }

    @Override
    public void execute() throws FileException {
        CommandHistory.addCommand(this, false);
        context.clearResult();

        File toFind = context.getWorkingAt(0);
        File start = context.getDirectory();

        if (context.getConfiguration().searchStart == LogicalConfiguration.SearchStart.SEARCH_FROM_HOME){
            start = FileUtilFunctions.getHomeDirectory();
        }


        Queue<File> que = new LinkedList<>();
        que.add(start);


        while (!que.isEmpty()){
            var current = que.poll();

            // continue search deeper
            if (current.isDirectory()){
                File[] files = current.listFiles();
                if (files == null) continue;

                que.addAll(List.of(files));
            }

            // add matching file
            else if (current.getName().matches(".*" + toFind.getName() + ".*")){
                context.addToResult(current);
            }

        }
    }

    @Override
    public String getID() {
        return "search";
    }
}
