package wordcounter.bo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.MalformedURLException;

import wordcounter.interfaces.URLParserInterface;

public class HTTPRequestURLParser implements URLParserInterface {

    public HTTPRequestURLParser() {

    }

    public String fetchURLAsString(String urlToFetch) throws MalformedURLException {
        StringBuilder accumulatedResponse = new StringBuilder();
        URL url = new URL(urlToFetch);
        try (
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        ) {


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
