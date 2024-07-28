package org.example;

import java.lang.reflect.Array;
import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.SwingUtilities;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;

public class SocketHandler implements AutoCloseable, Runnable{
    final static String SERVER_FILE_PATH = ".\\org\\example\\server_files";
    private boolean isAvailable = true;

    private AliasManager aliasManager;
    public static ArrayList<String> aliases;

    public static HashMap<String, Client> userNetworkMaps;

    public static ArrayList<ClientHandler> clientHandlers;

    public static ServerSocket serverSocket;

    private Socket socket;
    private String curDir;
    private File entireProjDir;
    private int socketID;
    private ServerView serverView;
    @Override
    public void close() throws Exception {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
            userNetworkMaps.clear();
            aliases.clear();
            System.out.println("Server socket on port " + socketID + " closed.");
        }
        isAvailable = false;

    }
    public SocketHandler(int socketID, ServerView serverView){

        this.socketID = socketID;
        this.serverView = serverView;
        this.aliasManager = new AliasManager();
        SocketHandler.aliases = new ArrayList<>();
        this.isAvailable = true;
        this.curDir = showDirectory();
        this.entireProjDir = new File(".");
        this.serverView.updateDirectory(showDirectory());
        this.userNetworkMaps = new HashMap<String, Client>();
        this.clientHandlers = new ArrayList<ClientHandler>();

    }



    public boolean isSocketAvailable(){
        return isAvailable;
    }
    public int getSocketID() {
        return socketID;
    }


    // Where the main processes of a server happens
    /**
     * Coding for the reading
     * /join for Connection - this is only checked in the client side
     * /leave for Disconnection
     * /register for Register
     * /store for Request server Directory
     * /dir for request file name
     * /get for download a file
     * 
     */
    public void startAccepting(){
        serverSocket = null; // Initialize server socket within the method
        // Update UI on the main thread using EventQueue
        String temp = "";

        try  {
            serverSocket = new ServerSocket(this.socketID);

            System.out.println("Server listening in Socket "+ this.socketID + " and is now accepting... ");
            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();


                System.out.println("A client has now connected");
                if (clientSocket != null) {
                    // Process client request (potentially lengthy)
                    String message = processClientRequest(clientSocket); // Example processing
                    updateServerUI(this.socketID, message);

                }
            }


        } catch (IOException | InterruptedException e) {
            if (e instanceof SocketTimeoutException) {
                System.out.println("Server timeout");
            } else {
                System.out.println("Server error");
            }
        }



    }

    public static ArrayList<ClientHandler> getClientHandlers() {
        return clientHandlers;
    }

    public void broadCastEveryone(String msg){
        try {
            for (Map.Entry<String, Client> entry : userNetworkMaps.entrySet()) {
                String clientId = entry.getKey();
                Client client = entry.getValue();
                if(client.isOnBroadCastChat()){
                    DataOutputStream dosWriter = client.getDosWriter();
                    dosWriter.writeInt(999);
                    dosWriter.writeUTF("SERVER");
                    dosWriter.writeUTF(msg);
                }

            }

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private String processClientRequest(Socket clientSocket) throws IOException, InterruptedException {
        ClientHandler clientHandler = new ClientHandler(this.serverView,this, clientSocket, this.serverSocket, this.aliases, this.userNetworkMaps);
        clientHandlers.add( clientHandler);
        clientHandler.start();



        return "Example message from client";
    }


    private String showDirectory(){

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

    private void updateServerUI(int socNum, String message) {


        SwingUtilities.invokeLater(() -> {
            this.serverView.updateServerMessage(socNum, message);

        });

    }
    private void validateStore(){

    }
    private void validateGet(){

    }
    private boolean doesContainAlias(String alias){
        if(aliases.contains(alias)){
            return true;
        }
        else{
            return false;
        }

    }


    @Override
    public void run() {
        startAccepting();
    }
}
