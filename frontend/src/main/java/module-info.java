module org.whoslv.frontend {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens org.whoslv.frontend to javafx.fxml;
    exports org.whoslv.frontend;
}