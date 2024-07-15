import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;

public class ServerFileUpload {
    private String alias;
    private byte[] byteFile;
    private String serverResponse;
    private String serverBoolean;
    
    public ServerFileUpload(private String alias,
                            private Socket endPoint,
                            private byte[] byteFile)
    {

        this.alias = alias;
        this.endPoint = endPoint;
        this.byteFile= byteFile;

    }

    public String getAlias() {
        return alias;
    }
    public byte[] getByteFile() {
        return byteFile;
    }

    public void setServerResponse(String resp){
        serverResponse = resp;

    }
    public void setServerBoolean(boolean flag){
        this.serverBoolean = flag;
    }
    
}
