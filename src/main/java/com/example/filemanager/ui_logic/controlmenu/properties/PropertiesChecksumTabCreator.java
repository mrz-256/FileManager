package com.example.filemanager.ui_logic.controlmenu.properties;

import com.example.filemanager.logic.FUtil;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;


public class PropertiesChecksumTabCreator {

    /**
     * Adds a tab with checksums to the properties popup.
     *
     * @param pane the tabpane
     * @param file the file whose properties are being checked
     */
    static void addChecksumTab(TabPane pane, File file) {
        // tab won't be added at all
        if (file.isDirectory() || file.length() == 0) return;

        var contents = new VBox();
        var grid = new GridPane();
        var tab = new Tab();
        grid.getColumnConstraints().add(new ColumnConstraints(75));
        tab.setContent(contents);
        tab.setClosable(false);
        tab.setText("Checksum");
        pane.getTabs().add(tab);

        //region match check
        var field = new TextField();
        var matchInfo = new Label();

        contents.getChildren().addAll(field, matchInfo);

        field.setPromptText("MD5 or SHA1 or SHA256 or SHA 512..");
        field.setOnAction((x) -> {
            var text = field.getText();
            String type = switch (text.length()) {
                case 32 -> "MD5";
                case 40 -> "SHA1";
                case 64 -> "SHA256";
                case 128 -> "SHA512";
                default -> "";
            };

            if (type.isEmpty()) {
                matchInfo.setText("incomplete checksum..");
                matchInfo.setStyle("-fx-border-color: grey");
                return;
            }

            if (text.equals(getChecksum(type, file))) {
                matchInfo.setText("Checksums do match.");
                matchInfo.setStyle("-fx-border-color: #63ff63");
            } else {
                matchInfo.setText("Checksums do NOT match.");
                matchInfo.setStyle("-fx-border-color: #ff8282");
            }
        });
        //endregion

        //region individual checksums
        contents.getChildren().add(grid);
        addCheckSum("MD5", file, 2, grid);
        addCheckSum("SHA1", file, 3, grid);
        addCheckSum("SHA256", file, 4, grid);
        addCheckSum("SHA512", file, 5, grid);
        //endregion

    }


    /**
     * Adds a row with checksum
     *
     * @param type     the type of the algorithm
     * @param file     the file to check
     * @param row      the row to add to the contents
     * @param contents the place where the row gets added
     */
    private static void addCheckSum(String type, File file, int row, GridPane contents) {
        var calculate = new Button("calculate");
        var label = new Label("");

        contents.add(new Label(type + ":"), 0, row);
        contents.add(label, 1, row);
        contents.add(calculate, 3, row);


        calculate.setOnMouseClicked((x) -> {
            calculate.setDisable(true);

            var checksum = getChecksum(type, file);
            label.setText(checksum.substring(0, 8) + "...  ");
            label.setTooltip(new Tooltip(checksum));

            var copy = new Button("copy");
            contents.add(copy, 2, row);

            copy.setOnMouseClicked((y) -> FUtil.storeTextToClipboard(checksum));

        });
    }

    /**
     * Returns checksum of a file by given type of algorithm in hexadecimal
     *
     * @param type the algorithm
     * @param file the file
     * @return the checksum or fail message
     */
    private static String getChecksum(String type, File file) {
        try {
            byte[] data = Files.readAllBytes(file.toPath());
            byte[] hash = MessageDigest.getInstance(type).digest(data);
            return new BigInteger(1, hash).toString(16);
        } catch (Exception e) {
            return "failed calculation";
        }
    }
}
