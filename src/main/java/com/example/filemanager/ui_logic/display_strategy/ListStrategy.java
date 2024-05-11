package com.example.filemanager.ui_logic.display_strategy;


import com.example.filemanager.logic.FUtil;
import com.example.filemanager.logic.LogicalTab;
import com.example.filemanager.ui_logic.images.IconButtonCreator;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
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
        pane.setHgap(20);

        scrollPane.setMinSize(icon_size, icon_size);
        pane.getChildren().clear();

        ArrayList<File> files = logicalTab.getListedFiles();

        if (files.isEmpty()) {
            return;
        }

        pane.addRow(0, new Label(), new Label("name"), new Label("size"), new Label("last modification"));
        pane.getColumnConstraints().clear();
        pane.getColumnConstraints().addAll(
                new ColumnConstraints(icon_size / 2f),
                new ColumnConstraints(150),
                new ColumnConstraints(80)
        );

        for (int i = 0; i < files.size(); i++) {
            var file = files.get(i);

            Button button = IconButtonCreator.createIconButton(
                    file, logicalTab, icon_size / 2,
                    "-fx-background-color: transparent;" +
                            "-fx-content-display: left;" +
                            "-fx-border-color: rgba(128,128,128,0.13);"
            );


            pane.addRow(i + 1,
                    button,
                    new Label(file.getName()),
                    new Label(FUtil.optimalizeSizeFormat(file.length())),
                    new Label(FUtil.simplifyDateFormat(file.lastModified()))
            );
        }

    }

}
