package com.example.client_fxml;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientChatController extends Client_ChatFile_MenuController{
    private Stage stage;

    private Scene targetNextScene;
    private Scene chatFileMenu;
    private String alias;

    private Socket clientEndpoint;


    @FXML
    private Label serverStatusTxt;

    @FXML
    private Label serverAliasTxt;


    @FXML
    protected void onDisconnectBtnChat() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("server-conreg.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 800);
        ClientConRegMenuController controller = fxmlLoader.getController();
        controller.setStage(this.stage);
        this.stage.setTitle("Client Application");
        this.targetNextScene = scene;
        try{
            if(this.alias == null || this.alias.length() ==0){
                this.alias = "";
            }
            DataOutputStream dosWriter = new DataOutputStream(this.clientEndpoint.getOutputStream());
            System.out.println("Client endpoint is now sending -1");
            dosWriter.writeInt(-1);
            dosWriter.writeUTF(this.alias);

            serverStatusTxt.setText("SERVER DISCONNECTED AND IDLE...");

            this.clientEndpoint.close();
            showSuccessWithVal("SUCCESS: You disconnected from the server");
        }catch (Exception e){
            e.printStackTrace();
            showError("ERROR: could not disconnect to the server");
        }

    }

    @Override
    public void setClientEndpoint(Socket clientEndpoint) {
        this.clientEndpoint = clientEndpoint;
    }

    @FXML
    protected void onBackBtn() {
        this.stage.setScene(this.chatFileMenu);
    }

    @Override
    public void setChatFileMenu(Scene chatFileMenu) {
        this.chatFileMenu = chatFileMenu;
    }

    public void setServerStatusTxt(String serverStatusTxt) {
        this.serverStatusTxt.setText(serverStatusTxt);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setAlias(String alias) {
        this.alias = alias;
        this.serverAliasTxt.setText("You are chatting in Server as \"" + this.alias + "\"");
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

    private void showError(String msg) throws IOException {

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
    private void showSuccess(String msg) throws IOException  {

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

        popup.setHeight(400);
        popup.setWidth(550);
        popup.setScene(scene);
        popup.show();




    }

    private void showSuccessWithVal(String msg) throws IOException  {
        Stage popup = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("popup-required-confirm-config.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 550, 700);
        ClientPopUpMenuController controller = fxmlLoader.getController();
        controller.setStage(this.stage);
        controller.setTargetNextScene(this.targetNextScene);
        controller.setMyStage(popup);

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

        popup.setHeight(400);
        popup.setWidth(550);
        popup.setScene(scene);
        popup.show();



    }


}
