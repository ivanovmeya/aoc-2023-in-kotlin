fun main() {

    fun cardRank(card: Char, withJoker: Boolean) =
        if (withJoker && card == 'J') 1 else cardRanks[card] ?: card.digitToInt()

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

                val firstCard = cardRank(o1.key, false)
                val secondCard = cardRank(o2.key, false)
                secondCard.compareTo(firstCard)
            }
        )

        return sortedCards
    }

    fun parseHands(input: List<String>): List<Hand> {
        return input.map {
            val (handStr, bid) = it.split(" ")

            val freq = parseHandToSortedFreq(handStr)

            //determine hand type
            val handType = when (freq.first().value) {
                5 -> HandType.Five
                4 -> HandType.Four
                3 -> {
                    //full house or 3 of a kind
                    if (freq[1].value == 2) HandType.FullHouse else HandType.Three
                }
                2 -> {
                    //two pair or one pair
                    if (freq[1].value == 2) HandType.TwoPair else HandType.OnePair
                }
                1 -> HandType.HighCard
                else -> HandType.HighCard
            }
            Hand(handStr, handType, bid.toInt())
        }
    }

    fun parseHandsWithJokers(input: List<String>): List<Hand> {
        return input.map { line ->
            val (handStr, bid) = line.split(" ")

            val freq = parseHandToSortedFreq(handStr)

            val jokers = freq.firstOrNull { it.key == 'J' }?.value ?: 0
            //determine hand type
            val topFreq = if (jokers != 5) freq.first { it.key != 'J' }.value else 0

            val handTypeJ = when (jokers) {
                5 -> HandType.Five
                4 -> HandType.Five
                3 -> if (topFreq == 2) HandType.Five else HandType.Four
                2 -> {
                    //3 cards left
                    when (topFreq) {
                        3 -> HandType.Five
                        2 -> HandType.Four
                        1 -> HandType.Three
                        else -> throw IllegalStateException("have 2 jokers and wrong topFreq=${topFreq}")
                    }
                }
                1 -> {
                    //4 cards left
                    when (topFreq) {
                        4 -> HandType.Five
                        3 -> {
                            //1 joker + 3 same cards, 1 left
                            HandType.Four
                        }
                        2 -> {
                            //1 joker + 2 same cards, 2 unknown cards yet (could be also pair or could be different cards)
                            //if also pair
                            if (freq[1].value == 2) {
                                HandType.FullHouse
                            } else {
                                //different cards
                                HandType.Three
                            }
                        }
                        1 -> {
                            //1 joker + 4 different cards
                            HandType.OnePair
                        }
                        else -> throw IllegalStateException("have 1 jokers and wrong topFreq=${topFreq}")
                    }
                }
                0 -> when (freq.first().value) {
                    5 -> HandType.Five
                    4 -> HandType.Four
                    3 -> {
                        //full house or 3 of a kind
                        if (freq[1].value == 2) HandType.FullHouse else HandType.Three
                    }
                    2 -> {
                        //two pair or one pair
                        if (freq[1].value == 2) HandType.TwoPair else HandType.OnePair
                    }
                    1 -> HandType.HighCard
                    else -> HandType.HighCard
                }
                else -> throw IllegalStateException("have wrong jokers $jokers, str = $handStr")
            }

            Hand(handStr, handTypeJ, bid.toInt())
        }
    }

    fun handsComparator(withJoker: Boolean): java.util.Comparator<Hand> = Comparator { hand1, hand2 ->
        val compare = hand1.type.rank.compareTo(hand2.type.rank)
        if (compare != 0) return@Comparator compare

        //if same hand type, then compare hand strength card by card
        for (i in 0..4) {
            val cardRankCompare = cardRank(hand1.str[i], withJoker).compareTo(cardRank(hand2.str[i], withJoker))
            if (cardRankCompare != 0) return@Comparator cardRankCompare
        }
        0
    }

    fun Iterable<Hand>.sumOfRankBids() = this
        .mapIndexed { index, hand ->
            val rank = index + 1
            hand.bid * rank
        }
        .sumOf { it }

    fun part1(input: List<String>): Int {
        return parseHands(input)
            .sortedWith(handsComparator(false))
            .sumOfRankBids()
    }

    fun part2(input: List<String>): Int {
        return parseHandsWithJokers(input)
            .sortedWith(handsComparator(true))
            .sumOfRankBids()
    }

    val testInput = readInput("Day07", true, 1)
    check(part1(testInput) == 6440)

    val testInput2 = readInput("Day07", true, 1)
    check(part2(testInput2) == 5905)

    val input = readInput("Day07", false)
    part1(input).println()
    part2(input).println()
}

data class Hand(val str: String, val type: HandType, val bid: Int)

val cardRanks = mapOf(
    'A' to 14,
    'K' to 13,
    'Q' to 12,
    'J' to 11,
    'T' to 10,
)

enum class HandType(val rank: Int) {
    Five(7),
    Four(6),
    FullHouse(5),
    Three(4),
    TwoPair(3),
    OnePair(2),
    HighCard(1),
}