module com.example.filemanager {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.filemanager to javafx.fxml;
    exports com.example.filemanager.logic;
    exports com.example.filemanager.logic.sort_strategy;
    exports com.example.filemanager.logic.commands;
    exports com.example.filemanager.logic.exceptions;
    exports com.example.filemanager;
}