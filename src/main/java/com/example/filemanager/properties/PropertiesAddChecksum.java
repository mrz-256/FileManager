package com.example.filemanager.properties;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;


public class PropertiesAddChecksum {

    /**
     * Adds a tab with checksums to the properties popup.
     * @param pane the tabpane
     * @param file the file whose properties are being checked
     */
    static void addChecksumTab(TabPane pane, File file) {
        // tab won't be added at all
        if (file.isDirectory() || file.length() == 0) return;

        var contents = new GridPane();
        var tab = new Tab();
        tab.setText("Checksum");
        tab.setContent(contents);
        tab.setClosable(false);
        pane.getTabs().add(tab);

        contents.getColumnConstraints().add(new ColumnConstraints(150));

        addCheckSum("MD5", file, 1, contents);
        addCheckSum("SHA1", file, 2, contents);
        addCheckSum("SHA256", file, 3, contents);
        addCheckSum("SHA512", file, 4, contents);

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
        contents.add(new Label(type + ":"), 0, row);
        var calculate = new Button("calculate");
        contents.add(calculate, 3, row);

        calculate.setOnMouseClicked((x) -> {
            var checksum = getChecksum(type, file);

            contents.add(new Label(checksum), 1, row);

            var copy = new Button("copy");
            contents.add(copy, 2, row);
            copy.setOnMouseClicked((y) -> {
                var clipboard = Clipboard.getSystemClipboard();
                var clipboardContent = new ClipboardContent();
                clipboardContent.putString(checksum);
                clipboard.setContent(clipboardContent);
            });
            calculate.setDisable(true);
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
