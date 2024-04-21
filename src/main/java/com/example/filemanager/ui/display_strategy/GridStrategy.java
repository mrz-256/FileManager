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

public class GridStrategy implements DisplayStrategy {

    @Override
    public void display(Tab tab, LogicalTab logicalTab, int icon_size, int width) {
        if (width == 0) return;

        int default_gap = 10;
        int hcells = width / (icon_size + default_gap);
        double gap = (width - (hcells * icon_size)) / ((double) hcells);

        if (hcells == 0) return;


        ScrollPane scrollPane = (ScrollPane) tab.getContent();
        GridPane pane = (GridPane) scrollPane.getContent();
        pane.setHgap(gap);
        pane.setVgap(gap);

        scrollPane.setMinSize(icon_size, icon_size);
        pane.getChildren().clear();

        ArrayList<File> files;

        try {
            files = logicalTab.getFilesToList();
        } catch (FileException e) {
            // todo:
            System.out.println("TODO: GRID STRATEGY ERROR " + e.getMessage());
            return;
        }

        int position = 0;
        for (var file : files) {
            Button button = UIUtil.createIconButton(
                    file, icon_size,
                    "-fx-background-color: transparent;" +
                    "-fx-content-display: top;" +
                    "-fx-border-color: rgba(128,128,128,0.13);"
            );
            UIUtil.setOnFileClickFunction(button, logicalTab, file);

            pane.add(button, position % hcells, position / hcells);
            position++;
        }

    }


}
