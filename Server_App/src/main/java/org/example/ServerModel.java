package org.example;

import java.io.File;
import java.util.ArrayList;


public class ServerModel {
    private ArrayList<String> chatLogs;
    private boolean isActive = false;
    private String localIP;
    private int sockNum;
    final static String SERVER_FILE_PATH = "server_files";
    private String directory;
    private String statusResponse;
    public ServerModel(String localIP, int sockNum){
        this.localIP = localIP;
        this.sockNum= sockNum;
        this.isActive = true;
        this.directory = updateDirectory();
        this.chatLogs = new ArrayList<>();

    }



    public ArrayList<String> addNewChatString(String msg){
        this.chatLogs.add(msg);
        return this.chatLogs;

    }
    // Passes Already listed file names in the directory
    public String updateDirectory(){


        File directory = new File(SERVER_FILE_PATH);
        StringBuilder directoryListing = new StringBuilder("");

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    directoryListing.append(file.getName()).append("\n");
                }
            } else {
                directoryListing.append(Paint.paintTextOrange("Failed to list contents\n"));
            }
        } else {
            directoryListing.append(Paint.paintTextOrange("Directory not found\n"));
        }

        String listingString = directoryListing.toString();
        return listingString;
    }

    public void disconnect() {
        this.isActive = false;
        this.chatLogs.clear();
        this.directory = "";
    }

    public int getSockNum() {
        return sockNum;
    }

    public String getLocalIP() {
        return localIP;
    }

    public boolean isSocketActive() {
         return this.isActive;
    }

    public String getStatusResponse() {
        if (isSocketActive()){
            return "Server: "+this.localIP+":"+this.sockNum + " is now active and listening";
        }
        return "Server: "+this.localIP+" is now inactive.";
    }
}
