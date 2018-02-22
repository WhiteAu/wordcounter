# URLWordCount
a Java Toy Project that counts the number of keywords hits at a given URL
Coded in Java 8; package management using gradle ( https://gradle.org/guides/#getting-started )


outlines for both HTTPReader and MemoryCache were pulled from online resources.


Open Questions/ Areas for further improvement:
Since WordCounter.keywords is static, it can change in the middle of a query--
this is an obvious race condition, and how it is handled can go in a number of different ways:
1) at the top of each 'getKeywordCount call, make a local copy of keywords
    a) This is the current strategy because it is easy, and without further knowledge of the size/scale of requests,
       the I am considering the synch overhead minimal.
2) continuously check for next keyword at the deep for loop level when testing for substring matching against the HTML String
 a) this has its own set of issues (what if we remove keywords that were previously present?)
3) move keywords to a DAO and implement optimistic locking to reduce synchronization overhead
    by reducing burden of copies.
    a) This is my favored option, but has considerably more dev overhead.


replace HTTPRequest with something more sophisticated/don't reinvent wheel.

Replace Caching Map with an LRU Map in apache commons (fixed size map; removes least used element first)

Refactor URL Responses/URL Parser to draw from interfaces.

Use Spring to build objects hierarchies.






