package org.example;

public class ServerController {
    public static ServerView serverView;
    public static ServerModel serverModel;
    public static SocketHandler socketHandler;

    final static String SERVER_IP = "127.0.0.1";

    public ServerController(ServerView serverView){

        this.serverView = serverView;

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
            Thread t = new Thread(() -> {socketHandler.startAccepting();});
            t.start();

        }catch (NumberFormatException e){
            ServerController.serverView.showErrorBox("Inputted the a non-digit as socket number");

        }

    }
}
