module com.example.client_fxml {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
            requires com.dlsc.formsfx;
            requires net.synedra.validatorfx;
            requires org.kordamp.ikonli.javafx;
                
    opens com.example.client_fxml to javafx.fxml;
    exports com.example.client_fxml;
}