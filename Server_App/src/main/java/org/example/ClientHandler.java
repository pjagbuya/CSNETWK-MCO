package org.example;

import javafx.application.Platform;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

class ClientHandler extends Thread {
    final static String SERVER_FILE_PATH = ".\\org\\example\\server_files";
    public static HashMap<String, String> serverChatlogs = new HashMap<String, String>();

    private boolean isInChatUni = false;

    private volatile boolean isConnected;

    private final Socket serverEndpoint;

    private HashMap<String, Client> userNetworkMaps;


    private ServerSocket serverSocket;

    private SocketHandler server;

    private String alias;
    private ArrayList<String> aliases;

    private DataOutputStream dosWriter;

    private DataInputStream disReader;


    private BufferedReader bisReader;

    private BufferedWriter bosWriter;
    private HashMap<Integer, Runnable> actionMap;
    private ServerView serverView;

    private Client myClient;


    /* function code to send when interacting with client
    * 0 - register
    * 1 - getFile
    * 2 - storeFile
    * 3 - getDirectory
    * 4 - chat
    * -1 - disconnect
    * */
    public ClientHandler(ServerView serverView, SocketHandler server, Socket clientSocket, ServerSocket serverSocket, ArrayList<String> aliases, HashMap<String, Client> userNetworkMaps) throws IOException {
        this.server = server;
        this.serverView = serverView;
        this.serverEndpoint = clientSocket;
        this.serverSocket = serverSocket;
        this.aliases = aliases;
        this.disReader = new DataInputStream(this.serverEndpoint.getInputStream());
        this.dosWriter = new DataOutputStream(this.serverEndpoint.getOutputStream());
        this.bisReader = new BufferedReader(new InputStreamReader(this.serverEndpoint.getInputStream()));
        this.bosWriter = new BufferedWriter(new OutputStreamWriter(this.serverEndpoint.getOutputStream()));
        this.isConnected = this.serverEndpoint.isConnected();;
        this.myClient = new Client(this.serverEndpoint, this.alias, this.dosWriter, this.disReader, this.bosWriter, this.bisReader);

        this.userNetworkMaps = userNetworkMaps;


        this.actionMap = new HashMap<>();
        actionMap.put(-2, this::quit_unicast);
        actionMap.put(-1, this::disconnect);
        actionMap.put(0, this::register);  // References an existing method
        actionMap.put(1, this::getFile);
        actionMap.put(2, this::storeFile);
        actionMap.put(3, this::getDirectory);
        actionMap.put(4, this::chat);
        actionMap.put(41, this::send_aliases);
        actionMap.put(42, this::chat_person);
    }

    private void quit_unicast(){
        try
        {

            this.dosWriter.writeInt(-999);

        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void disconnect() {
        try
        {
            this.isConnected = false;
            String reg_alias = this.disReader.readUTF();

            if(reg_alias.length() > 0 && !aliases.isEmpty()){
                this.aliases.remove(reg_alias);
                this.userNetworkMaps.remove(alias);
            }

            this.disReader.close();
            this.dosWriter.close();
            this.serverEndpoint.close();

        }catch (IOException e){
            e.printStackTrace();
        }

    }
    private void getFile() {
        try {
            String fileName = disReader.readUTF();
            File file = new File(SERVER_FILE_PATH + "\\" + fileName);
            System.out.println("Actually in getFile");
            if (file.exists() && file.isFile()) {
                String fileFormatted = appendTxtExtension(fileName);
                byte[] fileContent = Files.readAllBytes(Paths.get(SERVER_FILE_PATH+"\\"+fileFormatted));
                System.out.println("File does exist");
                dosWriter.writeInt(fileContent.length);
                dosWriter.write(fileContent);
                dosWriter.writeUTF("SUCCESS: File " + fileName + " retrieved.");

            } else {
                dosWriter.writeUTF("ERROR: File not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getDirectory() {
        try{
            String dir = showDirectory();
            this.dosWriter.writeUTF(dir);
        }catch(Exception e){
            System.out.println("ERROR! Interrupted File Directory showing");
        }


    }
    private void send_aliases() {

        try{
            for (Map.Entry<String, Client> entry : this.userNetworkMaps.entrySet()) {
                String alias = entry.getKey();
                Client client = entry.getValue();

                this.dosWriter.writeUTF(alias);

            }
            this.dosWriter.writeUTF("-1");




        }catch (IOException e){
            e.printStackTrace();
        }


    }

    private void chat_person() {
        try
        {

            String person_to_chat = disReader.readUTF();
            String msg = disReader.readUTF();
            Client clientBDis = userNetworkMaps.get(person_to_chat);
            System.out.println("Client sent message to this person");
            System.out.println(person_to_chat);
            System.out.println(msg);

            DataOutputStream clientBDosWriter = clientBDis.getDosWriter();
            clientBDosWriter.writeInt(999);

            clientBDosWriter.writeUTF(this.alias);
            clientBDosWriter.writeUTF(msg);
            clientBDosWriter.flush();

//127.0.0.1

        }catch(Exception e){
            e.printStackTrace();
        }





    }
    private void chat() {



        try{
            String msg = this.disReader.readUTF();
            serverChatlogs.put(this.alias, msg);
            for (Map.Entry<String, Client> entry : userNetworkMaps.entrySet()) {
                String clientId = entry.getKey();
                Client client = entry.getValue();

                if(!this.alias.equalsIgnoreCase(clientId))
                {
                    DataOutputStream dosBWriter = client.getDosWriter();
                    dosBWriter.writeInt(777);
                    dosBWriter.writeUTF(this.alias);
                    dosBWriter.writeUTF(msg);

                }
            }

            Platform.runLater(()->{
                this.serverView.addChatText(this.alias + ": " +msg);
            });
        }catch (IOException e){
            e.printStackTrace();
        }




    }
    private String appendTxtExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex != -1 && lastDotIndex == filename.length() - 4) {

            return filename;
        } else {
            return filename + ".txt";
        }
    }

    private void storeFile() {
        try {
            String fileName = disReader.readUTF();
            FileOutputStream fos = new FileOutputStream(SERVER_FILE_PATH+"\\"+ appendTxtExtension(fileName));
            int fileSize = disReader.readInt();
            byte[] fileContent = new byte[fileSize];
            disReader.readFully(fileContent);
            fos.write(fileContent);
            fos.close();

            Platform.runLater(()->serverView.updateDirectory(this.serverView.showDirectory()));

            dosWriter.writeUTF("SUCCESS: File " + fileName + " stored.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void register() {

        try{
            if(this.aliases.isEmpty()){
                System.out.println("No user to compare");
            }else{
                for(String s : this.aliases){
                    System.out.println(s);
                }
            }

            String alias = disReader.readUTF();
            System.out.println("Received alias: " + alias);
            if(this.aliases.contains(alias)){
                dosWriter.writeInt(2); //alias is taken code
            }
            else{
                dosWriter.writeInt(1);
                this.alias = alias;
                this.aliases.add(this.alias);
                this.userNetworkMaps.put(this.alias, this.myClient);
            }

        }catch (IOException e){
            e.printStackTrace();

        }


    }

    @Override
    public void run() {
        System.out.println("Client connected");

        try {
            int funcCode = 999;
            while(funcCode != -1){


                funcCode = this.disReader.readInt();
                if (funcCode == -999){      // If one of the two clients are still contact but on disconnects
                    String tellThisPersonImGone = this.disReader.readUTF();
                    Client clientContact = userNetworkMaps.get(tellThisPersonImGone);
                    DataOutputStream dosBWriter = clientContact.getDosWriter();

                    dosBWriter.writeInt(-111);
                    dosBWriter.writeUTF(this.alias);
                    this.dosWriter.writeInt(-999);
                    this.myClient.setOnBroadCastChat(false);
                    continue;
                }else if(funcCode == -888){ //Person is the only one left in chatroom
                    this.myClient.setOnBroadCastChat(false);
                    this.dosWriter.writeInt(-999);
                    continue;
                }else if (funcCode == 111){ // broadcast wants to reload the server chat logs
                    this.myClient.setOnBroadCastChat(true);
                    for (Map.Entry<String, String> entry : serverChatlogs.entrySet()) {
                        this.dosWriter.writeInt(1);
                        String user = entry.getKey();
                        String msg = entry.getValue();
                        this.dosWriter.writeUTF(user);
                        this.dosWriter.writeUTF(msg);

                    }
                    this.dosWriter.writeInt(-1);
                    continue;
                }
                Runnable action = this.actionMap.get(funcCode);

                action.run();
            }




        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addServerChatlogs(String user, String msg){
        serverChatlogs.put(user, msg);
    }
    private String showDirectory(){

        URI directoryUri = Paths.get(SERVER_FILE_PATH).toUri();
        File directory = new File(directoryUri);
        StringBuilder directoryListing = new StringBuilder("Server Directory: \n");

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            List<File> fileList = Arrays.asList(files);
            Collections.reverse(fileList);

            if (fileList != null) {
                for (File file : fileList) {
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
}
