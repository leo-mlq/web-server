import java.util.Date;

public class Response {    
    String httpVersion;
    int code;
    String content;
    String contentType;
    long contentLength;

    public Response(){
        this.httpVersion="HTTP/1.0";
        this.code=0;
        content=null;
        this.contentType="";
        this.contentLength=0l;
    }
    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setContent(String content){
        this.content=content;
        setContentLength(content.getBytes().length);
    }

    public void setContentType(String contentType){
        this.contentType=contentType;
    }

    public void setContentLength(long contentLength){
        this.contentLength=contentLength;
    }
    public String getResponse(){
        StringBuilder sb_res=new StringBuilder();
        sb_res.append(httpVersion).append(" ").append(code).append(" ");
        switch (code){
            case 200:
            sb_res.append("OK");
            break;
            case 400:
            sb_res.append("Bad Request");
            break;
            case 403:
            sb_res.append("Forbidden");
            break;
            case 404:
            sb_res.append("Not Found");
            break;
            case 405:
            sb_res.append("Method Not Allowed");
            break;
            case 408:
            sb_res.append("Request Timeout");
            break;
            case 500:
            sb_res.append("Server Error");
            break;
        }
        sb_res.append(System.lineSeparator());

        //header information
        sb_res.append("Date: ").append(new Date()).append(System.lineSeparator());
        
        if(contentType.compareTo(Utils.TYPE_TXT)==0 || contentType.compareTo(Utils.TYPE_HTML)==0){
            //sb_res.append("Accept-Ranges: bytes").append(System.lineSeparator());
        
            sb_res.append("Content-Type: ").append(contentType).append("; charset=UTF-8").append(System.lineSeparator());
        }
        else sb_res.append("Content-Type: ").append(contentType).append(System.lineSeparator());
        //sb_res.append("Content-Encoding: gzip").append(System.lineSeparator());
        //content information
        sb_res.append("Content-Length: ").append(contentLength).append(System.lineSeparator());
        sb_res.append(System.lineSeparator());
        if(content!=null) sb_res.append(content).append(System.lineSeparator());
        // sb_res.append(System.lineSeparator());
        return sb_res.toString();
    }

}
