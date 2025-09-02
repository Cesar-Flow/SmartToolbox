package org.whoslv.frontend;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.whoslv.frontend.controllers.LoginController;
import org.whoslv.frontend.controllers.LandpageController;
import org.whoslv.frontend.database.Connect;
import org.whoslv.frontend.database.Create;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    // Scenes
    private Stage stage;
    private Scene loginScene;
    private Scene landpageScene;

    // Controllers
    private LoginController loginController;
    private LandpageController landpageController;

    private final Connection conn;

    public MainApp() throws SQLException {
        conn = Connect.getConn();
    }

    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {
        // Conexão com o banco
        if (conn != null) {
            System.out.println("Banco pronto para uso!");
        }
        Create.createDatabase(conn);

        stage = primaryStage;

        // Carrega login
        SceneBundle<LoginController> login = loadScene("/org/whoslv/frontend/fxml/login.fxml", LoginController.class);
        loginScene = login.getScene();
        loginController = login.getController();
        loginController.setMain(this);
        loginController.setConnection(conn);

        // Carrega landpage
        SceneBundle<LandpageController> land = loadScene("/org/whoslv/frontend/fxml/landpage.fxml", LandpageController.class);
        landpageScene = land.getScene();
        landpageController = land.getController();
        landpageController.setMain(this);
        landpageController.setConnection(conn);

        mainStage = primaryStage;
        stage.setTitle("Login");

        // FIXAR tamanho da janela e impedir redimensionamento
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setResizable(false);

        // Mostra login
        //stage.setScene(loginScene);

        Integer loggedUserId = getLoggedUserId();
        if (loggedUserId != null) {
            // Auto login
            landpageController.autoLoginConfirm(); // ou outro método que inicialize a landpage
            stage.setScene(landpageScene);
        } else {
            // Mostra login normalmente
            stage.setScene(loginScene);
        }

        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
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

    // Troca para login
    public void gotoLogin() {
        stage.setScene(loginScene);
        // opcional: método automático do loginController
        // loginController.metodoAutomatico();
    }

    // Troca para landpage e dispara método automático
    public void gotoLandpage() {
        stage.setScene(landpageScene);
        // dispara método automático do controller assim que a scene é definida
        if (landpageController != null) {
            landpageController.autoLoginConfirm();
        }
    }

    private Integer getLoggedUserId() {
        String sql = "SELECT u.id, u.username FROM users u JOIN sessions s ON u.id = s.user_id WHERE s.active = 1 LIMIT 1;\n";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // nenhum usuário logado
    }


    public static void main(String[] args) {
        launch();
    }
}
