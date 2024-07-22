package com.example.client_fxml;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("server-conreg.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 800);
        ClientConRegMenuController controller = fxmlLoader.getController();
        controller.setStage(stage);
        stage.setTitle("Client Application");
        stage.setHeight(600);
        stage.setWidth(800);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}