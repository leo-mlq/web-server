package test.respTCP;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        Socket accept = serverSocket.accept();
        InputStream inputStream = accept.getInputStream();
        byte []bytes = new byte[1024];
        int len = inputStream.read(bytes);
        String str_recieved=new String(bytes,0,len);
        System.out.println("client: "+str_recieved);

        OutputStream outputStream = accept.getOutputStream();
        outputStream.write(("received "+str_recieved).getBytes());

        accept.close();
        serverSocket.close();
    }
}