package org.whoslv.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.whoslv.frontend.MainApp;

public class GetNCMController {
    private MainApp main;

    public void setMain(MainApp main) { this.main = main; }

    private final Stage mainStage = MainApp.getMainStage();

    @FXML private TextField codigoBarrasField;
    @FXML private TextField ncmField;
    @FXML private Label ncmLabel;

    @FXML
    protected void buscarNCM() {
        String codigoBarras = codigoBarrasField.getText();

        String ncm = buscarNoBanco(codigoBarras); // resultado vindo da query do banco de dados

        if (ncm != null && !ncm.isEmpty()) {
            ncmLabel.setVisible(true);
            ncmField.setVisible(true);
            ncmField.setText(ncm);
        } else {
            ncmLabel.setVisible(false);
            ncmField.setVisible(false);
            PopupUtils.showAlert("Problemas ao buscar", "Não foi possível encontrar um NCM com o código de barras " + codigoBarrasField.getText(), mainStage);
        }
    }

    @FXML
    protected void backToLandpage() {
        main.gotoLandpage();
    }


    private String buscarNoBanco(String codigoBarras) {
        // Simulação (substitua pela lógica real)
        if ("7891234567890".equals(codigoBarras)) {
            return "1234.56.78";
        }
        return null;
    }
}
