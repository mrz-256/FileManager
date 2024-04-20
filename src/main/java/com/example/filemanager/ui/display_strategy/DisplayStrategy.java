package com.example.filemanager.ui.display_strategy;

import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;

import java.io.File;

public interface DisplayStrategy {

    void display(SplitPane tab, File[] files);

}
