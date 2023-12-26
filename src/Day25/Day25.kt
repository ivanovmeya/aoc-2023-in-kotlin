package Day25

import Day25.karger.Edge
import Day25.karger.MinCut
import println
import readInput

fun main() {

    fun parseToEdges(input: List<String>): List<Pair<String, String>> {
        val edges = mutableListOf<Pair<String, String>>()
        input.forEach {
            val (src, dstRawList) = it.split(":")
            val dstList = dstRawList.trim().split(" ")

            for (dst in dstList) {
                edges.add(src to dst)
                edges.add(dst to src)
            }

        }

        return edges
    }

    fun parseVertices(input: List<String>): Set<String> {
        return input.map {
            val (src, dstList) = it.split(":").run {
                first() to last().trim().split(" ")
            }
            listOf(src) + dstList
        }.distinct().flatten().toSet()
    }

    fun part1(input: List<String>): Int {

        val edges = parseToEdges(input).map { Edge(it.first, it.second) }
        val vertices = parseVertices(input)

        var minCut = MinCut(vertices.size, edges.size, vertices)

        //Required 3 edges removal. (As edges not directed -> 6)
        var minCutValue = 0
        while (minCutValue != 6) {
            minCut = MinCut(vertices.size, edges.size, vertices)
            minCutValue = minCut.minCutKarger(edges)
            println("MinCutValue by Karger = $minCutValue")
        }

        println("Finally we got min cut value by Karger with removing ${minCutValue / 2} nodes")

        return minCut.calcMultiplyOfSetSizes()
    }

    val testInput = readInput("Day25", true, 1)
    val part1Test = part1(testInput)
    println(part1Test)
    check(part1Test == 54)
    val input = readInput("Day25", false)
    val part1 = part1(input)
    part1.println()

}