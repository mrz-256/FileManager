package com.example.filemanager.ui_logic.display_strategy;

import com.example.filemanager.logic.LogicalTab;
import javafx.scene.control.Tab;

/**
 * A strategy to display icons in tab
 */
public interface DisplayStrategy {

    /**
     * Used to display listed files of given logical tab to the provided javafx tab.
     * @param tab the tab to which to put the javafx images
     * @param logicalTab the logical tab to display
     * @param icon_size the size of the icon
     * @param width the width of the tabpane, used for correct calculating of item placement
     */
    void display(Tab tab, LogicalTab logicalTab, int icon_size, int width);


}
