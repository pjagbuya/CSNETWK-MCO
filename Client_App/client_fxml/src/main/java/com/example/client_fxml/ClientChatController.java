package com.example.client_fxml;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientChatController extends Client_ChatFile_MenuController{
    private Stage stage;

    private Scene targetNextScene;

    private Scene targetPrevScene;
    private Scene chatFileMenu;
    private String alias;

    private Socket clientEndpoint;

    private ArrayList<String> avail_in_net;
    private Scene clientUnicastScene;

    private Scene clientChatServers;

    private ClientUnicastController clientUnicastController;

    private String person_to_chat;


    @FXML
    private Label serverAliasDisplayTxt;

    @FXML
    private Label serverStatusTxt;


    @FXML
    private TextField serverChatField;

    @FXML
    private VBox serverLogs;




    @FXML
    private VBox userLogs;
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
    protected void onRefreshUsers(){
        sendAliasesAvail();
        Platform.runLater(()->{
            try {
                displayNewUsersChatList();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void activateResponseReader(){


        new Thread(()->{
            try{
                this.dosWriter.writeInt(111);
                int num = 1;
                while(num == 1){
                    num = this.disReader.readInt();
                    if(num == -1){
                        break;
                    }
                    String user = this.disReader.readUTF();
                    String msg = this.disReader.readUTF();
                    Platform.runLater(() -> {
                        addNewHBoxMessage(user, msg);

                    });



                }
            }catch (IOException e){
                e.printStackTrace();
                showError("ERROR: User was unable to join Unicast");
            }


            int num = 999;
            while(num!= -999){

                try {


                    num = this.disReader.readInt();
                    System.out.println("Code received for broadcast: "+ num);
                    if(num == - 999){           //Thread exiter
                        break;
                    }
                    String user = this.disReader.readUTF();
                    String msg = this.disReader.readUTF();
                    sendAliasesAvail();

                    Platform.runLater(() -> {
                        addNewHBoxMessage(user, msg);

                        try {
                            displayNewUsersChatList();
                        } catch (IOException e) {
                            e.printStackTrace();
                            showError("ERROR: unable to decrypt unicast");
                        }
                    });





                } catch (IOException e) {


                    e.printStackTrace();
                    showError("ERROR: unable to decrypt unicast");
                    break;
                }

            }

        }).start();




    }

    // 0 -  current user
    // 1 - other user
    // 2 - No color
    private void addNewHBoxMessage(String alias, String msg){
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(10,10,10,10));
        hbox.setMaxHeight(40);
        Label aliasL = new Label(alias+": ");
        Label msgL = new Label(msg);
        Label leftChatL = new Label(alias + " has left the chat");

        aliasL.setFont(Font.font("Cambria", FontWeight.BOLD, 12));
        leftChatL.setStyle("-fx-font: 12px Cambria italic;");
        msgL.setFont(Font.font("Cambria", FontWeight.NORMAL, 12));

        hbox.getChildren().addAll(aliasL, msgL);
        serverLogs.getChildren().add(hbox);

    }

    @FXML
    protected void onBroadCastBtn() throws IOException {
        try{

            String chat = serverChatField.getText();

            this.dosWriter.writeInt(4);

            dosWriter.writeUTF(chat);
            dosWriter.flush();

            addNewHBoxMessage(this.alias, chat);
            serverChatField.setText("");

        }catch (Exception e){
            e.printStackTrace();
            showError("ERROR: Chat has been interrupted and disconnecteed from server");
        }
    }

    @FXML
    protected void switchSceneChatPerson(String s) throws IOException {
        this.dosWriter.writeInt(-888);  //Shutoff thread for this broadcast
        System.out.println("Switch scene for specific triggered");
        this.setClientChatServers(this.stage.getScene());
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("server-unicast-msg-chat.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 700);
        ClientUnicastController controller = fxmlLoader.getController();
        this.clientUnicastController = controller;
        controller.setStage(stage);
        controller.setDisReader(this.disReader);
        controller.setDosWriter(this.dosWriter);
        controller.setBisReader(this.bisReader);
        controller.setBosWriter(this.bosWriter);
        controller.setChatFileMenu(this.chatFileMenu);
        controller.setClientChatServers(this.clientChatServers); // Switches back to prev scene
        controller.setAlias(this.alias);
        controller.setServerStatusTxt(this.serverStatusTxt.getText());
        controller.setKeepCheckResponses(true);
        controller.setClientEndpoint(this.clientEndpoint);
        controller.setPerson_to_chat(s);
        controller.setReceivePersonLeave(false);
        controller.setChattingWithAlias(s);
        controller.activateResponseReader();


//        controller.setClientChatServerFXMLload(fxmlLoader);
        stage.setTitle("Client Application");
        stage.setHeight(700);
        stage.setWidth(700);
        this.clientUnicastScene = scene;
        stage.setScene(this.clientUnicastScene);

    }


    public void displayNewUsersChatList() throws IOException {
        System.out.println("displayNewUsersChatList is now active");
        this.userLogs.getChildren().clear();

        for(String s : avail_in_net){

            Button newButton = new Button(s);
            newButton.getStyleClass().add("button-89");
            newButton.getStyleClass().add("cambria-bold-16");
            newButton.setPrefHeight(60);
            newButton.setPrefWidth(180);
            newButton.setOnAction(event -> {


                try {
                    switchSceneChatPerson(s);
                } catch (IOException e) {
                    e.printStackTrace();

                }
            });

            this.userLogs.getChildren().add(newButton);
        }


    }

    public void sendAliasesAvail(){
        System.out.println("Now checking all aliases in sendAliasesAvail");
        String input = "";
        this.avail_in_net = new ArrayList<>();


        try{

            dosWriter.writeInt(41);
            while(!input.equalsIgnoreCase("-1")){
                input = disReader.readUTF();
                if(!input.equalsIgnoreCase("-1") && !input.equalsIgnoreCase(this.alias)){

                    avail_in_net.add(input);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
            showError("ERROR: Server failed to respond with user lists");
        }
        System.out.println("Finding users...");
        for(String s: avail_in_net){
            System.out.println("Available user: " + s);
        }



    }
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
            this.clientUnicastController.setKeepCheckResponses(false);

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
    protected void onBackBtn(){
        try
        {
            this.dosWriter.writeInt(-888);  //Shutoff thread for this broadcast
        }catch (IOException e){
            e.printStackTrace();
        }

        this.stage.setScene(this.chatFileMenu);

    }

    public void setPerson_to_chat(String person_to_chat) {
        this.person_to_chat = person_to_chat;
    }

    public void setClientChatServers(Scene clientChatServers) {
        this.clientChatServers = clientChatServers;
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
    }


    public void setServerAliasDisplayTxt() {
        this.serverAliasDisplayTxt.setText("You are chatting in Server as \"" + this.alias + "\"");
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

    private void showError(String msg)  {

        try{
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
        }catch (IOException e){
            e.printStackTrace();

        }







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
