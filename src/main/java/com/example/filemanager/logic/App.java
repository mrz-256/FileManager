package com.example.filemanager.logic;

import javafx.fxml.FXML;

import java.io.File;
import java.util.ArrayList;

public class App {
    ArrayList<SplittableTab> tabs;



    public App() {
        this.tabs = new ArrayList<>();
        var home_tab = new SplittableTab(FileUtilFunctions.getHomeDirectory());
        tabs.add(home_tab);
    }


}
