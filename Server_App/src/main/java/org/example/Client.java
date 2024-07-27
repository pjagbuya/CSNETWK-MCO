package org.example;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private String alias;

    private Boolean isOnBroadCastChat;

    private DataOutputStream dosWriter;

    private DataInputStream disReader;
    private BufferedReader bisReader;

    private BufferedWriter bosWriter;



    public Client(Socket socket, String alias, DataOutputStream dosWriter, DataInputStream disReader, BufferedWriter bosWriter, BufferedReader bisReader) throws IOException {
        this.socket = socket;
        this.alias = alias;
        this.dosWriter = dosWriter;
        this.disReader = disReader;
        this.bisReader =bisReader;
        this.bosWriter =bosWriter;
        this.isOnBroadCastChat = false;


    }

    public Boolean isOnBroadCastChat() {
        return isOnBroadCastChat;
    }

    public void setOnBroadCastChat(Boolean onBroadCastChat) {
        isOnBroadCastChat = onBroadCastChat;
    }

    public DataInputStream getDisReader() {
        return disReader;
    }

    public DataOutputStream getDosWriter() {
        return dosWriter;
    }

    public BufferedReader getBisReader() {
        return bisReader;
    }

    public BufferedWriter getBosWriter() {
        return bosWriter;
    }

    public String getAlias() {
        return alias;
    }
}
