import kotlin.math.abs

fun main() {

    fun parseToCoordinates(input: List<String>, expandMultiplier: Int = 1): List<Galaxy> {
        val galaxies = mutableListOf<Galaxy>()
        //number of empty rows so far
        val eR = mutableListOf<Int>().apply {
            add(0)
        }
        //numberOfEmptyColumnsSoFar till i-th column
        val eC = mutableListOf<Int>().apply {
            add(0)
        }
        for (col in 1 until input[0].length) {
            //check that all rows are empty
            var row = 0
            while (row < input.size && input[row][col] == '.') row++

            eC.add(if (row == input.size) eC[col - 1] + 1 else eC[col - 1])
        }

        var gCount = 1
        for (row in input.indices) {
            //check if empty Row
            if (input[row].contains('#')) {
                if (row != 0) {
                    eR.add(eR[row - 1])
                }
                for (col in 0 until input[0].length) {
                    if (input[row][col] == '#') {
                        val x = row + (eR[row] * (expandMultiplier-1))
                        val y = col + (eC[col] * (expandMultiplier-1))
//                        println("g:[$row,$col] -> [$x,$y]")
                        galaxies.add(Galaxy(gCount, x, y))
                        gCount++
                    }
                }
            } else {
                if (row != 0) {
                    eR.add(eR[row - 1] + 1)
                }
            }
        }
        return galaxies
    }

    fun calcSumDistancesPairs(galaxies: List<Galaxy>): Long {
        var sum = 0L
        for (i in galaxies.indices) {
            for (j in i + 1 until galaxies.size) {
                val dX = abs(galaxies[j].x - galaxies[i].x)
                val dY = abs(galaxies[j].y - galaxies[i].y)
                sum += (dX + dY)
            }
        }
        return sum
    }

    fun part1(input: List<String>): Int {
        val galaxies = parseToCoordinates(input, 2)
        return calcSumDistancesPairs(galaxies).toInt()
    }

    fun part2(input: List<String>, multiplier: Int): Long {
        val galaxies = parseToCoordinates(input, multiplier)
        return calcSumDistancesPairs(galaxies)
    }

    val testInput = readInput("Day11", true, 1)
    val part1 = part1(testInput)
    println("part1=$part1")
    check(part1 == 374)

    val part2ten = part2(testInput, 10)
    println("part2ten = $part2ten")
    check(part2ten == 1030L)
    val part2Hundred = part2(testInput, 100)
    println("part2Hundred = $part2Hundred")
    check(part2Hundred == 8410L)

    val input = readInput("Day11", false)
    part1(input).println()
    part2(input, 1000000).println()
}

data class Galaxy(val id: Int, val x: Int, val y: Int)