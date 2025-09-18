package org.whoslv.frontend.controllers;

// import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.whoslv.frontend.MainApp;
import org.whoslv.frontend.database.PasswordUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LoginController {
    private MainApp main;
    private Connection conn;

    //private final Dotenv dotenv = Dotenv.load();
    private final Stage mainStage = MainApp.getMainStage();

    //private final String defaultUser = dotenv.get("DEFAULT_USER");
    // private final String masterPass = dotenv.get("MASTER_PASS");

    @FXML private TextField user;
    @FXML private PasswordField pass;

    public void setMain(MainApp main) {
        this.main = main;
    }
    public void setConnection(Connection conn) { this.conn = conn; }

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
            PopupUtils.showAlert("Campos Obrigatórios", "Os campos de usuário e senha são obrigatórios!", mainStage);
            return;
        }

        String sql = "SELECT password_hash FROM users WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getText());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String hashBanco = rs.getString("password_hash");
                if (PasswordUtils.checkPassword(pass.getText(), hashBanco)) {
                    updateLogged();
                    main.gotoLandpage();
                } else {
                    PopupUtils.showAlert("Problema de Autenticação", "Senha incorreta!", mainStage);
                    clean();
                }
            } else {
                PopupUtils.showAlert("Problema de Autenticação", "Usuário não encontrado!", mainStage);
                clean();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clean() {
        user.setText("");
        pass.setText("");
    }

    private void updateLogged() {
        String sqlReset = "UPDATE sessions SET active = 0 WHERE active = 1";
        String sqlSet = "INSERT INTO sessions (user_id, active) VALUES ((SELECT id FROM users WHERE username = ?), 1)";

        try (PreparedStatement resetStmt = conn.prepareStatement(sqlReset);
             PreparedStatement setStmt = conn.prepareStatement(sqlSet)) {

            // desativa todas as sessões ativas
            resetStmt.executeUpdate();

            // cria nova sessão ativa para o usuário atual
            setStmt.setString(1, user.getText());
            setStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}