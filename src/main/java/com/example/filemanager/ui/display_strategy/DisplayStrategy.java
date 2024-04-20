package com.example.filemanager.ui.display_strategy;

import com.example.filemanager.logic.LogicalTab;
import javafx.scene.control.Tab;

/**
 * A strategy to display icons in tab
 */
public interface DisplayStrategy {

    void display(Tab tab, LogicalTab logicalTab, int icon_size, int width);


}
