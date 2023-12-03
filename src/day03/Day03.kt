
fun main() {

    val directions = listOf(
        //row and column
        -1 to -1,
        -1 to 0,
        -1 to 1,
        0 to -1,
        0 to 1,
        1 to -1,
        1 to 0,
        1 to 1,
    )

    fun parseNumbers(input: List<String>): MutableMap<String, NumberInfo> {

        val numbersMap = mutableMapOf<String, NumberInfo>()
        val numberBuilder = StringBuilder()

        input.forEachIndexed { row, rowValue ->
            var start: Int? = null
            var column = 0

            while (column < rowValue.length) {
                while (column < rowValue.length && rowValue[column].isDigit()) {
                    if (start == null) {
                        start = column
                    }

                    numberBuilder.append(rowValue[column])
                    column++
                }

                //we got a number if start != null
                start?.let {
                    val end = column - 1
                    val number = NumberInfo(
                        id = "$row:[$start,$end]",
                        value = numberBuilder.toString().toInt()
                    )

                    for (c in it..end) {
                        numbersMap["$row,$c"] = number
                    }
                }

                start = null
                numberBuilder.clear()
                column++
            }
        }
        return numbersMap
    }

    fun part1(input: List<String>): Int {
        val numbersMap = parseNumbers(input)
        var sum = 0
        //traverse all matrix
        input.forEachIndexed { row, rowValue ->
            rowValue.forEachIndexed { column, ch ->
                if (!ch.isDigit() && ch != '.') {
                    //we found a symbol and need to check 9 surrounded directions

                    //save id's of already added numbers
                    val ids = mutableSetOf<String>()
                    for (dir in directions) {
                        val (r, c) = row + dir.first to column + dir.second
                        if (r >= 0 && r < input.size && c >=0 && c < input[0].length) {
                            //check if we have number here (look up at numbersMap)
                            val number = numbersMap["$r,$c"]
                            if (number != null && !ids.contains(number.id)) {
                                sum += number.value
                                ids.add(number.id)
                            }
                        }
                    }
                }
            }
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        val numbersMap = parseNumbers(input)
        //sum of all gear ratios
        var sum = 0
        //traverse all matrix
        input.forEachIndexed { row, rowValue ->
            rowValue.forEachIndexed { column, ch ->
                if (!ch.isDigit() && ch != '.') {
                    //we found a symbol, to be the gear it should have exactly 2 numbers connected.

                    //save id's of already added numbers
                    val ids = mutableSetOf<String>()

                    var ratio = 1
                    var numbersCount = 0

                    for (dir in directions) {
                        val (r, c) = row + dir.first to column + dir.second
                        if (r >= 0 && r < input.size && c >=0 && c < input[0].length) {
                            //check if we have number here (look up at numbersMap)
                            val number = numbersMap["$r,$c"]
                            if (number != null && !ids.contains(number.id)) {
                                ratio *= number.value
                                ids.add(number.id)
                                numbersCount++
                            }
                        }
                    }

                    if (numbersCount == 2) {
                        sum += ratio
                    }
                }
            }
        }

        return sum
    }

    val testInput = readInput("Day03", true, 1)
    check(part1(testInput) == 4361)

    val testInput2 = readInput("Day03", true, 1)
    check(part2(testInput2) == 467835)

    val input = readInput("Day03", false)
    part1(input).println()
    part2(input).println()
}

//id forms as "$row:[$start,$end]"
data class NumberInfo(val id: String, val value: Int)