package com.example.filemanager.ui.display_strategy;


import com.example.filemanager.UIController;
import com.example.filemanager.UIUtil;
import com.example.filemanager.logic.LogicalTab;
import com.example.filemanager.logic.exceptions.FileException;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.util.ArrayList;

public class ListStrategy implements DisplayStrategy {

    @Override
    public void display(Tab tab, LogicalTab logicalTab, int icon_size, int width) {
        if (width == 0) return;


        ScrollPane scrollPane = (ScrollPane) tab.getContent();
        GridPane pane = (GridPane) scrollPane.getContent();
        pane.setVgap(10);

        scrollPane.setMinSize(icon_size, icon_size);
        pane.getChildren().clear();

        ArrayList<File> files;

        try {
            files = logicalTab.getFilesToList();
        } catch (FileException e) {
            // todo:
            System.out.println("TODO: LIST STRATEGY ERROR " + e.getMessage());
            return;
        }

        pane.addRow(0, new Label("name"), new Label("size"), new Label("last modification"));

        for (int row=1; row < files.size(); row++) {
            var file = files.get(row);

            Button button = UIUtil.createIconButton(
                    file, icon_size,
                    "-fx-background-color: transparent;" +
                            "-fx-content-display: top;" +
                            "-fx-border-color: rgba(128,128,128,0.13);"
            );
            UIUtil.setOnFileClickFunction(button, logicalTab, file);


            pane.addRow(row, button, new Label(file.length() + " B"), new Label(file.lastModified() + ""));
        }

    }

}
