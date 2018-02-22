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

public class WordCounter

{
    //TODO: replace this with a set.
    private static List<String> keywords = new ArrayList<String>();
    private static final Object keywordLock = new Object();


    public static void setKeywords(List<String> keywords) {
        List<String> arrayList = new ArrayList<>(keywords);
        synchronized (keywordLock) {
            WordCounter.keywords = arrayList;
        }
    }



    public static int getKeywordCount(String url) {
        int cumulativeKeywordCount = 0;

        //since WordCount.keywords is static, going to make a local copy at start of function
        keywords = getSynchedCloneOfKeywords();


        //Since we're static, we don't know if keywords have been set.
        //TODO: refactor this to AccumulateCountInString
        if (keywords.isEmpty()) {
            String output = new StringBuilder().append("Static Keywords Have Not Been Set! please set static keywords")
                                        .append("to a list using setKeywords(List<String> keywords)")
                                        .toString();
            System.out.println(output);
        } else {
            String stringURL = fetchURLAsString(url);
            cumulativeKeywordCount = accumulateCountInString(stringURL, keywords);
        }


        return cumulativeKeywordCount;
    }

    private static List<String> getSynchedCloneOfKeywords() {
        List<String> clonedList;

        synchronized (keywordLock) {
            clonedList = new ArrayList<String>(WordCounter.keywords);
        }

        return clonedList;
    }

    private static String fetchURLAsString(String url) {
        String representation = HTTPRequestURLParser.fetchURLAsString(url);
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