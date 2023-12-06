fun main() {

    fun parseRace(input: List<String>): Pair<List<Long>, List<Long>> {
        val times = input
            .first()
            .substringAfter("Time:")
            .split(" ")
            .filter { it.isNotEmpty() }
            .map { it.trim().toLong() }

        val distances = input
            .last()
            .substringAfter("Distance:")
            .split(" ")
            .filter { it.isNotEmpty() }
            .map { it.trim().toLong() }

        return times to distances
    }

    fun parseRacePart2(input: List<String>): Pair<Long, Long> {
        val time = input
            .first()
            .substringAfter("Time:")
            .replace(" ", "")
            .toLong()

        val distance = input
            .last()
            .substringAfter("Distance:")
            .replace(" ", "")
            .toLong()

        return time to distance
    }

    fun distance(holdTime: Long, totalTime: Long): Long {
        return totalTime * holdTime - holdTime * holdTime
    }

    fun upperBoundIndex(endIndex: Long, totalTime: Long, record: Long): Long {
        var start = 0L
        var end = endIndex

        while (start < end) {
            val mid = start + (end - start) / 2
            val d = distance(mid, totalTime)
            when {
                d <= record -> start = mid + 1
                else -> end = mid
            }
        }
        //start has first index that are equal or more than record
        return start
    }

    fun numberOfWaysToBeatRecord(totalTime: Long, record: Long): Long {
        //binary search lower or upper bound to find first that beats the record
        //distance func is parabola -> we have 2 sorted parts: ascending [0, mid] and descending [mid+1, total]
        //in both parts use binary search upper bound to found first that beats record
        //one notice: parabola is symmetric around it peak(mid) point -> calc ascending part and multiply by 2

        val midTime = totalTime / 2
        val numberOfWaysInAscendingPart = midTime - upperBoundIndex(midTime, totalTime, record) + 1

        return if (totalTime % 2 != 0L) 2* numberOfWaysInAscendingPart else 2*numberOfWaysInAscendingPart -1
    }

    fun part1(input: List<String>): Long {
        val (times, distances) = parseRace(input)

        var multiplication = 1L
        times.forEachIndexed { index, time ->
            val record = distances[index]
            multiplication *= numberOfWaysToBeatRecord(time, record)
        }
        return multiplication
    }

    fun part2(input: List<String>): Long {
        val (time, distance) = parseRacePart2(input)
        return numberOfWaysToBeatRecord(time, distance)
    }

    val testInput = readInput("Day06", true, 1)
    check(part1(testInput) == 288L)

    val testInput2 = readInput("Day06", true, 1)
    check(part2(testInput2) == 71503L)

    val input = readInput("Day06", false)
    part1(input).println()

    check(part2(input) == 27363861L)
    part2(input).println()


}