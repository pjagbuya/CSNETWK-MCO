package ServerSide;
import java.net.*;
import java.io.*;
import java.util.*;

import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;

public class SocketHandler extends Thread{
    private boolean isAvailable = true;
    public static ArrayList<String> aliases;
    private Socket socket;
    private int socketID;
    public SocketHandler(int socketID){

        this.socketID = socketID;
        this.aliases = new ArrayList<>();
        this.isAvailable = false;

    }



    public boolean isSocketAvailable(){
        return isAvailable;
    }
    public int getSocketID() {
        return socketID;
    }

    @Override
    public void run(){

        startAccepting();
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
    private void startAccepting(){
        try (ServerSocket serverSocket = new ServerSocket(this.socketID)) {
            Socket serverEndpoint = serverSocket.accept();

            SocketHandler newSocketHandler = new SocketHandler(this.socketID);
            newSocketHandler.run();
            System.out.println("Server: New client connected: " + serverEndpoint.getRemoteSocketAddress());

			DataOutputStream dosWriter = new DataOutputStream(serverEndpoint.getOutputStream());
			DataInputStream disReader = new DataInputStream(serverEndpoint.getInputStream());
            String userRequestType = disReader.readUTF()+ " ";
            String[] commandWords = userRequestType.split("\\s+");
            String type = commandWords[0];

            switch (type) {

                case "/leave":
                
                break;
                case "/register":
                    String user =commandWords[1];

                    if (doesContainAlias(user)){
                        dosWriter.writeUTF(Paint.paintTextGreen("Welcome " + user +"!"));
                    }else{
                        dosWriter.writeUTF(Paint.paintTextOrange("Error: Registration failed, " + user +" already exists."));
                    }
                    
                    break;
                case "/store":
                
                break;   
                case "/dir":
                    
                    break;
                case "/get":
                
                break;             
                default:
                    dosWriter.writeUTF(Paint.paintTextOrange("Errpr: Command Not Found!"));
                    break;
            }

        } catch (Exception e) {

            e.printStackTrace();
        } finally{
            Paint.printTextOrange("Server: Connection is terminated.");
        }

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
