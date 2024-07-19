module Server.App.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens org.example to javafx.fxml;
    exports org.example;
}