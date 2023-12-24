import java.util.*

fun main() {

    fun parseMap(input: List<String>): List<List<Int>> {
        return input.map {
            it.toCharArray().map { weight -> weight.digitToInt() }
        }
    }

    fun movingDir(cell: Pair<Int, Int>, prevCell: Pair<Int, Int>): Direction {
        val dx = cell.first - prevCell.first
        val dy = cell.second - prevCell.second
        return when {
            dx == -1 -> Direction.TOP
            dy == -1 -> Direction.RIGHT
            dx == 1 -> Direction.DOWN
            dy == 1 -> Direction.LEFT
            else -> throw IllegalStateException("Can not determine direction, dx=$dx, dy=$dy")
        }
    }

    fun ninetyDegreeTurn(dir: Direction): List<Direction> {
        return when (dir) {
            Direction.LEFT -> listOf(Direction.TOP, Direction.DOWN)
            Direction.TOP -> listOf(Direction.RIGHT, Direction.LEFT)
            Direction.RIGHT -> listOf(Direction.TOP, Direction.DOWN)
            Direction.DOWN -> listOf(Direction.RIGHT, Direction.LEFT)
        }
    }

    fun nextDirections(dir: Direction): List<Direction> {
        return when (dir) {
            Direction.LEFT -> listOf(Direction.LEFT, Direction.TOP, Direction.DOWN)
            Direction.TOP -> listOf(Direction.TOP, Direction.RIGHT, Direction.LEFT)
            Direction.RIGHT -> listOf(Direction.RIGHT, Direction.TOP, Direction.DOWN)
            Direction.DOWN -> listOf(Direction.DOWN, Direction.RIGHT, Direction.LEFT)
        }
    }

    fun nextCoordinates(x: Int, y: Int, dir: Direction): Pair<Int, Int> {
        return when (dir) {
            Direction.LEFT -> x to (y + 1)
            Direction.TOP -> (x - 1) to y
            Direction.RIGHT -> x to (y - 1)
            Direction.DOWN -> (x + 1) to y
        }
    }

    fun dijkstra(m: List<List<Int>>, minSteps: Int = 1, getEligibleDirections: (cell: Cell) -> List<Direction>): Int {
        //we start at [0,0] and go to [m.size, m[0].size] (can go left,top,right, bottom)

        //what is dijkstra algorithm?
        //on each step we check 4 directions and add New Path Weight to Queue (PriorityQueue) and save the previous Cell and current path
        //on next step we take the minimum path cell from priority queue, that is not visited (settled) yet.

        //Dijkstra is Greedy algorithm

        val minPathQueue = PriorityQueue<PathEntry>(
            compareBy { it.weight }
        ).apply {
            offer(PathEntry(Cell(0, 0, Direction.LEFT, 0), 0))
        }

        val settled = hashSetOf<Cell>()

        while (minPathQueue.isNotEmpty()) {
            val (cell, weight) = minPathQueue.poll()
            //first time - come to last cell -> it would be the shortest path, by definition (because we poll from MinHeap)
            if (cell.x == m.lastIndex && cell.y == m[0].lastIndex && cell.steps >= minSteps) {
                return weight
            }
            if (settled.contains(cell)) continue
            settled.add(cell)

            //here we should be able to traverse available directions:
            //available directions should be selected by some functions that we pass from outside
            //but how then prepare PathWeight Data class? (as generic? let's just keep it as it is now and figure out later)

            //check available directions

            val possibleDirections = getEligibleDirections(cell)
            for (dir in possibleDirections) {
                val (x, y) = nextCoordinates(cell.x, cell.y, dir)

                //bounds constraints check
                if (x >= 0 && x < m.size && y >= 0 && y < m[0].size) {
                    val newWeight = weight + m[x][y]
                    val newDir = movingDir(x to y, cell.x to cell.y)

                    val nextCell = Cell(x, y, newDir, if (cell.direction == newDir) cell.steps + 1 else 1)

                    //Would it work if we omit steps from Cell data class?  (can just override equals and hashcode to exlclude steps)

                    //add only if it wasn't visited with such constraints already
                    if (!settled.contains(nextCell)) {
                        minPathQueue.offer(PathEntry(nextCell, newWeight))
                    }
                }
            }
        }

        return -1
    }

    fun part1(input: List<String>): Int {
        val map = parseMap(input)

        val path = dijkstra(map) { cell ->
            if (cell.steps == 3) {
                ninetyDegreeTurn(cell.direction)
            } else {
                nextDirections(cell.direction)
            }
        }
        return path
    }

    fun part2(input: List<String>): Int {
        val map = parseMap(input)

        val path = dijkstra(map, 4) { cell ->
            when {
                cell.steps < 4 -> {
                    //can go only straight (continue in that direction)
                    listOf(cell.direction)
                }
                cell.steps < 10 -> nextDirections(cell.direction)
                cell.steps == 10 -> ninetyDegreeTurn(cell.direction)
                else -> emptyList()
            }
        }

        return path
    }

    val testInput = readInput("Day17", true, 1)
    val part1Test = part1(testInput)
    println(part1Test)
    check(part1Test == 102)

    val part2Test = part2(testInput)
    println(part2Test)
    check(part2Test == 94)

    val testInput2 = readInput("Day17", true, 2)
    val part2Test2 = part2(testInput2)
    println(part2Test2)
    check(part2Test2 == 71)

    val input = readInput("Day17", false)
    val part1 = part1(input)
    check(part1 == 956)
    part1.println()
    part2(input).println()
}

data class PathEntry(val cell: Cell, val weight: Int)
data class Cell(val x: Int, val y: Int, val direction: Direction, val steps: Int) {
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other as? Cell == null) return false

        return this.x == other.x &&
                this.y == other.y &&
                this.direction == other.direction &&
                this.steps == other.steps
    }

    override fun hashCode(): Int {
        return x.hashCode() +
                y.hashCode() +
                direction.hashCode() +
                steps.hashCode()
    }
}

enum class Direction {
    LEFT,
    TOP,
    RIGHT,
    DOWN
}