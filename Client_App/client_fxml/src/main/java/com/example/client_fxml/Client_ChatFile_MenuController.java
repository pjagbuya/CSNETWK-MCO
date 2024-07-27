package com.example.client_fxml;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client_ChatFile_MenuController extends ClientConRegMenuController{
    private Stage stage;


    private Scene targetNextScene;


    private Scene chatServersScene;

    private ClientChatController clientChatController;

    private FXMLLoader clientChatFXMLLoader;

    private Thread runningServerChat;

    private Scene chatFileMenu;

    private String alias;

    private Socket clientEndpoint;

    @FXML
    private Button getFilesBtn;

    @FXML
    private Button dirBtn;

    @FXML
    private Button storeFilesBtn;


    @FXML
    private Button chatBtn;


    @FXML
    private Button disconnectBtn;

    @FXML
    private Label serverStatusTxt;

    @FXML
    private Label templateText;

    @FXML
    private Button backBtn;

    private DataInputStream disReader;

    private DataOutputStream dosWriter;

    private BufferedReader bisReader;

    private BufferedWriter bosWriter;

    public void setBosWriter(BufferedWriter bosWriter) {
        this.bosWriter = bosWriter;
    }

    public void setBisReader(BufferedReader bisReader) {
        this.bisReader = bisReader;
    }

    public void setDisReader(DataInputStream disReader) {
        this.disReader = disReader;
    }

    public void setDosWriter(DataOutputStream dosWriter) {
        this.dosWriter = dosWriter;
    }

    @FXML
    protected void onDisconnectBtn() throws IOException  {

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

            System.out.println("Client endpoint is now sending -1");
            dosWriter.writeInt(-1);
            dosWriter.writeUTF(this.alias);

            serverStatusTxt.setText("SERVER DISCONNECTED AND IDLE...");
            this.clientEndpoint.close();
            showSuccessWithVal("SUCCESS: You disconnected from the server");
        }catch (Exception e){
            showError("ERROR: could not disconnect to the server");
        }

    }

    public void setChatFileMenu(Scene chatFileMenu) {
        this.chatFileMenu = chatFileMenu;
    }
    @FXML
    protected void switchGetFileMenu() throws IOException {
        this.setChatFileMenu(this.stage.getScene());
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("server-getFile.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 800);
        ClientGetStoreFileController controller = fxmlLoader.getController();
        controller.setStage(stage);
        controller.setChatFileMenu(this.chatFileMenu);
        controller.setAlias(this.alias);
        controller.setServerStatusTxt(this.serverStatusTxt.getText());
        controller.setClientEndpoint(this.clientEndpoint);
        controller.setDisReader(this.disReader);
        controller.setDosWriter(this.dosWriter);
        stage.setTitle("Client Application");
        stage.setHeight(600);
        stage.setWidth(800);
        stage.setScene(scene);

    }

    @FXML
    protected void switchStoreFileMenu() throws IOException {
        this.setChatFileMenu(this.stage.getScene());
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("server-storeFile.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 800);
        ClientGetStoreFileController controller = fxmlLoader.getController();
        controller.setStage(stage);
        controller.setChatFileMenu(this.chatFileMenu);
        controller.setAlias(this.alias);
        controller.setServerStatusTxt(this.serverStatusTxt.getText());
        controller.setClientEndpoint(this.clientEndpoint);
        controller.setDisReader(this.disReader);
        controller.setDosWriter(this.dosWriter);
        stage.setTitle("Client Application");
        stage.setHeight(600);
        stage.setWidth(800);
        stage.setScene(scene);

    }


    @FXML
    protected void switchSceneChatMenu() throws IOException {

        System.out.println("Switch scene triggered");
        if(this.chatFileMenu == null){
            this.setChatFileMenu(this.stage.getScene());
        }


        if(clientChatFXMLLoader == null){
            clientChatFXMLLoader = new FXMLLoader(ClientApplication.class.getResource("server-uni-broad-chat.fxml"));
        }

        if(this.chatServersScene == null){
            this.chatServersScene = new Scene(clientChatFXMLLoader.load(), 700, 700);
        }
        if(this.clientChatController == null){
            this.clientChatController = clientChatFXMLLoader.getController();
        }
        this.clientChatController.setDisReader(this.disReader);
        this.clientChatController.setDosWriter(this.dosWriter);
        this.clientChatController.setBisReader(this.bisReader);
        this.clientChatController.setBosWriter(this.bosWriter);

        this.clientChatController.setStage(stage);
        this.clientChatController.setChatFileMenu(this.chatFileMenu);
        this.clientChatController.setAlias(this.alias);
        this.clientChatController.setServerStatusTxt(this.serverStatusTxt.getText());
        this.clientChatController.setClientEndpoint(this.clientEndpoint);
        this.clientChatController.setServerAliasDisplayTxt();
        this.clientChatController.sendAliasesAvail();
        this.clientChatController.displayNewUsersChatList();
        this.clientChatController.activateResponseReader();



        stage.setTitle("Client Application");
        stage.setHeight(700);
        stage.setWidth(700);

        Platform.runLater(() -> {
            stage.setScene(this.chatServersScene);
        });

    }

    /* function code to send when interacting with client
     * 0 - register
     * 1 - getFile
     * 2 - storeFile
     * 3 - getDirectory
     * 4 - chat
     * */
    @FXML
    protected void onGetDirectory() throws IOException {
        try{

            dosWriter.writeInt(3);


            String directory = disReader.readUTF();
            showSuccess(directory);


        }catch (Exception e){
            showError("ERROR: Process is interrupted");
        }


    }






    public void setServerStatusTxt(String serverStatusTxt) {
        this.serverStatusTxt.setText(serverStatusTxt);
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setClientEndpoint(Socket clientEndpoint){
        this.clientEndpoint = clientEndpoint;
    }



    public void setStage(Stage stage) {
        this.stage = stage;
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
