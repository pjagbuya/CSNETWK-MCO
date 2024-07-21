package org.example;

import java.io.File;
import java.util.*;


import org.example.UnivNodes.*;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

//    @Override
//    public void start(Stage stage) {
//        String javaVersion = System.getProperty("java.version");
//        String javafxVersion = System.getProperty("javafx.version");
//        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
//        Scene scene = new Scene(new StackPane(l), 640, 480);
//        stage.setScene(scene);
//        stage.show();
//    }

    final static String[] CMDS = {"/dir", "/?", "/add"};
    final static String SERVER_FILE_PATH = "server_files";
    final static String SERVER_IP = "127.0.0.1";
    public String getGreeting() {
        return "Hello World!";
    }
    public static void main(String[] args) {
        launch(args); // Launch the application with the main class
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Stage window = primaryStage;
        ServerView serverView = new ServerView(window);
        ServerController serverController = new ServerController(serverView);

        primaryStage.show();
    }

    private void closeProgram(Stage window)
    {   ConfirmBox boxMessage = new ConfirmBox();
        boolean answer = boxMessage.display("Warning", "Are you sure you want to exit?");


        if(answer)
            window.close();
    }

//    private void addSocketToDB(int sockNum, ArrayList<Integer> serverSocketsID, ArrayList<SocketHandler> socketHandlers){
//
//        String resp;
//        if (serverSocketsID.isEmpty()){
//
//            serverSocketsID.add(sockNum);
//
//            // add user socket end point here
//            SocketHandler tempSocket = new SocketHandler(sockNum);
//            tempSocket.run();
//            socketHandlers.add(tempSocket);
//        }
//
//
//    }

    // private void addNewSocket(Scanner sc, String userInput, ArrayList<Integer> serverSocketsID, ArrayList<SocketHandler> socketHandlers)
//    private void addNewSocket(Scanner sc, ArrayList<Integer> serverSocketsID, ArrayList<SocketHandler> socketHandlers){
//
//        boolean flag = true;
//        String userInput;
//        while (flag){
//            System.out.print("Input a new socket number (/x for exit): ");
//            userInput = sc.nextLine();
//            if (userInput.equalsIgnoreCase("/x") ==true){
//                flag = false;
//            }
//            else{
//                try{
//                    int sockNum = Integer.parseInt(userInput);
//                    System.out.println();
//                    System.out.println("Adding new Socket number " + sockNum + "...");
//
//                    addSocketToDB(sockNum, serverSocketsID, socketHandlers);
//
//                }catch (NumberFormatException e){
//                    System.out.println("Wrong input");
//                    e.printStackTrace();
//
//
//                }
//            }
//        }
//
//
//    }

    private void showDirectory(){


        File directory = new File(SERVER_FILE_PATH);
        StringBuilder directoryListing = new StringBuilder("Server Directory: ");

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    directoryListing.append(file.getName()).append("\n");
                }
            } else {
                directoryListing.append(Paint.paintTextRed("Failed to list contents\n"));
            }
        } else {
            directoryListing.append(Paint.paintTextRed("Directory not found\n"));
        }

        String listingString = directoryListing.toString();
    }

    private void showServerHelp(){
        StringBuilder commandsBuilder = new StringBuilder();
        commandsBuilder.append("/dir - shows current files in the directory of file storage for the server\n");
        commandsBuilder.append("/add - adds a socket to the network\n");
        commandsBuilder.append("/deleteall - deletes all sockets\n");
        commandsBuilder.append("/x - exits current prompt");

        String commands = commandsBuilder.toString();

    }
}