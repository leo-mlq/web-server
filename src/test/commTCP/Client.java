package test.commTCP;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
      
        Socket socket = new Socket("127.0.0.1", 8888);

        /*input interprets  from right to left */
        //system inputstream (byte form - 8 bits) to Line-Oriented writer (character 2 bytes)
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        //bufferedReader provides readline function;
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        /*output interprets from left to right */
        //converts characters into bytes for socket to transmitt.
        OutputStreamWriter outputStreamWriter=new OutputStreamWriter(socket.getOutputStream());
        //bufferWrited writes the whole line
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

        InputStream inputStream = socket.getInputStream();

        String line;
        while((line = bufferedReader.readLine()) != null){
            if("quit".equals(line))
                break;
            bufferedWriter.write(line);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            byte []bytes = new byte[1024];
        int len = inputStream.read(bytes);
        System.out.println("server: "+new String(bytes,0,len));

        }
        
        socket.close();

    }
}