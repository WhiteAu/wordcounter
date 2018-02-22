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

import java.util.*;
import org.apache.commons.lang3.StringUtils;
import wordcounter.models.CommonValues;

public class WordCounter

{
    //TODO: replace this with a set.
    private static List<String> keywords = new ArrayList<String>();
    private static final Object keywordLock = new Object();
    private static MemoryCache<String, String> urlToResourceCache =
            new MemoryCache<>(CommonValues.getSecondsInHour(), 2 * CommonValues.getSecondsInMinute(), 10);




    public static void setKeywords(List<String> keywords) {
        List<String> arrayList = new ArrayList<>(keywords);
        synchronized (keywordLock) {
            WordCounter.keywords = arrayList;
        }
    }



    public static int getKeywordCount(String url) {
        int cumulativeKeywordCount = 0;

        //since WordCount.keywords is static, going to make a local copy at start of function
        keywords = getCloneOfKeywords();


        //Since we're static, we don't know if keywords have been set.
        //TODO: refactor this to AccumulateCountInString
        if (keywords.isEmpty()) {
            String output = new StringBuilder().append("Static Keywords Have Not Been Set! please set static keywords")
                                        .append("to a list using setKeywords(List<String> keywords)")
                                        .toString();
            System.out.println(output);
        } else {
            String stringURL = fetchURLAsStringFromCache(url);
            cumulativeKeywordCount = accumulateCountInString(stringURL, keywords);
        }


        return cumulativeKeywordCount;
    }

    private static List<String> getCloneOfKeywords() {
        List<String> clonedList;

        synchronized (keywordLock) {
            clonedList = new ArrayList<String>(WordCounter.keywords);
        }

        return clonedList;
    }

    private static String fetchURLAsStringFromCache(String url) {
        String representation = urlToResourceCache.get(url);
        if (representation == null) {
            representation = HTTPRequestURLParser.fetchURLAsString(url);
            urlToResourceCache.put(url, representation);
        }

        return representation;
    }

    private static int accumulateCountInString(String searchString, List<String> keywords) {
        int cumulativeCount = 0;


        for(String keyword : keywords) {
            int count = StringUtils.countMatches(searchString, keyword);
            System.out.println(String.format("Found %d matches of keyword %s in the search string.", count, keyword));
            cumulativeCount += count;
        }

        return cumulativeCount;
    }



}