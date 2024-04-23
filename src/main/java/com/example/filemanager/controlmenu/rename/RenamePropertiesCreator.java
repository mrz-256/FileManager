package com.example.filemanager.controlmenu.rename;

import com.example.filemanager.UIController;
import com.example.filemanager.logic.LogicalTab;
import com.example.filemanager.logic.commands.RenameFileCommand;
import com.example.filemanager.logic.exceptions.FileException;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class RenamePropertiesCreator {

    public static Stage createRenamePropertiesPopUp(File file){
        var contents = new VBox();
        var scene = new Scene(contents);
        var stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Rename file \"" + file.getName() + "\" to..");

        contents.getChildren().add(new Label("originally: " + file.getName()));

        var field = new TextField();
        var feedback = new Label();
        var confirm = new Button();

        contents.getChildren().addAll(field, feedback, confirm);

        field.setPromptText("new name..");
        confirm.setText("confirm");

        confirm.setOnMouseClicked((x) -> {
            feedback.setStyle("-fx-border-color: #ff8989");

            var text = field.getText();
            if (text.isEmpty()){
                feedback.setText("Invalid filename.");
                return;
            }

            var new_file = new File(file.getParent(), text);

            try {
                RenameFileCommand command = new RenameFileCommand();
                command.execute(null, null, new File[]{file, new_file});
                feedback.setStyle("-fx-border-color: #b0ff89");
                feedback.setText("Success.");

                stage.close();
            } catch (FileException e){
                feedback.setText(e.getMessage().replaceAll("\\|.*", ""));
            }
            UIController.updateCurrentTab();

        });


        return stage;
    }
}
