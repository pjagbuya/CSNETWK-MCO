import java.net.*;
import java.io.*;
import java.util.*;

import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;

public class SocketHandler extends Thread{
    private boolean isAvailable = true;
    private Socket socket;
    private int socketID;
    public SocketHandler(int socketID){

        this.socketID = socketID;
        this.isAvailable = false;

    }

    public SocketHandler(Socket socket, int socketID){
        this.socket = socket;
        this.socketID = socketID;

    }

    public boolean isSocketAvailable(){
        return isAvailable;
    }
    public int getSocketID() {
        return socketID;
    }

    @Override
    public void run(){

        testTimer();
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
