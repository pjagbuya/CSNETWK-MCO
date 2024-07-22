package com.example.client_fxml;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientPopUpMenuController {
    private Socket clientEndpoint;

    private Stage myStage;

    private Scene targetNextScene;

    private Stage stage;

    @FXML
    private Button okCloseBtn;


    public void setMyStage(Stage myStage) {
        this.myStage = myStage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;

    }

    public void setTargetNextScene(Scene targetNextScene) {
        this.targetNextScene = targetNextScene;
    }

    @FXML
    protected void closeWindow(){
        Stage stage = (Stage)(okCloseBtn.getScene().getWindow());
        stage.close();

    }


    @FXML
    protected void setToNextSceneTrigger(){
        this.stage.setScene(this.targetNextScene);
        this.myStage.close();

    }









}