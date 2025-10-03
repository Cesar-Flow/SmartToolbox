package org.whoslv.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import org.whoslv.frontend.MainApp;
import org.whoslv.frontend.tools.ChooseDirectory;
import org.whoslv.frontend.tools.XMLGuard.Nota;

import java.util.*;

public class XMLGuardController {
    private MainApp main;

    public void setMain(MainApp main) { this.main = main; }

    private final Stage MS = MainApp.getMainStage();

    @FXML private ListView<String> listDuplicadas;
    @FXML private ListView<String> listFaltando;
    @FXML private Label labelCaminho;

    @FXML
    public void initialize() {
        // Configura a tooltip das notas duplicadas
        listDuplicadas.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                    if (!empty) {
                        setTooltip(new Tooltip("Clique para copiar"));
                    }
                }
            };

            // Copia o número da nota clicada
            cell.setOnMouseClicked(event -> {
                if (!cell.isEmpty()) {
                    Clipboard clipboard = Clipboard.getSystemClipboard();
                    ClipboardContent content = new ClipboardContent();
                    content.putString(cell.getItem());
                    clipboard.setContent(content);

                    // Mostrando notificação
                    String msg = String.format("Nota número %s copiada!", cell.getText());
                    PopupUtils.showAlert("Texto copiado", msg, this.MS);
                }
            });
            return cell;
        });

        // Configura a tooltip das notas faltantes
        listFaltando.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                    if (!empty) {
                        setTooltip(new Tooltip("Clique para copiar"));
                    }
                }
            };

            // Copia o número da nota clicada
            cell.setOnMouseClicked(event -> {
                if (!cell.isEmpty()) {
                    Clipboard clipboard = Clipboard.getSystemClipboard();
                    ClipboardContent content = new ClipboardContent();
                    content.putString(cell.getItem());
                    clipboard.setContent(content);


                    // Mostrando notificação
                    String msg = String.format("Nota número %s copiada!", cell.getText());
                    PopupUtils.showAlert("Texto copiado", msg, this.MS);
                }
            });
            return cell;
        });
    }

    // Retorna para a tela inicial e limpa a pasta selecionada
    @FXML
    protected void backToLandpage() {
        this.labelCaminho.setText("Nenhuma pasta selecionada");

        this.listFaltando.getItems().clear(); // Limpando a lista
        this.listDuplicadas.getItems().clear(); // Limpando a lista

        this.main.gotoLandpage();
    }

    // Seleciona o caminho do diretório e busca os nomes dos arquivos para exibir na tela
    @FXML
    public void selecionarPasta() {
        // Caminho absoluto do diretório escolhido
        String path = (new ChooseDirectory()).getDirectory(this.MS);
        if (path != null) {
            labelCaminho.setText("📂 " + path);

            List<String> duplicados = new ArrayList<>();
            List<String> faltantes = new ArrayList<>();

            // Extrair os números das notas a partir dos nomes dos arquivos
            List<Nota> notasExtraidas = Nota.extrairNotasDoDiretorio(path);

            // Ordenar por série e número
            notasExtraidas.sort(Comparator.comparing(Nota::serie)
                    .thenComparing(Nota::numero));

            // Verificar quebras e duplicados
            Nota.verificarQuebrasEDuplicados(notasExtraidas, duplicados, faltantes);

            // Insere uma label caso as listas estejam vazias
            if (listDuplicadas.getItems().isEmpty()) { listDuplicadas.setPlaceholder(new Label("Sem notas duplicadas")); }
            if (listFaltando.getItems().isEmpty()) { listFaltando.setPlaceholder(new Label("Sem notas faltantes")); }

            // Mostra os valores em tela
            listDuplicadas.getItems().setAll(duplicados);
            listFaltando.getItems().setAll(faltantes);
        }
    }
}
