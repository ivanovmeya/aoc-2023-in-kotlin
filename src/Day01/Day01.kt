fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line -> "${line.first { it.isDigit() }}${line.last { it.isDigit() }}".toInt() }
    }

    fun findFirstDigit(wordDigits: Map<String, Int>, line: String): Int {
        //1) find first word digit (min wordDigits index (remember digit))
        var wordDigitIndex = Int.MAX_VALUE
        var wordDigitValue = 0
        wordDigits.forEach { (wordDigit, digit) ->
            val index = line.indexOf(wordDigit)
            if (index != -1 && index < wordDigitIndex) {
                //check if it is min
                wordDigitIndex = index
                wordDigitValue = digit
            }
        }

        //2) compare it to real digit index.
        var realDigitIndex = line.indexOfFirst { it.isDigit() }
        realDigitIndex = if (realDigitIndex == -1) Int.MAX_VALUE else realDigitIndex
        return if (wordDigitIndex < realDigitIndex) {
            wordDigitValue
        } else {
            line[realDigitIndex].digitToInt()
        }
    }

    fun part2(input: List<String>): Int {
        //index of first digit, or index of first word
        val wordDigits = mapOf(
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9
        )

        return input.sumOf {line ->
            val first = findFirstDigit(wordDigits, line)
            //first digit from the end
            val last = findFirstDigit(wordDigits.mapKeys { it.key.reversed() }, line.reversed())
            "$first$last".toInt()
        }
    }

    val testInput = readInput("Day01", true, 1)
    check(part1(testInput) == 142)

    val testInput2 = readInput("Day01", true, 2)
    check(part2(testInput2) == 281)

    val input = readInput("Day01", false)
    part1(input).println()
    part2(input).println()
}
