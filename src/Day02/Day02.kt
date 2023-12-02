fun main() {

    val cons = CubeSet(12,13,14)

    fun parseGames(input: List<String>): List<Game> {
        //Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
        return input.map { line ->

            val (gameIdStr, gameSetsStr) = line.split(':').run {
                first() to last().split(';')
            }

            val gameId = gameIdStr.split(' ').last().toInt()

            val cubeSets = gameSetsStr.map { gameSetStr ->
                val colorMap = mutableMapOf<String, Int>()
                gameSetStr.split(',').map {
                    val (count, color) = it.trim().split(' ').run { first().toInt() to last() }
                    colorMap[color] = count
                }
                CubeSet(
                    r = colorMap.getOrDefault("red",0),
                    g = colorMap.getOrDefault("green", 0),
                    b = colorMap.getOrDefault("blue", 0)
                )
            }
            Game(gameId, cubeSets)
        }
    }

    fun part1(input: List<String>): Int {
        val games = parseGames(input)

        return games.filter {game ->
            game.cubeSets.all { cubeSet -> cubeSet.r <= cons.r && cubeSet.g <= cons.g && cubeSet.b <= cons.b }
        }.sumOf { it.id }
    }

    fun part2(input: List<String>): Int {
        val games = parseGames(input)

        return games.sumOf { game ->
            //find max values of rgb for all sets within a game and multiply them
            //power Of Set
            game.cubeSets.maxOf { it.r } * game.cubeSets.maxOf { it.g } * game.cubeSets.maxOf { it.b }
        }
    }

    val testInput = readInput("Day02", true, 1)
    check(part1(testInput) == 8)

    val testInput2 = readInput("Day02", true, 1)
    check(part2(testInput2) == 2286)

    val input = readInput("Day02", false)
    part1(input).println()
    part2(input).println()
}

data class Game(val id : Int, val cubeSets: List<CubeSet>)
data class CubeSet(val r: Int = 0, val g: Int = 0, val b: Int = 0)
