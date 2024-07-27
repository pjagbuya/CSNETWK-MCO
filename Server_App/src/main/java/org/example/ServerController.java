package org.example;

import javafx.stage.Stage;

public class ServerController {
    private Stage stage;
    public static ServerView serverView;
    public static ServerModel serverModel;
    public static SocketHandler socketHandler;

    final static String SERVER_IP = "127.0.0.1";

    public ServerController(ServerView serverView, Stage stage){

        this.serverView = serverView;
        this.stage = stage;

        this.stage.setOnCloseRequest(e->{
            if(socketHandler != null){
                try {
                    socketHandler.close();
                } catch (Exception err) {
                    err.printStackTrace();
                }
            }

        });


    }

    public static void disconnectSocket(){
        try {
            socketHandler.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void makeSocket(String userInput){

        try{
            int sockNum = Integer.parseInt(userInput);
            if (socketHandler != null){
                try {
                    socketHandler.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            socketHandler = new SocketHandler(sockNum, serverView);
            ServerController.serverView.setServerActive(sockNum);

            ServerController.serverModel = new ServerModel(SERVER_IP, sockNum);
            Thread t = new Thread(socketHandler);
            t.start();

        }catch (NumberFormatException e){
            ServerController.serverView.showErrorBox("Inputted a non-digit as socket number");

        }

    }

    public static void msgServerUsers(String msg){
        socketHandler.broadCastEveryone(msg);
    }
}
