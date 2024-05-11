package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.commands.commands.FileCommand;

import java.util.Date;
import java.util.Stack;

/**
 * Class for command history and command undoing
 */
public class CommandHistory {
    private static CommandHistory instance;
    private final Stack<FileCommand> executedCommands;
    private final StringBuilder log;



    private CommandHistory() {
        this.executedCommands = new Stack<>();
        this.log = new StringBuilder("##started : ").append(new Date().getTime()).append("##\n");
    }

    public static CommandHistory getInstance(){
        if (instance == null) instance = new CommandHistory();

        return instance;
    }


    /**
     * Adds new command to history
     * @param command command to add
     * @param undoable if the command can be undone
     */
    public static void addCommand(FileCommand command, boolean undoable){
        addLog(command, false);

        if (undoable){
            getInstance().executedCommands.push(command);
        }

    }

    /**
     * Writes about command to command log
     * @param command the command to write about
     * @param undo if command was 'undo' or 'do'
     */
    private static void addLog(FileCommand command, boolean undo){
        var log = getInstance().log;

        log.append("# ").append(new Date().getTime());
        log.append(" : ").append((undo)?"undo":"do");
        log.append(" : ").append(command.getID()).append('\n');
    }

    /**
     * Undoes last executed command that can be.
     */
    public static void undoLast(){
        if (getInstance().executedCommands.empty()) return;

        FileCommand command = getInstance().executedCommands.pop();
        addLog(command, true);

        command.undo();
    }

    /**
     * @return string representation of whole command history
     */
    @Override
    public String toString() {
        return log.toString();
    }
}
