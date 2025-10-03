package org.whoslv.frontend.controllers;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.whoslv.frontend.MainApp;

public class ContabilizaXMLController {
    private MainApp main;

    public void setMain(MainApp main) { this.main = main; }

    private final Stage MS = MainApp.getMainStage();

    @FXML
    protected void backToLandpage() { this.main.gotoLandpage(); }
}
