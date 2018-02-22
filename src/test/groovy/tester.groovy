import wordcounter.bo.WordCounter

List<String> TEST_KEYWORDS = new ArrayList<String>()
TEST_KEYWORDS.add("cats")
TEST_KEYWORDS.add("dom")

WordCounter.setKeywords(TEST_KEYWORDS)

println(WordCounter.keywords)

int count = WordCounter.getKeywordCount("https://en.wikipedia.org/wiki/Cat")
int countTwo = WordCounter.getKeywordCount("https://en.wikipedia.org/wiki/Cat")

println(count)
println(countTwo)

WordCounter.setKeywords(['dogs', 'dogs', 'aminals'])
int countThree = WordCounter.getKeywordCount("https://en.wikipedia.org/wiki/Cat")
int countFour = WordCounter.getKeywordCount("https://en.wikipedia.org/wiki/Cat")

println(countThree)
println(countFour)

//Check that bad URLS blow up and return 0
badCount = WordCounter.getKeywordCount("ww.bad.url")

println(badCount)