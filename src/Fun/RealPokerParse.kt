package Fun

class RealPokerParse {

    private val cardRanks = mapOf(
        'A' to 14,
        'K' to 13,
        'Q' to 12,
        'J' to 11,
        'T' to 10,
    )

    fun cardRank(card: Char) = cardRanks[card] ?: card.digitToInt()

    fun parseHandToSortedFreq(handStr: String): List<Map.Entry<Char, Int>> {
        //build frequencies table
        val freq = mutableMapOf<Char, Int>()
        for (card in handStr) {
            freq[card] = freq.getOrDefault(card, 0) + 1
        }

        //sort it by value in descending order
        val sortedCards = freq.entries.sortedWith(
            //We need Comparator, that will sort by value first and inside it sort by key
            Comparator { o1, o2 ->
                val byValue = o2.value.compareTo(o1.value)
                if (byValue != 0) return@Comparator byValue

                val firstCard = cardRank(o1.key)
                val secondCard = cardRank(o2.key)
                secondCard.compareTo(firstCard)
            }
        )

        return sortedCards
    }

    fun parseToRealPoker(input: List<String>): List<Pair<List<Map.Entry<Char, Int>>, Int>> {
        val sortedHands = input.map {
            val (handStr, bid) = it.split(" ")
            parseHandToSortedFreq(handStr) to bid.toInt()
            //now we can tell which hand is winning, we need to sort them by comparing to each other
        }.sortedWith(
            Comparator { hand1, hand2 ->
                //how to compare hands?
                //we go one by one on sorted Frequencies and compare each card strength
                var i = 0
                val hand1Freq = hand1.first
                val hand2Freq = hand2.first

                while (i < hand1Freq.size && i < hand2Freq.size) {
                    val compareHandType = hand1Freq[i].value.compareTo(hand2Freq[i].value)

                    //first freq stronger than other hand (like 3 of a kind is stronger than 2 of a kind)
                    if (compareHandType != 0) return@Comparator compareHandType

                    //we have same type of hand (probably, except for two pairs)

                    val compareHandTypeRank =
                        cardRank(hand1Freq[i].key).compareTo(cardRank(hand2Freq[i].key))

                    //hand type same (five of a kind) -> compare card rank
                    if (compareHandTypeRank != 0) return@Comparator compareHandTypeRank
                    //same card rank -> go to next
                    i++
                }
                //if everything same and we don't return -> just select any hand, let's go with first one
                -1
            }
        )

        return sortedHands
    }
}