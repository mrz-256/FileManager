package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.LogicalConfig;
import com.example.filemanager.logic.LogicalTab;

import java.io.File;

public record CommandContext(
        File directory,
        LogicalTab tab,
        LogicalConfig config,
        File[] working
) {
}
