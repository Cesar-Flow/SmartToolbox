package org.whoslv.frontend.controllers;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.util.Optional;

public class PopupUtils {

    public static boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null); // sem cabeçalho
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    public void showAlert(String msg, String title) {
        Notifications.create()
                .title(msg)
                .text(title)
                .position(Pos.TOP_RIGHT)
                .hideAfter(Duration.seconds(3))
                .showWarning();
    }

    public void showAlert(String msg, String title, Stage stage) {
        Notifications.create()
                .title(title)
                .text(msg)
                .owner(stage)
                .position(Pos.TOP_CENTER)
                .hideAfter(Duration.seconds(3))
                .showWarning();
    }
}
