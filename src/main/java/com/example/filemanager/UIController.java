package com.example.filemanager;

import com.example.filemanager.logic.FileUtilFunctions;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

import java.io.File;

public class UIController {
    @FXML
    private VBox places;
    @FXML
    private VBox recent;
    @FXML
    private TabPane tabPane;

    @FXML
    void initialize(){
        UIUtil.init();
        UIUtil.createNewTab(tabPane, new File(FileUtilFunctions.getHomeDirectory(), "Pictures"));
        tabPane.widthProperty().addListener((observableValue, number, newNumber) -> {
            UIUtil.updateAllTabs(newNumber.intValue());
        });
    }
}