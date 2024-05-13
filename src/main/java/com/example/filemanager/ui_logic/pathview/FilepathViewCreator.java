package com.example.filemanager.ui_logic.pathview;

import com.example.filemanager.UIController;
import com.example.filemanager.UIUtil;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.File;

public class FilepathViewCreator {
    public static void filepathViewFillPath(File file) {
        var pane = UIController.getFilepathViewPane();
        var items = pane.getItems();
        items.clear();

        var current = file;

        do {
            var button = new Button(current.getName());
            button.setStyle(
                    "-fx-padding: 2"
            );

            File finalCurrent = current;
            button.setOnMouseClicked((x) -> {
                try {
                    UIController.setDirectoryInCurrentTab(finalCurrent);
                } catch (Exception ignored) {
                }
            });

            items.add(0, button);

            current = current.getParentFile();
        } while (current != null);

        pane.setOnMouseClicked((x) -> gotoPathButton());

    }

    public static void gotoPathButton() {
        var pane = UIController.getFilepathViewPane();
        var items = pane.getItems();
        items.clear();

        var tab = UIController.getInstance().getCurrentLogicalTab();

        var field = new TextField();
        items.add(field);

        field.setText(tab.getDirectory().getAbsolutePath());
        field.setPrefWidth(pane.getWidth() / 2);

        field.setOnMouseClicked((x) -> {
            double width = pane.getWidth();
            field.setMinWidth(width);
        });

        field.setOnAction((x) -> {
            var directory = new File(field.getText());

            if (!directory.exists() || !directory.isDirectory()) {
                return;
            }

            try {
                tab.setDirectory(directory);
            } catch (Exception e) {
                var alert = UIUtil.createAlert(Alert.AlertType.ERROR, "Failed moving to directory", e.getMessage());
                alert.show();
            }
        });
    }

}
