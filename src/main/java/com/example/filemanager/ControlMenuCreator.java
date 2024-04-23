package com.example.filemanager;

import com.example.filemanager.logic.LogicalTab;
import com.example.filemanager.logic.exceptions.FileException;
import com.example.filemanager.properties.PropertiesPopUpCreator;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.io.File;
import java.util.List;

public class ControlMenuCreator {


    public static ContextMenu createControlContextMenu(LogicalTab logicalTab, File file) {
        var menu = new ContextMenu();
        fillControlMenu(menu, logicalTab, file);
        return menu;
    }

    private static void fillControlMenu(ContextMenu menu, LogicalTab logicalTab, File file) {
        var copy = new MenuItem("copy");
        var copyPath = new MenuItem("copy path");
        var cut = new MenuItem("cut");
        var delete = new MenuItem("delete");
        var duplicate = new MenuItem("duplicate");
        var rename = new MenuItem("rename");
        var properties = new MenuItem("properties");

        menu.getItems().addAll(
                copy, copyPath,
                new SeparatorMenuItem(), cut, delete, duplicate, rename,
                new SeparatorMenuItem(), properties
        );

        //region action copy
        copy.setOnAction((x) -> {
            var clipboard = Clipboard.getSystemClipboard();
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putFiles(List.of(file));
            clipboard.setContent(clipboardContent);
        });

        copyPath.setOnAction((x) -> {
            var clipboard = Clipboard.getSystemClipboard();
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString(file.getAbsolutePath());
            clipboard.setContent(clipboardContent);
        });
        //endregion

        //region affecting file
        cut.setOnAction((x) -> {
            var clipboard = Clipboard.getSystemClipboard();
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putFiles(List.of(file));
            clipboard.setContent(clipboardContent);

            try {
                logicalTab.executeCommand("delete_files", file);
            } catch (FileException e) {
                // todo:
                System.out.println(e.getMessage());
            }

            UIController.updateCurrentTab();
        });

        delete.setOnAction((x) -> {
            try {
                logicalTab.executeCommand("delete_files", file);
            } catch (FileException e) {
                // todo:
                System.out.println(e.getMessage());
            }
            UIController.updateCurrentTab();
        });

        duplicate.setOnAction((x) -> {
            try {
                logicalTab.executeCommand("paste_files", file);
            } catch (FileException e) {
                //todo:
                System.out.println(e.getMessage());
            }
            UIController.updateCurrentTab();
        });
        //endregion

        rename.setOnAction((x) -> {
            // todo: this requires popup window
        });

        properties.setOnAction((x) -> {
            var popup = PropertiesPopUpCreator.createPropertiesPopUp(logicalTab, file);
            popup.show();
        });

    }

}