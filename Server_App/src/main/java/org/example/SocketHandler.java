package org.example;

import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.SwingUtilities;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;

public class SocketHandler implements AutoCloseable{
    final static String SERVER_FILE_PATH = ".\\src\\main\\java\\org\\example\\server_files";
    private boolean isAvailable = true;
    public static ArrayList<String> aliases;
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
            System.out.println("Server socket on port " + socketID + " closed.");
        }
        isAvailable = false;

    }
    public SocketHandler(int socketID, ServerView serverView){

        this.socketID = socketID;
        this.serverView = serverView;
        SocketHandler.aliases = new ArrayList<>();
        this.isAvailable = false;
        this.curDir = showDirectory();
        this.entireProjDir = new File(".");
        this.serverView.updateDirectory(showDirectory());

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

        try  {
            serverSocket = new ServerSocket(this.socketID);
            serverSocket.setSoTimeout(0);

            System.out.println("Server listening in Socket "+ this.socketID + " and is now accepting... ");
            while (isAvailable) {
                Socket clientSocket = serverSocket.accept();
                if (clientSocket != null) {
                    // Process client request (potentially lengthy)
                    String message = processClientRequest(clientSocket); // Example processing

                    // Update UI on the main thread using EventQueue
                    updateServerUI(this.socketID, message);

                    // ... other processing on client connection
                }
            }


        } catch (IOException e) {
            if (e instanceof SocketTimeoutException) {
                System.out.println("Server timeout");
            } else {
                System.out.println("Server error");
            }
        }



    }
    private String processClientRequest(Socket clientSocket) throws IOException {

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

    private void testTimer(){
        for (int i = 1; i <=3000; i++){
            System.out.println("Server listening in Socket "+ socketID + " and is now accepting... (running at "+i+"s )");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        }
    }
}
