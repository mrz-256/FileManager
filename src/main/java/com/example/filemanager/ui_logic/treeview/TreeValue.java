package com.example.filemanager.ui_logic.treeview;

import javafx.scene.control.TreeItem;

import java.io.File;

public record TreeValue(File file){
    @Override
    public String toString() {
        return file.getName();
    }

    public static TreeItem<TreeValue> makeItem(File file){
        return new TreeItem<>(new TreeValue(file));
    }
}
