package com.example.filemanager.ui_logic.images;

import com.example.filemanager.UIController;
import com.example.filemanager.logic.FUtil;
import com.example.filemanager.logic.LogicalTab;
import com.example.filemanager.logic.commands.FileCommandName;
import com.example.filemanager.logic.exceptions.FileException;
import com.example.filemanager.ui_logic.controlmenu.ControlMenuCreator;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;

import java.io.File;

import static com.example.filemanager.UIUtil.createAlert;

/**
 * A class which holds static functions to create a button with an icon and bind it with default onclick method
 */
public class IconButtonCreator {
    /**
     * The ratio between the whole button and the icon.
     */
    private static final double ICON_TO_BUTTON_RATIO = 3 / 5f;


    /**
     * Creates an icon button for a file icon.
     *
     * @param file       the file to make an icon button from
     * @param logicalTab the logical tab used for the onclick function
     * @param size       the size of the icon button
     * @param styleCSS   style of the icon button
     * @return newly created icon button
     */
    public static Button createIconButton(File file, LogicalTab logicalTab, int size, String styleCSS) {
        Button button = new Button();
        button.setPrefSize(size, size);
        button.setMinSize(size, size);

        button.setTooltip(new Tooltip(file.getAbsolutePath()));

        button.setStyle(styleCSS);

        try {
            button.setGraphic(IconButtonCreator.loadImageIcon(file, (int) (size * ICON_TO_BUTTON_RATIO)));
        } catch (FileException ignore) {
            // icon will be plain button with no icon
        }

        setOnFileClickFunction(button, logicalTab, file);

        return button;
    }

    /**
     * Used to load icon of a file.
     * Loads either the image if the filetype is png, jpg, bmp or a file icon in other cases.
     *
     * @param file the file to make an icon for
     * @param size the size of created icon
     * @return newly created icon
     * @throws FileException when file doesn't exist
     */
    private static ImageView loadImageIcon(File file, int size) throws FileException {
        if (!file.exists()) {
            throw new FileException("File doesn't exist - can't create icon", file);
        }

        String type = FUtil.getFileType(file);

        // types which can be used as the icon are loaded here
        if (type.equals("png")
                || type.equals("jpg")
                || type.equals("gif")
                || type.equals("bmp")
                || type.equals("jpeg")
        ) {
            var uri = file.toURI().toString();
            var image = ImageCache.getImage(uri, size);

            return new ImageView(image);
        }

        // if I have an icon drawn for given file type, it is loaded
        var resource = UIController.class.getResource("/icons/file_icons/icon_" + type + ".png");
        if (resource != null) {
            return new ImageView(resource.toExternalForm());
        }

        // default 'unknown' (?) icon is used otherwise
        resource = UIController.class.getResource("/icons/file_icons/icon_unknown.png");
        if (resource != null) {
            return new ImageView(resource.toExternalForm());
        }
        return null;
    }

    /**
     * Sets the onClick function so that it moves tab to given file if it's a directory or execute it if it's a file
     *
     * @param button     the button to bind the function to
     * @param logicalTab the logical tab of given tab
     * @param file       the file to use for action
     */
    private static void setOnFileClickFunction(Button button, LogicalTab logicalTab, File file) {
        button.setOnMouseClicked(mouseEvent -> {

            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (file.isDirectory()) {
                    try {
                        logicalTab.setDirectory(file);
                    } catch (FileException e) {
                        var alert = createAlert(Alert.AlertType.ERROR, "Failed moving to directory", e.getMessage());
                        alert.show();
                    }
                } else {
                    try {
                        logicalTab.executeCommand(FileCommandName.OPEN, file);
                    } catch (FileException e) {
                        var alert = createAlert(Alert.AlertType.ERROR, "Failed opening file", e.getMessage());
                        alert.show();
                    }
                }

            } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                var menu = ControlMenuCreator.createControlContextMenu(logicalTab, file);
                button.setContextMenu(menu);
            }

        });
    }
}
