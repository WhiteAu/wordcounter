package bo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class HTTPRequestURLParser {

    public static String fetchURLAsString(String urlToFetch){
        StringBuilder accumulatedResponse = new StringBuilder();
        try {
            URL url = new URL(urlToFetch);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String responseBuffer = "";
            while ((responseBuffer = br.readLine()) != null) {
                System.out.println(String.format("next line:\n %s", responseBuffer));
                accumulatedResponse.append(responseBuffer);
            }
        } catch (Exception ex) {
            //TODO: do something more meaningful with exceptions here
            ex.printStackTrace();
        }

        return accumulatedResponse.toString();
    }
}
