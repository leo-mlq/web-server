import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Map;




class ServerThread implements Runnable{
    private Socket socket;
    private String documentRoot;
    public ServerThread(Socket socket,String documentRoot){
        this.socket=socket;
        this.documentRoot=documentRoot;
    }
    @Override
    public void run() {
        InputStream inputStream=null;
        InputStreamReader inputStreamReader=null;
        BufferedReader bufferedReader=null;
        OutputStream outputStream=null;
        OutputStreamWriter outputStreamWriter=null;
        BufferedWriter bufferedWriter=null;
        Response res_cur=new Response();
        /*default is HTTP/1.0*/
        String httpVersion="HTTP/1.0";
        try{
            /*make timeout based on number of threads */
            int numActiveThreads=Thread.activeCount()-1; //minus main thread;
            int timeoutMs=Utils.getSocketTimeout(numActiveThreads);
            socket.setSoTimeout(timeoutMs);
            inputStream = socket.getInputStream();
            inputStreamReader=new InputStreamReader(inputStream);
            bufferedReader=new BufferedReader(inputStreamReader);

            outputStream=socket.getOutputStream(); 
            outputStreamWriter=new OutputStreamWriter(outputStream);
            bufferedWriter = new BufferedWriter(outputStreamWriter);
            
            String line;
            while((line=bufferedReader.readLine())!=null){
                System.out.println("Total Threads: "+Thread.activeCount()+" Thread "+Thread.currentThread().getId()+" "+Thread.currentThread().getName()+" Client: "+line);
                String[] requestArgs=line.split(" ");
                
                if(requestArgs.length<2 || requestArgs.length>3){
                    //System.out.println("should not print");
                    res_cur.setCode(400);
                    res_cur.setHttpVersion("HTTP/1.0");
                    res_cur.setContentType(Utils.TYPE_HTML);
                    res_cur.setContent("Bad Request.");
                    break;
                }
                Request request_cur=new Request(requestArgs, bufferedReader);
                String httpMethod=request_cur.getHttpMethod();
                String requestUri=request_cur.getRequestUrl();
                httpVersion=request_cur.getHttpVersion();
                //Map<String,String> map_headers=request_cur.getMap_reqHeaders();
                //System.out.println(map_headers);
                if(httpMethod.compareTo("GET")!=0){
                    res_cur.setCode(405);
                    res_cur.setHttpVersion(httpVersion);
                    res_cur.setContentType(Utils.TYPE_HTML);
                    res_cur.setContent("Method Not Allowed.");
                    break;
                } 
                if(httpVersion.compareTo("HTTP/1.0")!=0 &&
                    httpVersion.compareTo("HTTP/1.1")!=0
                ){
                    res_cur.setCode(400);
                    res_cur.setHttpVersion("HTTP/1.0");
                    res_cur.setContentType(Utils.TYPE_HTML);
                    res_cur.setContent("Bad Request.");
                    break;
                }
                else{
                    /*read the file */
                    //path must be absolute path. e.g. "C:/Users/john/workspace/HttpServer for Windows", "/home/moazzeni/webserver_files" for Unix
                    String filePath=documentRoot+requestUri;
                    File f=new File(filePath);
                    FileInputStream fileInputStream=null;
                    InputStreamReader inputStreamReader2=null;
                    Response res_file=new Response();
                    try{
                        /*not found, response http 404 */
                        if(!(f.exists()==true && f.isDirectory()!=true)){
                            res_file.setCode(404);
                            res_file.setHttpVersion(httpVersion);
                            res_file.setContentType(Utils.TYPE_HTML);
                            res_file.setContent("Request not found.");
                            bufferedWriter.write(res_file.getResponse());
                            bufferedWriter.flush();
                        }
                        else{
                            String fileType=Utils.getContentType(requestUri);
                            File file=new File(filePath);
                           
                            if(fileType.compareTo(Utils.TYPE_GIF)==0 || 
                                fileType.compareTo(Utils.TYPE_JPG)==0 || 
                                fileType.compareTo(Utils.TYPE_PNG)==0 || 
                                fileType.compareTo(Utils.TYPE_SVG)==0){
                                    
                                fileInputStream = new FileInputStream(file);
                                byte[] bytes = new byte[1024];                    
                                res_file.setCode(200);
                                res_file.setHttpVersion(httpVersion);
                                res_file.setContentType(Utils.getContentType(requestUri));
                                res_file.setContentLength(fileInputStream.available());
                                outputStream.write(res_file.getResponse().getBytes());
                                outputStream.flush();
                                int length = 0;
                                while ((length = fileInputStream.read(bytes)) != -1) {
                                    outputStream.write(bytes,0,length);
                                }
                                outputStream.flush();
                            }
                            else{
                                inputStreamReader2=new InputStreamReader(new FileInputStream(file), "UTF-8");
                                char[] chars = new char[1024];
                                StringBuilder sb_cur = new StringBuilder();
                                int length;
                                while ((length = inputStreamReader2.read(chars)) != -1) {
                                    sb_cur.append(chars, 0, length);
                                }
                                res_file.setCode(200);
                                res_file.setHttpVersion(httpVersion);
                                res_file.setContentType(Utils.getContentType(requestUri));
                                res_file.setContent(sb_cur.toString());
                                bufferedWriter.write(res_file.getResponse());
                                bufferedWriter.flush();
                                
                            }
                        }
                    }catch(Exception e){
                        /*forbidden/no access to file, response http 403 */
                        if(e instanceof FileNotFoundException){
                            res_file.setCode(403);
                            res_file.setHttpVersion(httpVersion);
                            res_file.setContentType(Utils.TYPE_HTML);
                            res_file.setContent("Invalid credential.");
                            bufferedWriter.write(res_file.getResponse());
                            bufferedWriter.flush();

                        }
                        else throw e;
                    }finally{
                        if(fileInputStream!=null) fileInputStream.close();
                        if(inputStreamReader2!=null) inputStreamReader2.close();
                    }
                    
                }
                /*above should not terminate the connection if HTTP/1.1*/
                if(httpVersion.compareTo("HTTP/1.1")!=0) break;
            }
        }catch(Exception e){
            
            if(e instanceof SocketTimeoutException){
                /*send timeout? 408 Request Timeout?*/
                //System.out.println("Timeout no inputs from client");
                res_cur.setCode(408);
                res_cur.setHttpVersion(httpVersion);
                res_cur.setContentType(Utils.TYPE_HTML);
                res_cur.setContent("Timeout due to inactivity.");
            }
            else if(e instanceof IOException || e instanceof SocketException){
                /*server error, response http 500 */
                res_cur.setCode(500);
                res_cur.setHttpVersion(httpVersion);
                res_cur.setContentType(Utils.TYPE_HTML);
                res_cur.setContent("Internal Server Error.");
            }
            else e.printStackTrace();
            
        }finally{
            try{
                if(bufferedWriter!=null){
                    /*send "Connection closed by foreign host." to client*/
                    if(res_cur.code!=0) bufferedWriter.write(res_cur.getResponse());
                    bufferedWriter.flush();
                    bufferedWriter.close();
                }
                if(bufferedReader!=null) bufferedReader.close();
                if(socket!=null) socket.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}

public class Server {
    
    private int port;
    private String documentRoot;
    //private ServerSocket serverSocket;
    private boolean isRunning;


    public Server(int port, String documentRoot){
        this.port=port;
        this.documentRoot=documentRoot;
        this.isRunning=false;
    }
    public void start(){
        isRunning=true;
        ServerSocket serverSocket=null;
        try{
            serverSocket = new ServerSocket(port);
            //always accept new connection.
            while(isRunning){
                Socket socket_cur=serverSocket.accept();
                Runnable r_cur=new ServerThread(socket_cur,documentRoot);
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

    public void stop(){ isRunning=false;}

    public static void main(String[] args){
        Map<String, String> map_args=null;
        try{
            map_args=Utils.parseServerArgs(args);
            String documentRoot=map_args.get(Utils.DOCROOT_FLAG);
            String port=map_args.get(Utils.PORT_FLAG);
            System.out.println("port: "+port+". document root: "+documentRoot);
            Server server=new Server(Integer.parseInt(port), documentRoot);
            server.start();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
