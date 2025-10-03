package org.whoslv.frontend.tools;

import javafx.application.Application;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class ChooseDirectory extends Application {
    public String getDirectory(Stage stage) {
        final DirectoryChooser DC = new DirectoryChooser();
        DC.setTitle("Selecione uma pasta");

        File path = DC.showDialog(stage);

        if (path != null) {
            return path.getAbsolutePath();
        } else {
            return null;
        }
    }

    public String getDirectory(Stage stage, String titulo) {
        final DirectoryChooser DC = new DirectoryChooser();
        DC.setTitle(titulo);

        File path = DC.showDialog(stage);

        if (path != null) {
            return path.getAbsolutePath();
        } else {
            return null;
        }
    }

    @Override
    public void start(Stage stage) {}
}
