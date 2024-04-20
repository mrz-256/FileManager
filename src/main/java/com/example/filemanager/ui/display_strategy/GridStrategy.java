package com.example.filemanager.ui.display_strategy;

import com.example.filemanager.UIUtil;
import com.example.filemanager.logic.LogicalTab;
import com.example.filemanager.logic.exceptions.FileException;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;

import java.io.File;
import java.util.ArrayList;

public class GridStrategy implements DisplayStrategy {

    @Override
    public void display(Tab tab, LogicalTab logicalTab, int icon_size, int width) {
        if (width == 0) return;

        int default_gap = 10;
        int hcells = width / (icon_size + default_gap);
        double gap = (width - (hcells*icon_size)) / ((double)hcells);

        if (hcells == 0) return;

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setMinSize(icon_size, icon_size);
        GridPane pane = new GridPane();

        pane.setHgap(gap);
        pane.setVgap(gap);

        scrollPane.setContent(pane);
        tab.setContent(scrollPane);


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
            pane.add(createIcon(file, icon_size), position % hcells, position / hcells);
            position++;
        }

    }

    private Button createIcon(File file, int size) {
        Button button = new Button();
        button.setText(file.getName());
        button.setPrefSize(size, size);
        button.setMinSize(size, size);
        button.setTextAlignment(TextAlignment.CENTER);
        button.setTooltip(new Tooltip(file.getName()));
        button.setStyle("-fx-background-color: transparent;");

        try {
            //ImageView imageView = new ImageView();
            //Image image = UIUtil.loadImageIcon(file, size);

            //imageView.setImage(image);
            button.setGraphic(UIUtil.loadImageIcon(file, size / 4 * 3));
        } catch (FileException ignore) {
            // icon will be plain button
        }

        return button;
    }

}
