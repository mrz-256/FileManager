package com.example.filemanager.ui_logic.newdirectory;

import com.example.filemanager.UIController;
import com.example.filemanager.UIUtil;
import com.example.filemanager.logic.LogicalTab;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;


/**
 * Creator of new directory dialogue popup.
 */
public class NewDirectoryDialogueCreator {

    /**
     * Creates a 'new-directory-dialogue'
     *
     * @param tab the current tab
     * @return the dialogue
     */
    public static Stage createNewDialog(LogicalTab tab) {
        var contents = new GridPane();
        var scene = new Scene(contents);
        var stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("New directory..");

        var text = new TextField();
        var confirm = new Button("confirm");
        var info = new Label();
        contents.addRow(0, text, confirm);
        contents.addRow(1, info);

        text.setPromptText("directory name..");

        confirm.setOnMouseClicked((x) -> {
            var name = text.getText();
            if (name.isEmpty()) return;

            var file = new File(tab.getDirectory(), name);

            if (file.exists()) {
                info.setText("Such file already exists.");
                info.setStyle("-fx-border-color: #ff8989");
                return;
            }

            try {
                tab.executeCommand("new_directory", file);

                stage.close();
                UIController.updateCurrentTab();
            } catch (Exception e) {
                var alert = UIUtil.createAlert(Alert.AlertType.ERROR, "Failed creating file.", e.getMessage());
                alert.show();
            }
        });


        return stage;

    }

}
