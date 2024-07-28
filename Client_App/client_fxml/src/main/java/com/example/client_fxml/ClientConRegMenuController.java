package com.example.client_fxml;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientConRegMenuController {
    private Socket clientEndpoint;

    private Scene targetNextScene;

    private DataInputStream disReader;

    private DataOutputStream dosWriter;

    private BufferedReader bisReader;

    private BufferedWriter bosWriter;

    private Stage stage;

    private String alias;

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
    protected void onDisonnectButtonClick() throws IOException  {

        try{
            if(this.alias == null || this.alias.length() ==0){
                this.alias = "";
            }

            dosWriter.writeInt(-1);
            dosWriter.writeUTF(this.alias);
            this.clientEndpoint.close();
            serverStatusTxt.setText("SERVER DISCONNECTED AND IDLE...");
            showHelp("SUCCESS: You disconnected from the server");
        }catch (Exception e){
            showError("ERROR: could not disconnect to the server");
        }



    }
    @FXML
    protected void onConnectButtonClick() throws IOException  {
        String server = serverField.getText();
        String port = portField.getText();

        if(server.length()==0 || port.length() ==0){

            showError("ERROR: You are missing values on the server/port field");

        }else{
            try{
                int nPort = Integer.parseInt(port);
                this.clientEndpoint = new Socket();
                this.clientEndpoint.connect(new InetSocketAddress(server, nPort), 5000);


                serverStatusTxt.setText("You are now connected to "+ server+":"+port);
                showHelp("SUCCESS: You are now connected to server");
            }catch (Exception e){
                showError("ERROR: Connection error, could not connect to the address / not found");
            }


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

        else if(user == null || user.length()==0 || user.length()<3){

            showError("ERROR: Please input a valid username/alias with at least 3 characters");

        }else{
            int flag = 0;

            int nPort = Integer.parseInt(port);

            flag = sendClientAlias(user);
            if (flag == 1){
                this.alias = user;
                this.bisReader = new BufferedReader(new InputStreamReader(this.clientEndpoint.getInputStream()));
                this.bosWriter = new BufferedWriter(new OutputStreamWriter(this.clientEndpoint.getOutputStream()));
                FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("server-chooseChatOrFile.fxml"));
                Scene scene = new Scene(fxmlLoader.<Parent>load(), 600, 800);
                Client_ChatFile_MenuController controller = fxmlLoader.getController();
                controller.setStage(this.stage);
                controller.setBisReader(this.bisReader);
                controller.setBosWriter(this.bosWriter);
                controller.setServerStatusTxt("connected to SERVER: "+ server+":"+port);
                controller.setClientEndpoint(this.clientEndpoint);
                controller.setAlias(this.alias);
                controller.setChatFileMenu(scene);
                controller.setDisReader(disReader);
                controller.setDosWriter(dosWriter);
                this.targetNextScene = scene;
                Thread.sleep(100);
                showSuccessWithVal("SUCCESS: You are now registered currently as \""+ user+"\""+".\n Welcome!!!");





            }
            else if(flag == -1){
                showError("ERROR: You are not connected to a network yet. Please connect first");
            }
            else {
                showError("ERROR: Alias is already taken");
            }





        }


    }

    public void setStage(Stage stage) {
        this.stage = stage;

    }
    /* function code to send when interacting with client
     * 0 - register
     * 1 - getFile
     * 2 - storeFile
     * 3 - getDirectory
     * 4 - chat
     * */
    private int sendClientAlias(String alias){

        try {
            this.dosWriter = new DataOutputStream(this.clientEndpoint.getOutputStream());
            this.disReader = new DataInputStream(this.clientEndpoint.getInputStream());
            int flag = 0;

            dosWriter.writeInt(0);  // Send code that this is a register request
            System.out.println("Now sending alias");
            dosWriter.writeUTF(alias);
            System.out.println("Now waiting for Int");
            flag = disReader.readInt();

            if (flag == 0){
                System.out.println("Result was false");

            }else if (flag == 1){
                System.out.println("Result was true");
            }else{
                System.out.println("Alias was taken");
            }
            return flag;
        }catch (Exception e){
            return -1;

        }



    }

    private String showClientHelp(){
        StringBuilder commandsBuilder = new StringBuilder();
        commandsBuilder.append("The following buttons do the ff:\n\n");
        commandsBuilder.append("connect - allows you to connect to a server you input\n\n");
        commandsBuilder.append("disconnect - disconnect you from the server\n\n");
        commandsBuilder.append("register - you will give it an alias that is not taken\n\n");
        commandsBuilder.append("directory - sends you a list of files to get from the server\n\n");
        commandsBuilder.append("get files - gets the file based on the file name you gave\n\n");
        commandsBuilder.append("store files - stores the file based on the file name you gave\n\n");
        commandsBuilder.append("refresh - refreshes available users to unicast\n\n");
        commandsBuilder.append("chat - gives you both unicast and broadcast\n\n");
        commandsBuilder.append("back - sends you to the previous menu\n\n");

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
        showHelp(showClientHelp(), 550, 700);

    }

    @FXML
    protected void setToNextSceneTrigger(){
        this.stage.setScene(this.targetNextScene);
        Stage outstage = (Stage)(okCloseBtn.getScene().getWindow());
        outstage.close();

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

        popup.setHeight(400);
        popup.setWidth(550);
        popup.setScene(scene);
        popup.show();




    }
    private void showHelp(String msg, int w, int h) throws IOException  {

        Stage popup = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("popup-confirm-config.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), w, h);
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

        popup.setHeight(w);
        popup.setWidth(h);
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