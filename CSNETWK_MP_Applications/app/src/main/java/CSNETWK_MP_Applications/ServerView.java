package CSNETWK_MP_Applications;

import CSNETWK_MP_Applications.UnivNodes.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class ServerView{

    final static int WIDTH = 1200;
    final static int HEIGHT = 600;
    final static String IP_LOCAL = "127.0.0.1";
    final static String BG_COLOR = "#ECFFE6";
    private BorderPane bgPane;
    private Stage parentWin;
    private Scene startScene;
    private static final MenuButton BTN_CREATE = new MenuButton("Add Socket");
    private static final MenuButton BTN_TESTF = new MenuButton("Close Sockets");
    private static final MenuButton BTN_EXIT = new MenuButton("Exit");

    public ServerView(Stage parentWin)
    {
        this.parentWin = parentWin;
        this.bgPane = new BorderPane();
        this.startScene = new Scene(bgPane, WIDTH, HEIGHT);


        VBox LabelsAndInputs = new VBox(20);

        HBox btnSection = new HBox(20);



        btnSection.setAlignment(Pos.BOTTOM_CENTER);
        btnSection.getChildren().addAll(BTN_CREATE, BTN_TESTF, BTN_EXIT);



        Label inputLabel = new Label("Input name of socket to listen");

        Label ipLabel = new Label("Server IP: " + IP_LOCAL);
        TextField inputField = new TextField();
        LabelsAndInputs.getChildren().addAll(ipLabel, inputLabel, inputField);
        LabelsAndInputs.setAlignment(Pos.CENTER);
        this.bgPane.setCenter(LabelsAndInputs);

        this.bgPane.setBottom(btnSection);
        this.bgPane.setStyle("-fx-base: " + BG_COLOR+ ";");
        this.parentWin.setWidth(WIDTH);
        this.parentWin.setHeight(HEIGHT);
        this.parentWin.setTitle("Server Application");
        this.parentWin.setScene(startScene);


    }

    public void showScene(){
        
    }



}