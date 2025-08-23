package org.whoslv.frontend;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.whoslv.frontend.controllers.LoginController;
import org.whoslv.frontend.controllers.LandpageController;

import java.io.IOException;
import java.util.Objects;

// Classe para guardar Scene + Controller juntos
class SceneBundle<T> {
    private final Scene scene;
    private final T controller;

    public SceneBundle(Scene scene, T controller) {
        this.scene = scene;
        this.controller = controller;
    }

    public Scene getScene() {
        return scene;
    }

    public T getController() {
        return controller;
    }
}

public class MainApp extends javafx.application.Application {

    private static Stage mainStage;

    private Stage stage;
    private Scene loginScene;
    private Scene landpageScene;

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;

        // Carrega as scenes e controllers de forma enxuta
        SceneBundle<LoginController> login = loadScene("/org/whoslv/frontend/login.fxml", LoginController.class);
        SceneBundle<LandpageController> land = loadScene("/org/whoslv/frontend/landpage.fxml", LandpageController.class);

        // Injeta referência do MainApp nos controllers
        login.getController().setMain(this);
        land.getController().setMain(this);

        // Guarda as scenes
        loginScene = login.getScene();
        landpageScene = land.getScene();

        mainStage = primaryStage;
        stage.setTitle("Login");

        // FIXAR tamanho da janela e impedir redimensionamento
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setResizable(false);

        stage.setScene(loginScene);
        stage.show();
    }

    // Método genérico para carregar FXML + Scene + Controller
    private <T> SceneBundle<T> loadScene(String fxmlPath, Class<T> controllerClass) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        // Aplica CSS global de uma vez só
        String css = Objects.requireNonNull(
                getClass().getResource("/org/whoslv/frontend/css/style.css")
        ).toExternalForm();
        scene.getStylesheets().add(css);

        return new SceneBundle<>(scene, loader.getController());
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public void gotoLogin() {
        stage.setScene(loginScene);
    }

    public void gotoLandpage() {
        stage.setScene(landpageScene);
    }

    public static void main(String[] args) {
        launch();
    }
}
