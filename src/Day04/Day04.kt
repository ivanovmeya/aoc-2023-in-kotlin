fun main() {


    fun parseCards(input: List<String>): List<Card> {
//        Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
//        Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
//        Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
//        Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
//        Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
//        Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11

        val numberRegex = Regex("""\d+""")

        return input.mapIndexed { index, line ->
            val (winningStr, yoursStr) = line.split(':')[1].split('|').map { it.trim() }

            val winning = numberRegex.findAll(winningStr).map { it.value.toInt() }.toList()
            val yours = numberRegex.findAll(yoursStr).map { it.value.toInt() }.toList()
            Card(
                index + 1,
                yours.count { winning.contains(it) }
            )
        }

    }

    fun part1(input: List<String>): Int {
        val cards = parseCards(input)

        return cards.sumOf { card ->
            //calc score of the card as 2 to the power of winning numbers
            if (card.winCount != 0) 1 shl (card.winCount - 1) else 0
        }
    }

    fun part2(input: List<String>): Int {
        val cardHolders = parseCards(input).map { CardHolder(it, 1) }
        var totalCardsCount = 0

        cardHolders.forEachIndexed { index, holder ->
            totalCardsCount += holder.count

            //processing:
            for (next in index + 1 .. index + holder.card.winCount) {
                if (next < cardHolders.size) {
                    cardHolders[next].count += holder.count
                }
            }
        }

        return totalCardsCount
    }

    val testInput = readInput("Day04", true, 1)
    check(part1(testInput) == 13)

    val testInput2 = readInput("Day04", true, 1)
    check(part2(testInput2) == 30)

    val input = readInput("Day04", false)
    part1(input).println()
    part2(input).println()
}

data class Card(val id: Int, val winCount: Int)

data class CardHolder(val card: Card, var count: Int)