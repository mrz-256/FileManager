package com.example.filemanager.ui.display_strategy;

import com.example.filemanager.UIUtil;
import com.example.filemanager.logic.LogicalTab;
import com.example.filemanager.logic.exceptions.FileException;
import javafx.application.HostServices;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.io.IOException;
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
            Button button = createIcon(file, icon_size);
            setButtonOnClickFunction(button, tab, logicalTab, file);

            pane.add(button, position % hcells, position / hcells);
            position++;
        }

    }

    private Button createIcon(File file, int size) {
        Button button = new Button();
        button.setPrefSize(size, size);
        button.setMinSize(size, size);

        button.setText(file.getName());
        button.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-content-display: top;" +
                        "-fx-border-color: rgba(128,128,128,0.13);"
        );


        button.setTooltip(new Tooltip(file.getName()));

        try {
            button.setGraphic(UIUtil.loadImageIcon(file, size / 5 * 3));
        } catch (FileException ignore) {
            // icon will be plain button
        }

        return button;
    }

    private void setButtonOnClickFunction(Button button, Tab tab, LogicalTab logicalTab, File file) {
        button.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (file.isDirectory()) {
                    try {
                        logicalTab.setDirectory(file);
                        tab.setText(file.getName());
                        UIUtil.updateAllTabs();


                    } catch (FileException e) {
                        System.out.println("TODO: GRID STRATEGY ERROR " + e.getMessage());
                    }
                }
            }
        });
    }
}
