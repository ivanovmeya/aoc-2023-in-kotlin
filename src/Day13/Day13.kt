fun main() {

    fun parseMapsList(input: List<String>): List<List<List<Char>>> {
        return input.joinToString("\n").split("\n\n").map { it.lines().map { line -> line.toList() } }
    }

    fun isMirroredHorizontallyAcross(map: List<List<Char>>, start: Int, errorMargin: Int = 0): Boolean {
        var col1 = start
        var col2 = start + 1

        var totalErrors = 0
        while (col1 >= 0 && col2 < map[0].size) {
            val errors = map.indices.count { row -> map[row][col1] != map[row][col2] }
            totalErrors += errors
            if (errors > errorMargin) {
                return false
            }
            col1--
            col2++
        }
        return totalErrors == errorMargin
    }

    fun isMirroredVerticallyAcross(map: List<List<Char>>, start: Int, errorMargin: Int = 0): Boolean {
        var row1 = start
        var row2 = start + 1

        var totalErrors = 0
        while (row1 >= 0 && row2 < map.size) {
            val errors = map[0].indices.count { col -> map[row1][col] != map[row2][col] }
            totalErrors += errors
            if (errors > errorMargin) {
                return false
            }

            row1--
            row2++
        }
        return totalErrors == errorMargin
    }

    fun calcSummarize(maps: List<List<List<Char>>>, errorMargin: Int): Int {
        return maps.sumOf { map ->

            val n = map.size
            val m = map[0].size

            val mirroredColumn = (0 until m - 1).firstOrNull { col ->
                isMirroredHorizontallyAcross(map, col, errorMargin)
            }

            val mirroredRow = (0 until n - 1).firstOrNull { row ->
                isMirroredVerticallyAcross(map, row, errorMargin)
            }

            if (mirroredRow != null) {
                (mirroredRow + 1) * 100
            } else if (mirroredColumn != null) {
                mirroredColumn + 1
            } else {
                throw IllegalStateException("Pattern ${map.joinToString("")} is not mirrored")
            }
        }
    }

    fun part1(input: List<String>): Int {
        return calcSummarize(parseMapsList(input), 0)
    }

    fun part2(input: List<String>): Int {
        return calcSummarize(parseMapsList(input), 1)
    }

    val testInput = readInput("Day13", true, 1)
    val part1Test = part1(testInput)
    println(part1Test)
    check(part1Test == 405)

    val part2Test = part2(testInput)
    println(part2Test)
    check(part2Test == 400)

    val input = readInput("Day13", false)
    val part1 = part1(input)
    part1.println()
    check(part1 == 28895)

    val part2 = part2(input)
    part2.println()
    check(part2 == 31603)
}