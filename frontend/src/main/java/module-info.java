module org.whoslv.frontend {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.dotenv;

    opens org.whoslv.frontend to javafx.fxml;
    exports org.whoslv.frontend;
    exports org.whoslv.frontend.controllers;
    opens org.whoslv.frontend.controllers to javafx.fxml;
}