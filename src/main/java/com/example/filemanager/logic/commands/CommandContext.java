package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.LogicalConfiguration;
import com.example.filemanager.logic.LogicalTab;

import java.io.File;

public record CommandContext(
        File directory,
        LogicalTab tab,
        LogicalConfiguration config,
        File[] working
) {
}
