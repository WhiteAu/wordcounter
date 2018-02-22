import wordcounter.bo.WordCounter

List<String> TEST_KEYWORDS = new ArrayList<String>()
TEST_KEYWORDS.add("cats")
TEST_KEYWORDS.add("dom")

WordCounter.setKeywords(TEST_KEYWORDS)

println(WordCounter.keywords)

int count = WordCounter.getKeywordCount("https://en.wikipedia.org/wiki/Cat")

println(count)