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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientConRegMenuController {
    @FXML
    private Button helpBtn;

    @FXML
    private Label serverStatusTxt;

    @FXML
    private TextField serverField;

    @FXML
    private TextField portField;

    @FXML
    private Button okCloseBtn;

    @FXML
    private TextField userField;

    @FXML
    protected void onConnectButtonClick() throws IOException  {
        String server = serverField.getText();
        String port = portField.getText();

        if(server.length()==0 || port.length() ==0){

            showError("ERROR: You are missing values on the server/port field");

        }else{
            // Insert connect to server here, user may type wrong server
            serverStatusTxt.setText("You are now connected to "+ server+":"+port);

        }

    }

    @FXML
    protected void onRegisterBtnClick() throws IOException, InterruptedException {
        String user = userField.getText();
        String server = serverField.getText();
        String port = portField.getText();
        if(server.length()==0 || port.length() ==0){

            showError("ERROR: You are missing values on the server/port field");

        }

        else if(user.length()==0 || user.length()<3){

            showError("ERROR: Please input a valid username/alias with at least 3 characters");

        }else{
            // Insert user alias checking algo here
        }


    }
    private String showClientHelp(){
        StringBuilder commandsBuilder = new StringBuilder();
        commandsBuilder.append("The following buttons do the ff:\n\n");
        commandsBuilder.append("connect - allows you to connect to a server you input\n\n");
        commandsBuilder.append("disconnect - disconnect you from the server\n\n");
        commandsBuilder.append("register - you will give it an alias that is not taken\n\n");
        commandsBuilder.append("directory - sends you a list of files to get from the server\n\n");
        commandsBuilder.append("get - gets the file based on the file name you gave\n\n");
        commandsBuilder.append("store - stores the file based on the file name you gave\n\n");

        String commands = commandsBuilder.toString();

        return commands;
    }
    @FXML
    protected void closeWindow(){
        Stage stage = (Stage)(okCloseBtn.getScene().getWindow());
        stage.close();

    }
    @FXML
    protected void setHelpBtn() throws IOException {
        showHelp(showClientHelp());

    }
    public static List<Node> getAllNodes(Parent root) {
        List<Node> nodes = new ArrayList<>();
        addAllDescendents(root, nodes);
        return nodes;
    }

    private static void addAllDescendents(Parent parent, List<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent) {
                addAllDescendents((Parent) node, nodes); // Recursive call
            }
        }
    }

    private void showHelp(String msg) throws IOException  {

        Stage popup = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("popup-confirm-config.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 550, 700);
        popup.setTitle("Message from the System");


        // Steps below describe how to get a specific node of a particuler scene
        Parent root = scene.getRoot();
        List<Node> allNodes = getAllNodes(root);
        List<Label> labels = new ArrayList<>();
        for (Node node : allNodes) {
            if (node instanceof Label) {
                Label label = (Label) node;
                labels.add(label);
            }
        }

        labels.get(0).setText(msg);
        labels.get(0).setWrapText(true);

        popup.setHeight(700);
        popup.setWidth(550);
        popup.setScene(scene);
        popup.show();




    }
    private void showError(String msg) throws IOException  {

        Stage popup = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("popup-error-config.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 300, 400);
        popup.setTitle("Message from the System");


        // Steps below describe how to get a specific node of a particuler scene
        Parent root = scene.getRoot();
        List<Node> allNodes = getAllNodes(root);
        List<Label> labels = new ArrayList<>();
        for (Node node : allNodes) {
            if (node instanceof Label) {
                Label label = (Label) node;
                labels.add(label);
            }
        }

        labels.get(0).setText(msg);
        labels.get(0).setWrapText(true);

        popup.setHeight(300);
        popup.setWidth(400);
        popup.setScene(scene);
        popup.show();




    }





}