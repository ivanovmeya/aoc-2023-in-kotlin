fun main() {

    //region ParseData
    fun parseSeedsToRanges(input: List<String>): List<LongRange> {
        val rowNumbers = input.first().split(':').last().trim().split(' ').map { it.toLong() }
        val seedRanges = rowNumbers.windowed(2, 2).map {
            val start = it.first()
            val length = it.last()
            LongRange(start, start + length - 1)
        }
        return seedRanges
    }

    fun parseSeeds(input: List<String>): List<Long> {
        return input.first().split(':').last().trim().split(' ').map { it.toLong() }
    }

    fun parseMaps(input: List<String>): List<List<MapRange>> {

        //parse maps
        //It will be 7 maps, but they are chaining
        var index = 1
        var currentMap = -1

        val mapRanges = mutableListOf<MutableList<MapRange>>()
        while (index < input.size) {
            val line = input[index]
            when {
                line.isEmpty() -> index++
                line.contains("map") -> {
                    currentMap++
                    mapRanges.add(mutableListOf())
                    index++
                }
                else -> {
                    //range here
                    val (destStart, sourceStart, length) = line.split(' ').map { it.trim().toLong() }
                    mapRanges[currentMap].add(MapRange(destStart, sourceStart, length))
                    index++
                }
            }
        }

        return mapRanges
    }
    //endregion ParseData

    fun rangeThroughMap(range: LongRange, mapRanges: List<MapRange>): List<LongRange> {
        val resultRanges = mutableListOf<LongRange>()
        val queue = ArrayDeque<LongRange>().apply { add(range) }

        mapRanges.forEach { mapRange ->
            val size = queue.size
            repeat(size) {
                val rangeToProcess = queue.removeFirst()
                val mapRangeSource = LongRange(mapRange.sourceStart, mapRange.sourceStart + mapRange.length - 1)

                //find the intersections between ranges
                val (intersection, leftOvers) = rangeToProcess.intersect(mapRangeSource)
                //we map intersection, and for leftovers push them to processing queue and will try to intersect with other current map ranges
                if (intersection != null) {
                    resultRanges.add(
                        LongRange(
                            mapRange.destStart + (intersection.first - mapRange.sourceStart),
                            mapRange.destStart + (intersection.last - mapRange.sourceStart)
                        )
                    )
                }

                //push non intersected ranges for next mapRange processing
                if (leftOvers.isNotEmpty()) queue.addAll(leftOvers)
            }

        }

        //if we left with unprocessed ranges, then it means there are no intersections, and we map seeds as it was
        resultRanges.addAll(queue)
        return resultRanges

    }

    //simulation of seed going through all maps
    fun processSeed(seed: Long, maps: List<List<MapRange>>): Long {
        //find if transformation exist
        //look at source start for each map line
        //if seed in [sourceStart, sourceStart+length-1] map it to destStart + (seed - sourceStart)
        //if no one matches keep seed as it is

        //for each map, check if mapping exists with above formula, and then proceed to next map
        var s = seed
        maps.forEach { ranges ->
            val matchedRange = ranges.firstOrNull { s in LongRange(it.sourceStart, it.sourceStart + it.length - 1) }
            s = if (matchedRange != null) {
                matchedRange.destStart + (s - matchedRange.sourceStart)
            } else {
                s
            }
        }

        //s have location
        return s
    }

    fun part1(input: List<String>): Long {
        val seeds = parseSeeds(input)
        val maps = parseMaps(input)

        return seeds.minOf { seed ->
            processSeed(seed, maps)
        }
    }

    fun part2(input: List<String>): Long {
        val seedRanges = parseSeedsToRanges(input)
        val maps = parseMaps(input)

        var currentSeedRanges = seedRanges
        //transform seedRanges to finalRanges and get minimum start of the range

        //for seedRanges we apply map transformations and use this ranges as input to next map transformation
        maps.forEach { mapRanges ->
            currentSeedRanges = currentSeedRanges
                .map { seedRange -> rangeThroughMap(seedRange, mapRanges) }
                .flatten()
        }

        //now we have final ranges, just get the minimum start of it
        return currentSeedRanges.minOf { it.first }
    }

    val testInput = readInput("Day05", true, 1)
    check(part1(testInput) == 35L)

    val testInput2 = readInput("Day05", true, 1)
    check(part2(testInput2) == 46L)

    val input = readInput("Day05", false)
    part1(input).println()
    part2(input).println()
}

data class MapRange(val destStart: Long, val sourceStart: Long, val length: Long)

//returns intersection and leftovers (not intersected part of the source range)
fun LongRange.intersect(other: LongRange): Pair<LongRange?, List<LongRange>> {
    //NO intersections
    if (last < other.first || first > other.last) return null to listOf(this)

    val intersection = LongRange(
        kotlin.math.max(first, other.first),
        kotlin.math.min(last, other.last)
    )

    val leftOvers = mutableListOf<LongRange>()
    if (first < intersection.first) {
        leftOvers.add(LongRange(first, intersection.first))
    }

    if (last > intersection.last) {
        leftOvers.add(LongRange(last, intersection.last))
    }

    return intersection to leftOvers
}