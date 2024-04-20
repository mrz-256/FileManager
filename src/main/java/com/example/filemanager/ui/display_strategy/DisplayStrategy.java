package com.example.filemanager.ui.display_strategy;

import javafx.scene.control.Tab;

import java.io.File;

public interface DisplayStrategy {

    void display(Tab tab, File[] files);

}
