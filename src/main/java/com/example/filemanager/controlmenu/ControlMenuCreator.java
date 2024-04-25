package com.example.filemanager.controlmenu;

import com.example.filemanager.UIController;
import com.example.filemanager.UIUtil;
import com.example.filemanager.controlmenu.rename.RenamePropertiesCreator;
import com.example.filemanager.logic.FileUtilFunctions;
import com.example.filemanager.logic.LogicalTab;
import com.example.filemanager.logic.exceptions.FileException;
import com.example.filemanager.controlmenu.properties.PropertiesPopUpCreator;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

import java.io.File;

/**
 * Creator of the menu opened by right-clicking on a file
 */
public class ControlMenuCreator {


    /**
     * Creates the menu
     *
     * @param logicalTab the tab with the file
     * @param file       the file for which the menu is being created
     * @return the new context menu
     */
    public static ContextMenu createControlContextMenu(LogicalTab logicalTab, File file) {
        var menu = new ContextMenu();
        fillControlMenu(menu, logicalTab, file);
        return menu;
    }

    /**
     * Fills the context menu with actual values
     *
     * @param menu       the menu
     * @param logicalTab the logical tab
     * @param file       the file
     */
    private static void fillControlMenu(ContextMenu menu, LogicalTab logicalTab, File file) {
        // all used menu items
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
            FileUtilFunctions.storeFileToClipboard(file);
        });

        copyPath.setOnAction((x) -> {
            FileUtilFunctions.storeTextToClipboard(file.getAbsolutePath());
        });
        //endregion

        //region affecting file
        cut.setOnAction((x) -> {
            FileUtilFunctions.storeFileToClipboard(file);

            try {
                logicalTab.executeCommand("delete_files", file);
            } catch (FileException e) {
                UIUtil.createAlert(Alert.AlertType.ERROR, "Can't cut file: " + file, e.getMessage());
            }

            UIController.updateCurrentTab();
        });

        delete.setOnAction((x) -> {
            try {
                logicalTab.executeCommand("delete_files", file);
            } catch (FileException e) {
                UIUtil.createAlert(Alert.AlertType.ERROR, "Can't delete file: " + file, e.getMessage());
            }
            UIController.updateCurrentTab();
        });

        duplicate.setOnAction((x) -> {
            try {
                logicalTab.executeCommand("paste_files", file);
            } catch (FileException e) {
                UIUtil.createAlert(Alert.AlertType.ERROR, "Can't duplicate file " + file, e.getMessage());
            }
            UIController.updateCurrentTab();
        });

        rename.setOnAction((x) -> {
            var popup = RenamePropertiesCreator.createRenamePropertiesPopUp(file);
            popup.show();
        });
        //endregion

        //region properties
        properties.setOnAction((x) -> {
            var popup = PropertiesPopUpCreator.createPropertiesPopUp(logicalTab, file);
            popup.show();
        });
        //endregion

    }

}
