package org.whoslv.frontend.controllers;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.whoslv.frontend.MainApp;

public class LoginController {
    private MainApp main;

    private final Dotenv dotenv = Dotenv.load();
    private final Stage mainStage = MainApp.getMainStage();

    private final String defaultUser = dotenv.get("DEFAULT_USER");
    private final String masterPass = dotenv.get("MASTER_PASS");

    @FXML
    private TextField user;

    @FXML
    private PasswordField pass;

    public void setMain(MainApp main) {
        this.main = main;
    }

    @FXML
    public void initialize() {
        pass.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                onLogin();
            }
        });
    }

    @FXML
    protected void closeApp() {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    protected void onLogin() {
        if (user.getText().isEmpty() || pass.getText().isEmpty()) {
            this.showAlert("Campos Obrigatórios", "Os campos de usuário e senha são obrigatórios!", mainStage);
            return;
        }

        if (user.getText().equals(defaultUser) && pass.getText().equals(masterPass)) {
            this.clean();
            main.gotoLandpage();
        }else {
            this.showAlert("Credenciais inválidas", "As credenciais informadas são inválidas!", mainStage);
            this.clean();
        }
    }

    private void clean() {
        user.setText("");
        pass.setText("");
    }

    private void showAlert(String msg, String title) {
        Notifications.create()
                .title(msg)
                .text(title)
                .position(Pos.TOP_RIGHT)
                .hideAfter(Duration.seconds(3))
                .showWarning();
    }

    private void showAlert(String msg, String title, Stage stage) {
        Notifications.create()
                .title(title)
                .text(msg)
                .owner(stage)
                .position(Pos.TOP_CENTER)
                .hideAfter(Duration.seconds(3))
                .showWarning();
    }
}