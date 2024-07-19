
import java.net.*;
import java.io.*;
import java.util.*;

import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;

public class User {
    
    public static void main(String[] args) {

        String sServerAddress = args[0];
		int nPort = Integer.parseInt(args[1]);

		
		try
		{
			Socket clientEndpoint = new Socket(sServerAddress, nPort);

			
			System.out.println("Client: Connected to server at " + clientEndpoint.getRemoteSocketAddress());
			System.out.println();
			DataOutputStream dosWriter = new DataOutputStream(clientEndpoint.getOutputStream());
			dosWriter.writeUTF("Client: Hello from client" + clientEndpoint.getLocalSocketAddress());
			
			DataInputStream disReader = new DataInputStream(clientEndpoint.getInputStream());

			// Setup and hear the filesize
			int fileSize = disReader.readInt();
			byte[]  fileContent = new byte[fileSize];
			// Knowing the file size helps the readFully function not hit the EOF
			disReader.readFully(fileContent);
			
			


			System.out.println("Client: Downloaded file \"Received.txt\"");
			System.out.println();
			fos.write(fileContent);



			
			fos.close();

			clientEndpoint.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			System.out.println("Client: Connection is terminated.");
		}
        
    }

    private void registerAlias(){

    }
    private void fileUpload(){

    }
    private void fileDownload(){
        
    }

}
