package com.example.filemanager.ui_logic.treeview;

import com.example.filemanager.logic.FUtil;
import com.example.filemanager.logic.sort_strategy.NameStrategy;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TreeViewBuilder {

    public static void buildTreeView(TreeView<TreeValue> view) {
        var item_root = new TreeItem<TreeValue>();
        view.setRoot(item_root);

        if (System.getProperty("os.name").equals("Linux")) {
            item_root.setValue(new TreeValue(FUtil.getHomeDirectory()));
            treeViewAddLayer(item_root);

        } else {
            for (char c = 'A'; c <= 'Z'; c++) {
                var file = new File(c + ":");
                if (!file.exists() || !file.isDirectory()){
                    break;
                }

                var item = TreeValue.makeItem(file);
                item_root.getChildren().add(item);
                treeViewAddLayer(item);
            }
        }

        item_root.setExpanded(true);
    }

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
            for(var child : item.getChildren()){
                if (child.getChildren().isEmpty()){
                    treeViewAddLayer(child);
                }
            }
        });
    }

}
