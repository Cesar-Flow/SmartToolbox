package org.whoslv.tools.XMLProcessor;

import javax.swing.*;
import java.io.File;

public class VisualPath {
    String path = null;

    // public VisualPath() {}

    public String getPath() {
        return this.path;
    }

    public String setPath() {
        // use "var" no lugar de String caso dê erro de tipagem (improvavel)
        String response = this.askPath();

        if (!(response == null)) {
            this.path = response;
        }
        return response;
    }

    // Sobrepondo métodos
    public void setPath(String newPath) {
        this.path = newPath;
    }

    public String askPath() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // só pastas
        chooser.setDialogTitle("Selecione uma pasta");

        int response = chooser.showOpenDialog(null);

        if (response == JFileChooser.APPROVE_OPTION) {
            File pasta = chooser.getSelectedFile();
            return pasta.getAbsolutePath();
        } else {
            return null;
        }
    }
}
