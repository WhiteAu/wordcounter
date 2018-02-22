package wordcounter.bo;

/*

** WORDCOUNTER.JAVA

**

** Library object that, given a url and set of keywords,

, returns the number of times

** any of these keywords appear in the resource identified by the URL. Only exact matches should "hit".

**

** The library must support multi-threaded access. The keyword set may be updated

** at any time. Results for a given resource should be cached for 1 hour to reduce network

** traffic and increase performance.

**

** Should stand alone with no additional dependencies beyond what is in the JRE.
* ** That means no external web scrapers!

*/

import java.net.MalformedURLException;
import java.util.*;
import org.apache.commons.lang3.StringUtils;

import wordcounter.models.CommonValues;

public class WordCounter

{
    //TODO: replace this with a set.
    private static Set<String> keywords = new HashSet<>();
    private static final Object keywordLock = new Object();
    private static MemoryCache<String, String> urlToResourceCache =
            new MemoryCache<>(CommonValues.SECONDS_IN_HOUR, 2 * CommonValues.SECONDS_IN_MINUTE, 10);
    private static HTTPRequestURLParser httpRequestURLParser = new HTTPRequestURLParser();




    public static void setKeywords(List<String> keywords) {
        Set<String> hashSet = new HashSet<>(keywords);
        synchronized (keywordLock) {
            WordCounter.keywords = hashSet;
        }
    }



    public static int getKeywordCount(String url) {
        int cumulativeKeywordCount = 0;

        //since WordCount.keywords is static, going to make a local copy at start of function
        keywords = getCloneOfKeywords();


        //Since we're static, we don't know if keywords have been set.
        //TODO: refactor this to AccumulateCountInString
        if (keywords.isEmpty()) {
            String output = "Static Keywords Have Not Been Set! please set static keywords to a list using setKeywords(List<String> keywords)";
            System.out.println(output);
        } else {
            try {
                String stringURL = fetchURLAsStringFromCache(url);
                cumulativeKeywordCount = accumulateCountInString(stringURL, keywords);
            } catch (MalformedURLException e) {
                System.out.println(String.format("The supplied URL %s\n is not valid. Please create a valid URL.", url));
                cumulativeKeywordCount = 0;
            }

        }

        return cumulativeKeywordCount;
    }

    private static Set<String> getCloneOfKeywords() {
        Set<String> clonedList;

        synchronized (keywordLock) {
            clonedList = new HashSet<>(WordCounter.keywords);
        }

        return clonedList;
    }

    private static String fetchURLAsStringFromCache(String url) throws MalformedURLException{
        String representation = urlToResourceCache.get(url);
        if (representation == null) {
            representation = httpRequestURLParser.fetchURLAsString(url);
            urlToResourceCache.put(url, representation);
        }

        return representation;
    }

    private static int accumulateCountInString(String searchString, Set<String> keywords) {
        int cumulativeCount = 0;


        for(String keyword : keywords) {
            int count = StringUtils.countMatches(searchString, keyword);
            System.out.println(String.format("Found %d matches of keyword %s in the search string.", count, keyword));
            cumulativeCount += count;
        }

        return cumulativeCount;
    }



}