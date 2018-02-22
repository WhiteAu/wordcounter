package wordcounter.interfaces;

import  java.net.MalformedURLException;

public interface URLParserInterface {
    String fetchURLAsString(String urlToFetch) throws MalformedURLException ;
}
