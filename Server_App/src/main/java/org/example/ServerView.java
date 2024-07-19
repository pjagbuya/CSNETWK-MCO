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

    public ServerView(Stage parentWin)
    {
        this.parentWin = parentWin;
        this.bgPane = new BorderPane();
        this.startScene = new Scene(bgPane, WIDTH, HEIGHT);
        this.chatVB = new VBox();
        this.chatContent = new ScrollPane();
        this.serverStatusPane = new StackPane();
        this.dirContentSP = new ScrollPane();
        this.dirContentSPTexts = new VBox();
        this.chatContentTexts = new VBox();

        VBox serverStatusPaneWithDir = new VBox();
        VBox LabelsAndInputs = new VBox(20);

        HBox btnSection = new HBox(20);



        btnSection.setAlignment(Pos.BOTTOM_CENTER);
        btnSection.getChildren().addAll(BTN_ADD, BTN_DEL, BTN_HELP);


        Label chatTitleLabel = new Label("Server Chat");
        Label inputLabel = new Label("Input name of socket to listen");
        Label ipLabel = new Label("Server IP: " + IP_LOCAL);
        Label serverDirLabel = new Label("Server Directory: ");
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

        this.serverStatusPane.getChildren().addAll(this.serverStatTextFlow);
        this.serverStatusPane.setMaxWidth(200);
        this.serverStatusPane.setAlignment(Pos.CENTER);


        serverStatusPaneWithDir.getChildren().addAll(serverStatusPane, serverDirLabel, dirContentSP);
        serverStatusPaneWithDir.setAlignment(Pos.CENTER);

        chatContent.setContent(chatContentTexts);
        dirContentSP.setContent(dirContentSPTexts);



        this.bgPane.setCenter(LabelsAndInputs);
        this.bgPane.setLeft(this.chatVB);
        this.bgPane.setRight(serverStatusPane);

        this.bgPane.setBottom(btnSection);
        this.bgPane.setStyle("-fx-base: " + BG_COLOR+ ";");
        this.parentWin.setWidth(WIDTH);
        this.parentWin.setHeight(HEIGHT);
        this.parentWin.setTitle("Server Application");
        this.parentWin.setScene(startScene);


        BTN_ADD.setOnAction(e->{
            String numString = inputField.getText();
            ServerController.makeSocket(numString);
        });

        BTN_DEL.setOnAction(e->{
            setServerInactive();
            ServerController.disconnectSocket();
        });


    }



    public void addChatText(String text){
        Label temp = new Label(text);
        temp.setFont(NORM_FONT);
        this.chatContentTexts.getChildren().addAll(temp);
        
    }

    public void setServerActive(int socketNum){
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
        setServerActive(sockNum);
    }




}