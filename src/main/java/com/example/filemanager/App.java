package com.example.filemanager;

import com.example.filemanager.logic.FileUtilFunctions;
import com.example.filemanager.logic.LogicalSplittableTab;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.ArrayList;

public class App {
    private static ArrayList<LogicalSplittableTab> tabs;



    public static void init() {
        tabs = new ArrayList<>();
    }

    public static void createNewTab(TabPane tabPane,File file){
        var logical_tab = new LogicalSplittableTab(file);
        tabs.add(logical_tab);

        Tab tab = new Tab();
        SplitPane splitPane = new SplitPane();

        tab.setContent(splitPane);
        tabPane.getTabs().add(tab);


    }


}
