import java.util.HashMap;
import java.util.Map;

public class Utils{
    public final static String PORT_FLAG="-port";
    public final static String DOCROOT_FLAG="-document_root";
    public final static String HTML_EXT=".html";
    public final static String JPG_EXT=".jpg";
    public final static String JPEG_EXT=".jpeg";
    public final static String TXT_EXT=".txt";
    public final static String GIF_EXT=".gif";
    public final static String TYPE_HTML="text/html";
    public final static String TYPE_TXT="text/plain";
    public final static String TYPE_JPG="image/jpeg";
    public final static String TYPE_GIF="image/gif";
    public static final String TYPE_PNG = "image/png";
    public static final String PNG_EXT = ".png";
    public static final String JS_EXT = ".js";
    public final static String TYPE_JS="application/javascript";
    public static final String CSS_EXT = ".css";
    public final static String TYPE_CSS="text/css";
    public static final String SVG_EXT = ".svg";
    public final static String TYPE_SVG="image/svg+xml";

    public static Map<String,String> parseServerArgs(String[] args){
        Map<String, String> map_ret=new HashMap<>();
        for(int i=0;i<args.length;i++){
            String str_cur=args[i];
            if(str_cur.charAt(0)=='-'){
                switch(str_cur.toLowerCase()){
                    case PORT_FLAG:
                        if(i+1>=args.length || args[i+1].charAt(0)=='-') throw new IllegalArgumentException("Expect arg after: "+args[i]);
                        map_ret.put(PORT_FLAG,args[i+1]);
                        i++;
                    break;
                    case DOCROOT_FLAG:
                        if(i+1>=args.length || args[i+1].charAt(0)=='-') throw new IllegalArgumentException("Expect arg after: "+args[i]);
                        //document root remove single and double quotes if they are in pair.
                        map_ret.put(DOCROOT_FLAG,args[i+1].replaceAll("^(['\"])(.*)\\1$", "$2"));
                        i++;
                    break;
                    default:
                        throw new IllegalArgumentException("Expect -document_root and -port and args");
                }
            }
        }
        if(map_ret.size()!=2) throw new IllegalArgumentException("Expect -document_root and -port and args");
        return map_ret;
    }
    public static String getContentType(String requestUri){
        String lowCaseReqUri=requestUri.toLowerCase();
        if(lowCaseReqUri.endsWith(JPG_EXT) || lowCaseReqUri.endsWith(JPEG_EXT)) return TYPE_JPG;
        else if(lowCaseReqUri.endsWith(HTML_EXT)) return TYPE_HTML;
        else if(lowCaseReqUri.endsWith(GIF_EXT)) return TYPE_GIF;
        else if(lowCaseReqUri.endsWith(TXT_EXT)) return TYPE_TXT;
        else if(lowCaseReqUri.endsWith(PNG_EXT)) return TYPE_PNG;
        else if(lowCaseReqUri.endsWith(JS_EXT)) return TYPE_JS;
        else if(lowCaseReqUri.endsWith(CSS_EXT)) return TYPE_CSS;
        else if(lowCaseReqUri.endsWith(SVG_EXT)) return TYPE_SVG;
        //errors will be text/plain
        else return TYPE_TXT;
    }

    public static int getSocketTimeout(int numActiveThreads){
        int cores = Runtime.getRuntime().availableProcessors();
        //if active thread/connections < number of cores, set timeout to 5 mins
        if(numActiveThreads<=cores) return 5*60000;
        else if(numActiveThreads>cores && numActiveThreads<=2*cores) return 3*60000;
        else return 1*60000;
    } 
}