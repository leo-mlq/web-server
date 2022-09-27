import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Request {
    private String httpMethod;
    private String requestUrl;
    private String httpVersion;
    private Map<String, String> map_reqHeaders;
    private BufferedReader bufferedReader;

    public Request(String[] requestArgs, BufferedReader bufferedReader) throws IOException{
        this.bufferedReader=bufferedReader;
        this.map_reqHeaders=new HashMap<>();
        this.httpMethod=requestArgs[0];
        this.requestUrl=requestArgs[1].compareTo("/")==0?"/index.html":requestArgs[1];
        this.httpVersion=requestArgs.length==3?requestArgs[2]:"HTTP/1.0";
        parseHeaders();
    }

    private void parseHeaders() throws IOException{
        String line;
        while((line=this.bufferedReader.readLine()).compareTo("")!=0){
            System.out.println("Total Threads: "+Thread.activeCount()+" Thread "+Thread.currentThread().getId()+" "+Thread.currentThread().getName()+" Client: "+line);
            int idx_colon=line.indexOf(":");
            int idx_space=line.indexOf(" ");
            this.map_reqHeaders.put(line.substring(0, idx_colon),line.substring(idx_space+1));
        }
    }

    public String getHttpMethod() {
        return this.httpMethod;
    }

    public String getRequestUrl() {
        return this.requestUrl;
    }

    public String getHttpVersion() {
        return this.httpVersion;
    }

    public Map<String, String> getMap_reqHeaders() {
        return this.map_reqHeaders;
    }
}
