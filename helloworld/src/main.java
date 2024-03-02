import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class main {
    public static void main(String[] args) {
        System.out.println("Java http client example");
        System.out.println("------------------------");
        try {
            URL url = new URL("https://jsonplaceholder.typicode.com/users");
            BufferedReader inputStream = new BufferedReader(
                    new InputStreamReader(url.openStream()));
            String line = "";
            StringBuilder json = new StringBuilder();
            while (line != null) {
                line = inputStream.readLine();
                if(line != null) json.append(line);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
