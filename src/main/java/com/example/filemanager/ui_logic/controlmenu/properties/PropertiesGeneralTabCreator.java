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
        int row = 0;

        tab.setText("General");
        tab.setContent(contents);
        tab.setClosable(false);
        pane.getTabs().add(tab);

        contents.getColumnConstraints().add(new ColumnConstraints(150));

        contents.addRow(row++, new Label("Name:"), new Label(file.getName()));
        contents.addRow(row++, new Label("Location:"), new Label(file.getParent()));
        contents.addRow(row++, new Label("Type:"), new Label(FUtil.getFileType(file)));
        if (file.isHidden()) {
            contents.addRow(row++, new Label("Visibility:"), new Label("hidden file"));
        }
        contents.addRow(row++, new Label());
        contents.addRow(row++, new Label("Size:"), new Label(FUtil.optimizeSizeFormat(file.length()) + " (" + file.length() + "B)"));
        contents.addRow(row++, new Label());

        BasicFileAttributes attr;
        try {
            attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        } catch (IOException e) {
            return;
        }

        contents.addRow(row++, new Label("Created:"), new Label(FUtil.simplifyDateTime(attr.creationTime().toString())));
        contents.addRow(row++, new Label("Modified:"), new Label(FUtil.simplifyDateTime(attr.lastModifiedTime().toString())));
        contents.addRow(row++, new Label("Accessed:"), new Label(FUtil.simplifyDateTime(attr.lastAccessTime().toString())));

        if (file.isDirectory()) {
            contents.addRow(row++, new Label());
            var files = file.listFiles();
            int nfiles = (files != null) ? files.length : 0;
            contents.addRow(row++, new Label("Contains:"), new Label(nfiles + " files."));
        }

    }
}
