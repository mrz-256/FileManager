package com.example.filemanager.logic.commands;

import com.example.filemanager.logic.Context;
import com.example.filemanager.logic.FileUtilFunctions;
import com.example.filemanager.logic.LogicalConfiguration;
import com.example.filemanager.logic.exceptions.FileException;
import com.example.filemanager.logic.sort_strategy.NameStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ListDirectoriesCommandTest {

    @Test
    void execute() throws FileException {
        var command = new ListDirectoriesCommand();
        var result = command.execute(
                FileUtilFunctions.getHomeDirectory(),
                LogicalConfiguration.defaultConfiguration(),
                null
        );
        System.out.println("All directories: " + result);

        result = command.execute(
                FileUtilFunctions.getHomeDirectory(),
                new LogicalConfiguration(new NameStrategy(), true, "", LogicalConfiguration.SearchStart.SEARCH_FROM_HERE),
                null
        );
        System.out.println("Including hidden directories: " + result);
    }
}