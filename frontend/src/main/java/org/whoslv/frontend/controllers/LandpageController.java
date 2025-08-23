package org.whoslv.frontend.controllers;

import javafx.fxml.FXML;
import org.whoslv.frontend.MainApp;

public class LandpageController {
    private MainApp main;

    public void setMain(MainApp main) {
        this.main = main;
    }

    @FXML
    private void gotoLogin() {
        main.gotoLogin();
    }
}
