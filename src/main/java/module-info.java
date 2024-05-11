module com.example.filemanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.filemanager to javafx.fxml;
    exports com.example.filemanager.logic;
    exports com.example.filemanager.logic.sort_strategy;
    exports com.example.filemanager.logic.commands;
    exports com.example.filemanager.logic.exceptions;
    exports com.example.filemanager;
    exports com.example.filemanager.ui_logic.controlmenu.properties;
    opens com.example.filemanager.ui_logic.controlmenu.properties to javafx.fxml;
    exports com.example.filemanager.ui_logic.controlmenu;
    opens com.example.filemanager.ui_logic.controlmenu to javafx.fxml;
    exports com.example.filemanager.ui_logic.display_strategy;
    exports com.example.filemanager.logic.commands.commands;
}