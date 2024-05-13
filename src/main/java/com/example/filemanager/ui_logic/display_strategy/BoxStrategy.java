package com.example.filemanager.ui_logic.display_strategy;

import com.example.filemanager.logic.LogicalTab;
import com.example.filemanager.ui_logic.buttons.IconButtonCreator;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.util.ArrayList;

public class BoxStrategy implements DisplayStrategy {

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
        pane.getColumnConstraints().clear();
        pane.getChildren().clear();

        ArrayList<File> files = logicalTab.getListedFiles();

        if (files.isEmpty()) {
            return;
        }

        int position = 0;
        for (var file : files) {
            Button button = IconButtonCreator.createIconButton(
                    file, logicalTab, icon_size,
                    "-fx-background-color: transparent;" +
                            "-fx-content-display: top;" +
                            "-fx-border-color: rgba(128,128,128,0.13);"
            );
            button.setText(file.getName());

            pane.add(button, position % hcells, position / hcells);
            position++;
        }

    }


}
