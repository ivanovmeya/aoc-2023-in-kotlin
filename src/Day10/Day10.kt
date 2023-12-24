package Day10

import readInput

fun main() {

    fun parse(input: List<String>): List<List<Char>> {
        return input.map { it.toCharArray().toList() }
    }

    fun findStart(m: List<List<Char>>): IntArray {
        m.forEachIndexed { row, line ->
            line.forEachIndexed { col, ch ->
                if (ch == 'S') return intArrayOf(row, col)
            }
        }
        return intArrayOf(-1, -1)
    }

    val dirs = listOf(
        intArrayOf(0, -1) to listOf('-', 'L', 'F'), //left
        intArrayOf(-1, 0) to listOf('|', '7', 'F'), //up
        intArrayOf(0, 1) to listOf('-', 'J', '7'), //right
        intArrayOf(1, 0) to listOf('|', 'J', 'L') //down
    )

    fun defineStartDirs(m: List<List<Char>>, start: IntArray): List<IntArray> {
        val possibleDirs = mutableListOf<IntArray>()
        for (d in dirs) {
            val x = start[0] + d.first[0]
            val y = start[1] + d.first[1]

            if (x >= 0 && x < m.size && y >= 0 && y < m[0].size) {
                if (d.second.contains(m[x][y]))
                    possibleDirs.add(intArrayOf(x, y))
            }
        }

        return possibleDirs
    }

    fun next(m: List<List<Char>>, current: IntArray, prev: IntArray): IntArray {
        val xC = current[0]
        val yC = current[1]

        val xP = prev[0]
        val yP = prev[1]

        return when (m[xC][yC]) {
            '|' -> if (xC - 1 == xP) intArrayOf(xC + 1, yC) else intArrayOf(xC - 1, yC)
            '-' -> if (yC - 1 == yP) intArrayOf(xC, yC + 1) else intArrayOf(xC, yC - 1)
            'L' -> if (xC - 1 == xP) intArrayOf(xC, yC + 1) else intArrayOf(xC - 1, yC)
            'J' -> if (xC - 1 == xP) intArrayOf(xC, yC - 1) else intArrayOf(xC - 1, yC)
            '7' -> if (yC - 1 == yP) intArrayOf(xC + 1, yC) else intArrayOf(xC, yC - 1)
            'F' -> if (yC + 1 == yP) intArrayOf(xC + 1, yC) else intArrayOf(xC, yC + 1)
            else -> throw IllegalArgumentException("wrong current element = ${m[xC][yC]}")
        }
    }

    fun part1(input: List<String>): Int {

        val m = parse(input)
        //find start
        //set two pointers and go simultaneously, until met
        //from current position two exits -> back and forward (keep direction)

        val start = findStart(m)
        println("start = ${start.joinToString(",")}")

        val startDirections = defineStartDirs(m, start)
        startDirections.forEach {
            println("char=${m[it[0]][it[1]]}; m[${it[0]}][${it[1]}]")
        }

        var steps = 1
        var firstPrev = start
        var first = startDirections[0]

        val loopPath = mutableListOf(start[0] to start[1])

        while ("${first[0]},${first[1]}" != "${start[0]},${start[1]}") {
            loopPath.add(first[0] to first[1])
            val tmp = first
            first = next(m, first, firstPrev)
            firstPrev = tmp
            steps++
        }
        loopPath.add(first[0] to first[1])

        return steps / 2
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput1 = readInput("Day10", true, 1)
    val part1test1 = part1(testInput1)
    println(part1test1)
    check(part1test1 == 4)

    val testInput2 = readInput("Day10", true, 2)
    val part1test2 = part1(testInput2)
    println(part1test2)

    check(part1test2 == 8)

    val input = readInput("Day10", false, 1)
    println(part1(input))

}