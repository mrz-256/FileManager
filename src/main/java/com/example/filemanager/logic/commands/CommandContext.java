package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.LogicalConfig;
import com.example.filemanager.logic.LogicalTab;

import java.io.File;

/**
 * A data structure to hold data passed to commands.
 * Depending on specific command some may be set to null or otherwise ignored.
 *
 * @param directory the working directory used in the command
 * @param tab       the tab responsible for given command
 * @param config    the configuration of given command/tab
 * @param working   the files used as parameters for operations in command
 */
public record CommandContext(
        File directory,
        LogicalTab tab,
        LogicalConfig config,
        File[] working
) {
}
