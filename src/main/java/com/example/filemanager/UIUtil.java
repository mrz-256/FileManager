package com.example.filemanager;

import com.example.filemanager.logic.SplittableTab;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class UIUtil {
    private static LinkedList<SplittableTab> tabs;


    public static void init() {
        tabs = new LinkedList<>();
    }

    public static void createNewTab(TabPane tabPane, File file) {

        FXMLLoader loader = new FXMLLoader(UIUtil.class.getResource("tab-pane.fxml"));
        try {
            Tab tab = loader.load();
            tab.setText(file.getName());

            tabPane.getTabs().add(tab);

            SplittableTab logicalTab = new SplittableTab(tab, file);
            tabs.add(logicalTab);

        } catch (IOException e) {
            System.out.println("TODO: FAILED TAB CREATION - " + e.getMessage());
        }

    }


}
