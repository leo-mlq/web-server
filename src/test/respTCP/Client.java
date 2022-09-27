package test.respTCP;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8888);
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("Hi server".getBytes());

        //receive from server
        InputStream inputStream = socket.getInputStream();
        byte []bytes = new byte[1024];
        int len = inputStream.read(bytes);
        System.out.println("server: "+new String(bytes,0,len));

        socket.close();

    }
}
