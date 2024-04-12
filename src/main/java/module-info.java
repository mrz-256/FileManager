module com.example.filemanager {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.filemanager to javafx.fxml;
    exports com.example.filemanager;
    exports com.example.filemanager.ui;
    opens com.example.filemanager.ui to javafx.fxml;
}