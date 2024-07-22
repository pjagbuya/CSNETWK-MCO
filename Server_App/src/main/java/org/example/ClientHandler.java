package org.example;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.nio.file.Paths;
import java.util.*;

class ClientHandler implements Runnable {
    final static String SERVER_FILE_PATH = ".\\src\\main\\java\\org\\example\\server_files";
    public static ArrayList<String> serverChatlogs = new ArrayList<>();

    private final Socket serverEndpoint;
    private ServerSocket serverSocket;

    private String alias;
    private ArrayList<String> aliases;

    private DataOutputStream dosWriter;

    private DataInputStream disReader;

    private HashMap<Integer, Runnable> actionMap;


    /* function code to send when interacting with client
    * 0 - register
    * 1 - getFile
    * 2 - storeFile
    * 3 - getDirectory
    * 4 - chat
    * -1 - disconnect
    * */
    public ClientHandler(Socket clientSocket, ServerSocket serverSocket, ArrayList<String> aliases) throws IOException {
        this.serverEndpoint = clientSocket;
        this.serverSocket = serverSocket;
        this.aliases = aliases;
        this.disReader = new DataInputStream(this.serverEndpoint.getInputStream());
        this.dosWriter = new DataOutputStream(this.serverEndpoint.getOutputStream());
        this.actionMap = new HashMap<>();
        actionMap.put(-1, this::disconnect);
        actionMap.put(0, this::register);  // References an existing method
        actionMap.put(1, this::getFile);
        actionMap.put(2, this::storeFile);
        actionMap.put(3, this::getDirectory);
        actionMap.put(4, this::chat);
    }
    private void disconnect() {
        try
        {

            String reg_alias = this.disReader.readUTF();
            if(reg_alias.length() > 0)
                aliases.remove(reg_alias);

            this.serverEndpoint.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    private void getFile() {
    }

    private void getDirectory() {
        try{
            String dir = showDirectory();
            this.dosWriter.writeUTF(dir);
        }catch(Exception e){
            System.out.println("ERROR! Interrupted File Directory showing");
        }


    }

    private void chat() {
    }

    private void storeFile() {
    }

    private void register() {

        try{
            String alias = disReader.readUTF();
            System.out.println("Received alias: " + alias);
            if(this.aliases.contains(alias)){
                dosWriter.writeInt(0);
            }
            else{
                dosWriter.writeInt(1);
                this.alias = alias;
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

                Runnable action = this.actionMap.get(funcCode);

                action.run();
            }




        } catch (Exception e) {
            e.printStackTrace();
        }
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
