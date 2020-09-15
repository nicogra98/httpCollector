import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class http_client {
    public static void main (String [] args) throws Exception{
        boolean redirected = false; //flag
        String location = args[0];
        /*------------We get the path for the output file------------*/
        String dir = System.getProperty("user.dir") + "/http_client_output";

        /*------------We start a connection with the url in args------------*/
        URL url = new URL (args[0]);
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setRequestMethod("GET");

        /*------------We handle the redirect------------*/
        int responseCode = httpCon.getResponseCode();

        if (responseCode == 301 || responseCode == 302){
            redirected = true;
            location = httpCon.getHeaderField("Location");
            URL newUrl = new URL (location);
            httpCon = (HttpURLConnection) newUrl.openConnection();
        }

        /*------------We read the contents------------*/
        InputStream input = httpCon.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        /*------------We set up eveyrhing needed to write in the file------------*/
        FileWriter writer = new FileWriter(dir);
        BufferedWriter bwriter = new BufferedWriter(writer);

        /*------------We print if we are redirected------------*/
        if (redirected){
            bwriter.write("URL redirected to: " + location + "\n");
        }

        /*------------We Get the header info------------*/
        Map <String, List <String>> header = new HashMap<String, List <String>>();
        header = httpCon.getHeaderFields();

        /*We write header info into file------------*/
        bwriter.write("Printing HTTP header info from " + location + "\n");
        for (Map.Entry entry : header.entrySet()){
            bwriter.write(entry.getKey() + " " + entry.getValue().toString() + "\n");
            bwriter.flush();
        }
 
        bwriter.write("\n");
        bwriter.write("\n");

        /*------------We write the URL Content into file------------*/
        String line = "";
        bwriter.write("URL Content...\n");
        while ( (line = reader.readLine()) != null) {
            bwriter.write(line + "\n");
            bwriter.flush();

        }

        reader.close();
        writer.close();


   
     }
}