package ServerSide;
import java.net.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;


public class Server{
    final static String[] CMDS = {"/dir", "/?", "/add"};
    final static String SERVER_FILE_PATH = "server_files";
    final static String SERVER_IP = "127.0.0.1";
    public static void main(String[] args) {


        Scanner sc = new Scanner(System.in);

        Server server = new Server();


        // Storing memorizable stuff
        ArrayList<String> aliases = new ArrayList<>();
        ArrayList<Integer> serverSocketsID = new ArrayList<>();         // List of users endpoints that are in the server
        ArrayList<SocketHandler> socketHandlers = new ArrayList<>();    // Socket object per user that enters, containing user names


        // User input for chat interaction
        String userInput;

        String serverIP = "127.0.0.1";
        System.out.println("Server is now active at localhost ("+serverIP+").");
        System.out.println("Commands for server application is \"/dir\" and \"/add\", you can quit with \"/x\"");
        System.out.println("Type \"/?\" if you wanna know more in commands and definitions");

        boolean flag = true;
        while (flag)
        {

            System.out.print("Input a command from the listed above: ");
            userInput = sc.nextLine();

            // User input processing
            switch (userInput.toLowerCase()) {
                case "/dir":
                    server.showDirectory();
                    break;
                case "/add":

                    server.addNewSocket(sc, serverSocketsID, socketHandlers);
                    break;
                case "/deleteall":
                    serverSocketsID.clear();
                    socketHandlers.clear();
                    break;
                case "/?":
                    server.showServerHelp();
                    break;
                default:
                    System.out.println("Invalid command. Please use /dir, /add, /deleteall, or /?");
                    break;
            }





            
        }


        sc.close();



       
    }
    private void addSocketToDB(int sockNum, ArrayList<Integer> serverSocketsID, ArrayList<SocketHandler> socketHandlers){

        String resp;
        if (serverSocketsID.isEmpty()){

            serverSocketsID.add(sockNum);

            // add user socket end point here
            SocketHandler tempSocket = new SocketHandler(sockNum);
            tempSocket.run();
            socketHandlers.add(tempSocket);
        }


    }

    // private void addNewSocket(Scanner sc, String userInput, ArrayList<Integer> serverSocketsID, ArrayList<SocketHandler> socketHandlers)
    private void addNewSocket(Scanner sc, ArrayList<Integer> serverSocketsID, ArrayList<SocketHandler> socketHandlers){
        
        boolean flag = true;
        String userInput;
        while (flag){
            System.out.print("Input a new socket number (/x for exit): ");
            userInput = sc.nextLine();
            if (userInput.equalsIgnoreCase("/x") ==true){
                flag = false;
            }
            else{
                try{
                    int sockNum = Integer.parseInt(userInput);
                    System.out.println();
                    System.out.println("Adding new Socket number " + sockNum + "...");

                    addSocketToDB(sockNum, serverSocketsID, socketHandlers);
                    
                }catch (NumberFormatException e){
                    System.out.println("Wrong input");
                    e.printStackTrace();
                    
        
                }
            }
        }


    }

    private void showDirectory(){


        File directory = new File(SERVER_FILE_PATH);
        System.out.println("Server Directory: ");

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    System.out.println(file.getName());
                }
            } else {
                System.out.println("Failed to list contents");
            }
        } else {
            System.out.println("Directory not found");
        }
    }

    private void showServerHelp(){
        System.out.println("/dir - shows current files in the directory of file storage for the server");
        System.out.println("/add - adds a socket to the network");
        System.out.println("/deleteall - deletes all sockets");
        System.out.println("/x - exits current prompt");

    }

    // private SocketHandler addSocketHandler(){
    //     SocketHandler sh = new SocketHandler()
    //     return 
    // }

}