package test.commTCP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

class ServerThread implements Runnable{
    private Socket socket;

    public ServerThread(Socket socket){
        this.socket=socket;
    }

    @Override
    public void run() {
        try{
            InputStream inputStream = socket.getInputStream();
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
            String line=bufferedReader.readLine();
            while(line!=null){
                System.out.println("Thread "+Thread.currentThread().getId()+" "+Thread.currentThread().getName()+" Client: "+line);
                line=bufferedReader.readLine();
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try{
                socket.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        
    }
    
}

public class Server {
    public static void main(String[] args){
        //port should read from args;
        int port=8888;
        
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(port);
            //always accept new connection.
            while(true){
                Socket socket_cur=serverSocket.accept();
                Runnable r_cur=new ServerThread(socket_cur);
                Thread thread_cur=new Thread(r_cur, socket_cur.toString());
                thread_cur.start();
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try{
                serverSocket.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        
    }
}
