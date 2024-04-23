package com.example.filemanager.properties;

import com.example.filemanager.logic.FileUtilFunctions;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Objects;

public class PropertiesAddGeneral {

    static void addGeneralTab(TabPane pane, File file) {
        var contents = new GridPane();
        var tab = new Tab();
        tab.setText("General");
        tab.setContent(contents);
        tab.setClosable(false);
        pane.getTabs().add(tab);

        contents.getColumnConstraints().add(new ColumnConstraints(150));

        contents.addRow(0, new Label("Name:"), new Label(file.getName()));
        contents.addRow(1, new Label("Location:"), new Label(file.getParent()));

        BasicFileAttributes attributes;
        try {
            attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        } catch (IOException e) {
            return;
        }

        contents.addRow(2, new Label("Type:"), new Label(FileUtilFunctions.getFileType(file)));
        contents.addRow(3, new Label());
        contents.addRow(4, new Label("Size:"), new Label(FileUtilFunctions.getOptimalSizeFormat(file.length()) + " (" + file.length() + "B)"));
        contents.addRow(5, new Label());
        contents.addRow(6, new Label("Created:"), new Label(getMoreReadableTime(attributes.creationTime())));
        contents.addRow(7, new Label("Modified:"), new Label(getMoreReadableTime(attributes.lastModifiedTime())));
        contents.addRow(8, new Label("Accessed:"), new Label(getMoreReadableTime(attributes.lastAccessTime())));

        if (file.isDirectory()) {
            contents.addRow(9, new Label());
            contents.addRow(10, new Label("Contains:"), new Label(Objects.requireNonNull(file.listFiles()).length + " files."));
        }

    }

    /**
     * Returns iso time without the T and the fraction part of a second
     *
     * @param time the time
     * @return string of the time in more readable form
     */
    private static String getMoreReadableTime(FileTime time) {
        return time.toString().replaceAll("((\\..*Z)|T)", " ");
    }
}
