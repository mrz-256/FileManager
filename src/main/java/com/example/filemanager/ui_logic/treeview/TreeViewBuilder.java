package com.example.filemanager.ui_logic.treeview;

import com.example.filemanager.UIUtil;
import com.example.filemanager.logic.FUtil;
import com.example.filemanager.logic.sort_strategy.NameStrategy;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A class for building the tree view of file hierarchy.
 */
public class TreeViewBuilder {

    /**
     * Creates the tree-view for the first time and sets some other things.
     *
     * @param view the TreeView object
     */
    public static void treeViewInit(TreeView<TreeValue> view) {
        rebuildTreeView(view);

        view.setOnMouseClicked((x) -> {
            var current = view.getSelectionModel().getSelectedItem();
            if (current == null || current.getValue() == null) {
                return;
            }

            var file = current.getValue().file();

            UIUtil.tryMovingToDirectory(file);
        });
    }

    /**
     * Rebuilds the whole tree-view structure.
     *
     * @param view the view
     */
    public static void rebuildTreeView(TreeView<TreeValue> view) {
        var item_root = new TreeItem<TreeValue>();
        view.setRoot(item_root);

        // home directory
        if (System.getProperty("os.name").equals("Linux")) {
            item_root.setValue(new TreeValue(FUtil.getHomeDirectory()));
            treeViewAddLayer(item_root);

        // disks(?) for other systems
        } else {
            File[] drives = File.listRoots();
            for (var file : drives) {
                var item = TreeValue.makeItem(file);
                item_root.getChildren().add(item);
                treeViewAddLayer(item);
            }
        }

        item_root.setExpanded(true);
    }

    /**
     * Adds a layer of subdirectories under the selected item
     *
     * @param item the item to add subdirectories into
     */
    private static void treeViewAddLayer(TreeItem<TreeValue> item) {
        var directory = item.getValue().file();
        if (!directory.exists() || !directory.isDirectory()) {
            return;
        }

        var subdirs = directory.listFiles();
        if (subdirs == null) {
            return;
        }

        // get sorted directories
        ArrayList<File> list = new ArrayList<>(List.of(subdirs));
        list.removeIf(file -> {
            return !file.isDirectory() || file.isHidden();
        });
        new NameStrategy().sort(list);

        // for all subdirectories, add as a subitem
        for (var subdir : list) {
            var subitem = TreeValue.makeItem(subdir);
            item.getChildren().add(subitem);
        }

        // when item is for the first time expanded, prepare the next layer
        item.expandedProperty().addListener((x) -> {
            for (var child : item.getChildren()) {
                if (child.getChildren().isEmpty()) {
                    treeViewAddLayer(child);
                }
            }
        });
    }

}
