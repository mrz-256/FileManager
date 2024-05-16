package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.FUtil;
import com.example.filemanager.logic.commands.commands.FileCommand;
import com.example.filemanager.logic.exceptions.FileException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.Instant;
import java.util.Stack;

/**
 * Class for command history and command undoing
 */
public class CommandHistory {
    private static Stack<FileCommand> executedCommands;
    private static StringBuilder log;
    private static int logSize;
    private static final int MAX_LOG_SIZE = 10;
    private static final String LOG_FILE_PATH = "src/main/resources/data/logs.txt";

    /**
     * Initializes command history with start time as the first log.
     */
    public static void initialize() {
        log = new StringBuilder("##started : ").append(FUtil.simplifyDateTime(Instant.now().toString())).append("##\n");
        executedCommands = new Stack<>();
        logSize = 0;
    }

    /**
     * Adds new command to history
     *
     * @param command  command to add
     * @param undoable if the command can be undone
     */
    public static void addCommand(FileCommand command, boolean undoable) {
        addLog(command, false);

        if (undoable) {
            executedCommands.push(command);
        }

    }

    /**
     * Undoes last executed command that can be.
     *
     * @throws FileException when undo fails
     */
    public static void undoLast() throws FileException {
        if (executedCommands.empty()) {
            return;
        }

        FileCommand command = executedCommands.pop();
        addLog(command, true);

        command.undo();
    }

    /**
     * Writes about command to command log
     *
     * @param command the command to write about
     * @param undo    if command was 'undo' or 'do'
     */
    private static void addLog(FileCommand command, boolean undo) {
        logSize++;

        log.append(FUtil.simplifyDateTime(Instant.now().toString()));
        log.append(" : ").append((undo) ? "undo" : " do ");
        log.append(" : ").append(command.getID()).append('\n');

        if (logSize > MAX_LOG_SIZE) {
            flushToFile();
        }
    }

    /**
     * Flushes the current logs to the logfile.
     */
    public static void flushToFile() {
        var file = new File(LOG_FILE_PATH);

        try (var br = new BufferedWriter(new FileWriter(file, true))) {
            br.write(log.toString());
        } catch (Exception e) {
            System.out.println("E" + e.getMessage());
        }

        log = new StringBuilder();
        logSize = 0;
    }
}
