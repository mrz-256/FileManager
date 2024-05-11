package com.example.filemanager.ui_logic.controlmenu.properties;

import com.example.filemanager.logic.FUtil;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

public class PropertiesGeneralTabCreator {

    /**
     * Adds a tab with general information to the properties popup.
     *
     * @param pane the tabpane
     * @param file the file whose properties are being checked
     */
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

        BasicFileAttributes attr;
        try {
            attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        } catch (IOException e) {
            return;
        }

        contents.addRow(2, new Label("Type:"), new Label(FUtil.getFileType(file)));
        contents.addRow(3, new Label());
        contents.addRow(4, new Label("Size:"), new Label(FUtil.optimizeSizeFormat(file.length()) + " (" + file.length() + "B)"));
        contents.addRow(5, new Label());
        contents.addRow(6, new Label("Created:"), new Label(FUtil.simplifyDateTime(attr.creationTime().toString())));
        contents.addRow(7, new Label("Modified:"), new Label(FUtil.simplifyDateTime(attr.lastModifiedTime().toString())));
        contents.addRow(8, new Label("Accessed:"), new Label(FUtil.simplifyDateTime(attr.lastAccessTime().toString())));

        if (file.isDirectory()) {
            contents.addRow(9, new Label());
            contents.addRow(10, new Label("Contains:"), new Label(Objects.requireNonNull(file.listFiles()).length + " files."));
        }

    }
}
