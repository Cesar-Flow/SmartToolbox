package org.whoslv.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.whoslv.frontend.MainApp;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LandpageController {
    private MainApp main;
    private Connection conn;

    public void setMain(MainApp main) {
        this.main = main;
    }
    public void setConnection(Connection conn) { this.conn = conn; }

    @FXML private ImageView ncmImage;
    @FXML private ImageView contabilizaImage;
    @FXML private ImageView guardImage;

    public void initialize() {
        final String ICONPATH = "/org/whoslv/frontend/icons/";
        String[] images = {
                "NCMNexus.png", "ContabilizaXML.png", "XMLGuard.png"
        };

        ImageView[] imgViews = { ncmImage, contabilizaImage, guardImage };

        for (int c = 0; c < images.length; c++) {
            InputStream is = getClass().getResourceAsStream(ICONPATH + images[c]);
            if (is == null) {
                throw new IllegalStateException("Imagem não encontrada: " + images[c]);
            }
            Image image = new Image(is);
            imgViews[c].setImage(image);
        }
    }


    @FXML Label helloText;

    @FXML
    private void onLogout() {
        quitLogged();
        main.gotoLogin();
    }

    private void quitLogged() {
        String sqlReset = "UPDATE sessions SET active = 0 WHERE active = 1";

        try (PreparedStatement resetStmt = conn.prepareStatement(sqlReset)) {
            resetStmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void autoLoginConfirm() {
        String loggedUser = getLoggedUser();
        helloText.setText(loggedUser);

        int autoLoginConfirm;
        String sql = """
        SELECT s.id, s.auto_login_confirm, s.auto_login
        FROM sessions s
        WHERE s.active = 1
        AND auto_login_confirm = 0
        LIMIT 1
    """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                int sessionId = rs.getInt("id");
                autoLoginConfirm = rs.getInt("auto_login_confirm");

                // Se ainda não foi oferecida a opção ao usuário
                if (autoLoginConfirm == 0) {
                    boolean response = PopupUtils.showConfirmation(
                            "Login automático",
                            "Ativar o login automático neste dispositivo?"
                    );

                    // Atualiza a sessão
                    String sqlUpdate = """
                    UPDATE sessions
                    SET auto_login_confirm = 1,
                        auto_login = ?,
                        active = ?
                    WHERE id = ?
                """;

                    try (PreparedStatement updateStmt = conn.prepareStatement(sqlUpdate)) {
                        if (response) {
                            updateStmt.setInt(1, 1); // auto_login = 1
                            updateStmt.setInt(2, 1); // mantém active = 1
                        } else {
                            updateStmt.setInt(1, 0); // auto_login = 0
                            updateStmt.setInt(2, 0); // desativa sessão
                        }
                        updateStmt.setInt(3, sessionId);
                        updateStmt.executeUpdate();
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }


    private String getLoggedUser() {
        String sql = "SELECT u.username FROM users u JOIN sessions s ON u.id = s.user_id WHERE s.active = 1 LIMIT 1";

        try (PreparedStatement pstmt = this.conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getString("username");
            }

        } catch (SQLException e) {
            System.err.println("Erro: " + e.getMessage());
        }

        return null;
    }


    // Abertura das páginas

    // Entrar no GetNCM
    @FXML
    protected void enterNCM() { main.gotoNCM(); }

    // Entrar no ContabilizaXML
    @FXML
    protected void enterContabiliza() { main.gotoContabiliza(); }

    // Entrar no XML Guard
    @FXML
    protected void enterGuard() { main.gotoGuard(); }
}
