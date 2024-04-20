package com.example.filemanager;

import com.example.filemanager.logic.FileUtilFunctions;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

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
        UIUtil.createNewTab(tabPane, FileUtilFunctions.getHomeDirectory());
    }
}