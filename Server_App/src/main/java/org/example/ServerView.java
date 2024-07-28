package org.example;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.example.UnivNodes.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.net.URI;
import java.nio.file.Paths;


public class ServerView{

    final static int WIDTH = 1200;
    final static int HEIGHT = 600;
    final static String IP_LOCAL = "127.0.0.1";
    final static String BG_COLOR = "#ECFFE6";
    private BorderPane bgPane;
    private Stage parentWin;
    private Scene startScene;
    private static final MenuButton BTN_ADD = new MenuButton("Add Socket");
    private static final MenuButton BTN_DEL = new MenuButton("Close Sockets");
    private static final MenuButton BTN_HELP = new MenuButton("?");

    private static final MenuButton BTN_REF = new MenuButton("Refresh");

    private static final MenuButton BTN_CHAT= new MenuButton("Broadcast");
    final static String SERVER_FILE_PATH = ".\\org\\example\\server_files";

    private final Font H1_FONT = new Font("Courier New", 32);
    private final Font H2_FONT = new Font("Courier New", 24);
    private final Font NORM_FONT = new Font("Courier New", 16);
    private VBox chatVB;
    private ScrollPane chatContent;
    private StackPane serverStatusPane;
    private ScrollPane dirContentSP;

    private VBox dirContentSPTexts;
    private VBox chatContentTexts;

    private TextFlow serverStatTextFlow;

    private VBox serverStatusPaneWithDir;

    public ServerView(Stage parentWin)
    {
        this.parentWin = parentWin;
        this.bgPane = new BorderPane();
        this.startScene = new Scene(bgPane, WIDTH, HEIGHT);
        this.chatVB = new VBox();
        this.chatContent = new ScrollPane();
        StackPane chatContentStackPane = new StackPane();
        this.serverStatusPane = new StackPane();
        this.dirContentSP = new ScrollPane();
        StackPane dirContentStackPane = new StackPane();
        this.dirContentSPTexts = new VBox();
        this.chatContentTexts = new VBox();

        this.serverStatusPaneWithDir = new VBox();

        VBox LabelsAndInputs = new VBox(20);

        HBox btnSection = new HBox(20);



        btnSection.setAlignment(Pos.BOTTOM_CENTER);
        btnSection.getChildren().addAll(BTN_ADD, BTN_DEL, BTN_REF, BTN_CHAT, BTN_HELP);


        Label chatTitleLabel = new Label("Server Chat");
        Label inputLabel = new Label("Input name of socket to listen");
        Label ipLabel = new Label("Server IP: " + IP_LOCAL);
        Label serverDirLabel = new Label("Server Directory: ");

        serverDirLabel.setFont(H2_FONT);
        this.serverStatTextFlow = new TextFlow();
        this.serverStatTextFlow.setMaxWidth(200);
        Text serverStatusText = new Text("Server "+ IP_LOCAL + " is inactive.");
        inputLabel.setFont(H2_FONT);
        ipLabel.setFont(H1_FONT);
        serverStatusText.setFont(H2_FONT);
        chatTitleLabel.setFont(H1_FONT);
        this.serverStatTextFlow.getChildren().add(serverStatusText);


        TextField inputField = new TextField();
        inputField.setPrefHeight(100);
        inputField.setMaxWidth(600);
        inputField.setStyle("-fx-font-size: 36px;");

        LabelsAndInputs.getChildren().addAll(ipLabel, inputLabel, inputField);
        LabelsAndInputs.setAlignment(Pos.CENTER);
        this.chatVB.setStyle(" -fx-border-color: green, transparent, transparent, black; " +
                "  -fx-border-width: 3px; padding-right:20px;");
        this.chatContent.setStyle(" -fx-border-color: black, transparent, transparent, black; " +
                "  -fx-border-width: 4px;");

        this.chatVB.getChildren().addAll(chatTitleLabel, this.chatContent);
        this.chatVB.setAlignment(Pos.TOP_CENTER);
        this.chatVB.setPrefHeight(700);

        this.serverStatusPane.getChildren().addAll(this.serverStatTextFlow);
        this.serverStatusPane.setMaxWidth(200);
        this.serverStatusPane.setAlignment(Pos.CENTER);


        serverStatusPaneWithDir.getChildren().addAll(serverStatusPane, serverDirLabel, dirContentSP);
        serverStatusPaneWithDir.setAlignment(Pos.TOP_CENTER);

        chatContentStackPane.getChildren().add(chatContentTexts);
        chatContent.setContent(chatContentStackPane);
        dirContentStackPane.getChildren().add(dirContentSPTexts);
        dirContentSP.setContent(dirContentStackPane);
        dirContentSPTexts.maxWidth(300);
        serverStatusPaneWithDir.setMaxWidth(300);
        serverStatusPaneWithDir.setPrefWidth(300);
        serverStatusPaneWithDir.setPrefHeight(700);
        chatContent.setPrefViewportHeight(700);
        dirContentSP.setPrefViewportWidth(400);

        dirContentSP.setPrefViewportWidth(300);
        dirContentSP.setPrefViewportHeight(700);
        this.dirContentSPTexts.setMinHeight(700);



        this.bgPane.setCenter(LabelsAndInputs);
        this.bgPane.setLeft(this.chatVB);
        this.bgPane.setRight(serverStatusPaneWithDir);

        this.bgPane.setBottom(btnSection);
        this.bgPane.setStyle("-fx-base: " + BG_COLOR+ ";");
        this.parentWin.setWidth(WIDTH);
        this.parentWin.setHeight(HEIGHT);
        this.parentWin.setTitle("Server Application");
        this.parentWin.setScene(startScene);


        BTN_ADD.setOnAction(e->{
            String numString = inputField.getText();
            ServerController.makeSocket(numString);
            inputField.setText("");
        });

        BTN_DEL.setOnAction(e->{
            setServerInactive();
            ServerController.disconnectSocket();
        });

        BTN_REF.setOnAction(e->{
            updateDirectory(showDirectory());
        });

        BTN_HELP.setOnAction(e->{
            String help_options = "'Add sockets' - adds the socket number you specify in. Reclicking option will kill previous socket \n" +
                                  "'Close sockets' - close all current sockets \n" +
                                  "'Refresh' - refreshes the directory \n"     ;
            AlertBoxRep alertBoxRep = new AlertBoxRep("HELP Options",help_options);
        });
        BTN_CHAT.setOnAction(e->{
            String msg = inputField.getText();
            ServerController.msgServerUsers(msg);
            addChatText("SERVER: "+ msg);
            inputField.setText("");


        });


    }



    public void addChatText(String text){
        Label temp = new Label(text);
        temp.setFont(NORM_FONT);
        this.chatContentTexts.getChildren().addAll(temp);
        
    }

    public void updateDirectory(String msg){

        this.dirContentSPTexts.getChildren().clear();
        System.out.println(msg);
        Label text = new Label(msg);
        text.setFont(H2_FONT);
        this.dirContentSPTexts.getChildren().add(text);
    }
    public String showDirectory(){

        URI directoryUri = Paths.get(SERVER_FILE_PATH).toUri();
        File directory = new File(directoryUri);
        StringBuilder directoryListing = new StringBuilder("");

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    directoryListing.append(file.getName()).append("\n");
                }
            } else {
                directoryListing.append(Paint.paintTextRed("ERROR: Failed to list contents\n"));
            }
        } else {
            directoryListing.append(Paint.paintTextRed("ERROR: Directory not found\n"));
        }

        String listingString = directoryListing.toString();
        System.out.println("Directory currently has: ");
        System.out.println(listingString);
        return listingString;
    }
    public void setServerActive(int socketNum){
        System.out.println("Updating serveer message");
        this.serverStatTextFlow.getChildren().clear();
        Text serverStatusText = new Text("Server "+ IP_LOCAL + ":" + socketNum +" is active and listening for files.");
        serverStatusText.setFont(H2_FONT);
        this.serverStatTextFlow.getChildren().add(serverStatusText);
    }
    public void setServerInactive(){

        this.serverStatTextFlow.getChildren().clear();
        Text serverStatusText = new Text("Server "+ IP_LOCAL + " is now inactive.");
        serverStatusText.setFont(H2_FONT);
        this.serverStatTextFlow.getChildren().add(serverStatusText);
    }

    public void showErrorBox(String msg){
        AlertBoxRep alertBoxRep = new AlertBoxRep("ERROR!", msg, 16);

    }

    public void updateServerMessage(int sockNum, String msg){
//        updateDirectory(showDirectory());
        setServerActive(sockNum);
    }




}