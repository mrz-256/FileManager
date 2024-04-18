package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.Context;
import com.example.filemanager.logic.Logic;
import com.example.filemanager.logic.exceptions.FileException;
import com.example.filemanager.logic.exceptions.PasteFilesException;

import java.io.File;
import java.util.ArrayList;

/**
 *  A command to paste all files from context working[] into context directory.
 *  Stores them temporally so that action can be undone
 */
public class PasteFilesCommand extends FileCommand{
    private final ArrayList<File> copies;


    public PasteFilesCommand(Context context) {
        super(context);
        copies = new ArrayList<>();
    }


    @Override
    public void execute() throws FileException {
        CommandHistory.addCommand(this, true);
        context.clearResult();

        StringBuilder error = new StringBuilder();



        for (var file : context.getWorking()) {
            if (file == null) continue;

            File new_file = Logic.inventUniqueName(new File(context.getDirectory(), file.getName()));
            copies.add(new_file);

            if (!Logic.copyFile(file, new_file)){
                error.append("Failed copying file | ").append(file).append('\n');
            }
        }

        if (!error.isEmpty()){
            throw new PasteFilesException(error.toString());
        }

    }

    @Override
    public void undo() {
        for(var file : copies){
            if (file != null) file.delete();
        }
    }

    @Override
    public String getID() {
        return "paste_files";
    }
}
