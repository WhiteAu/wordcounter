# URLWordCount
a Java Toy Project that counts the number of keywords hits at a given URL
Coded in Java 8.


Open Questions for further improvement:
Since bo.bo.WordCounter.keywords is static, it can change in the middle of a query--
this is an obvious race condition, and how it is handled can go in a number of different ways:
1) at the top of each 'getKeywordCount call, make a local copy of keywords
2) continuously check for next keyword at the deep for loop level when testing for substring matching against the HTML String
 a) this has its own set of issues (what if we remove keywords that were previously present?)

3) move keywords to a DAO and have proper locking


replace HTTPRequest with something more sophisticated/don't reinvent wheel.



