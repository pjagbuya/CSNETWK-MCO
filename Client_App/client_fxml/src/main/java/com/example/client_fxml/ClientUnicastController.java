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
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientUnicastController extends Client_ChatFile_MenuController{

    private Boolean isReceivePersonLeave;
    private Stage stage;
    private volatile boolean keepCheckingResponses = true; // volatile for thread safety
    private Scene clientChatServers;

    private Thread activeResponseReader;


    private FXMLLoader clientChatServerFXMLload;

    private Scene targetNextScene;

    private Scene targetPrevScene;
    private Scene chatFileMenu;
    private String alias;

    private Socket clientEndpoint;

    private ArrayList<String> avail_in_net;

    private String person_to_chat;
    @FXML
    private VBox msgLogs;

    @FXML
    private Label serverAliasTxt;

    @FXML
    private Label serverStatusTxt;



    @FXML
    private VBox userLogs;

    @FXML
    private TextField chatText;

    @FXML
    private Button sendChatBtn;
    private DataInputStream disReader;

    private DataOutputStream dosWriter;

    private BufferedReader bisReader;

    private BufferedWriter bosWriter;


    public void setReceivePersonLeave(Boolean receivePersonLeave) {
        isReceivePersonLeave = receivePersonLeave;
    }

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
    protected void onSendChatBtn() throws IOException {


        try{

            if(isReceivePersonLeave){
                showError("ERROR: Your contact user is not active anymore please press \"Back\"");
            }else{
                String chat = chatText.getText();

                this.dosWriter.writeInt(42);


                System.out.print("Person you are gonna chat "+ this.person_to_chat);

                dosWriter.writeUTF(this.person_to_chat);
                dosWriter.writeUTF(chat);
                dosWriter.flush();

                addNewHBoxMessage(this.alias, chat, 0);
                chatText.setText("");

            }








        }catch (Exception e){
            e.printStackTrace();
            showError("ERROR: Chat has been interrupted and disconnecteed from server");
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

            serverStatusTxt.setText("SERVER DISCONNECTED AND IDLE...");
            this.setKeepCheckResponses(false);
            this.activeResponseReader.interrupt();
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
    protected void onBackBtnUnicast() {

//        dosWriter.writeInt(-999);
        try{
            System.out.println("Person left right? " + isReceivePersonLeave);
            if(isReceivePersonLeave){
                this.dosWriter.writeInt(-888); // call unicast exit
                isReceivePersonLeave = false;


            } else{

                this.dosWriter.writeInt(-999); // call unicast exit with notify on user
                this.dosWriter.writeUTF(this.person_to_chat);
                isReceivePersonLeave = false;
            }

            this.keepCheckingResponses = false;
        }catch (IOException e){
            e.printStackTrace();
        }

        this.stage.setScene(this.chatFileMenu);


    }


    public void setKeepCheckResponses(boolean keepCheckResponses) {
        this.keepCheckingResponses = keepCheckResponses;


    }

    public void activateResponseReader(){

            new Thread(()->{
                try {

                    this.dosWriter.writeInt(42);
                    dosWriter.writeUTF(this.person_to_chat);
                    dosWriter.writeUTF("*"+ this.alias+" HAS ENTERED YOUR CHAT DMs*");

                    dosWriter.flush();
                }
                catch(IOException e){
                    showError("ERROR! The server chat has been disconnected");
                }

                int num = 999;

                while(this.keepCheckingResponses){

                    try {

                        num = disReader.readInt();
                        System.out.println("Exit code: " + num);
                        if(num==-999){

                            break;
                        }else if( num == -111){

                            String user = disReader.readUTF();
                            Platform.runLater(() -> addNewHBoxMessage(user, "User left", -1));
                            isReceivePersonLeave = true;



                        }

                        else{

                            String user = this.disReader.readUTF();
                            String msg = this.disReader.readUTF();
                            System.out.println("User has received a message from: " + user);
                            System.out.println("Message is " + msg);

                            Platform.runLater(() -> addNewHBoxMessage(user, msg, 1));
                        }




                    } catch (IOException e) {


                        e.printStackTrace();
                        break;
                    }

                }

            }).start();




    }

    // 0 -  current user
    // 1 - other user
    // 2 - No color
    private void addNewHBoxMessage(String alias, String msg, int userType){
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(10,10,10,10));
        hbox.setMaxHeight(40);
        Label aliasL = new Label(alias+": ");
        Label msgL = new Label(msg);
        Label leftChatL = new Label(alias + " has left the chat");
        if(userType == 0)
            aliasL.setTextFill(Color.BLUE);
        else if(userType == 1)
            aliasL.setTextFill(Color.RED);
        else if(userType==-1){

        }
        aliasL.setFont(Font.font("Cambria", FontWeight.BOLD, 12));
        leftChatL.setStyle("-fx-font: 12px Cambria italic;");
        msgL.setFont(Font.font("Cambria", FontWeight.NORMAL, 12));
        if(userType != -1){
            hbox.getChildren().addAll(aliasL, msgL);
            msgLogs.getChildren().add(hbox);
        }else{
            hbox.getChildren().addAll(leftChatL);
            msgLogs.getChildren().add(hbox);
        }



    }

    public void setClientChatServerFXMLload(FXMLLoader setClientChatServerFXMLload) {
        this.clientChatServerFXMLload = setClientChatServerFXMLload;
    }

    public void setPerson_to_chat(String person_to_chat) {
        this.person_to_chat = person_to_chat;
    }

    @Override
    public void setChatFileMenu(Scene chatFileMenu) {
        this.chatFileMenu = chatFileMenu;
    }

    public void setClientChatServers(Scene clientChatServers) {
        this.clientChatServers = clientChatServers;
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
    public void setChattingWithAlias(String alias_rec) {
        this.serverAliasTxt.setText("You are chatting with "+"\""+alias_rec+"\"" + " as \"" + this.alias + "\"");
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
