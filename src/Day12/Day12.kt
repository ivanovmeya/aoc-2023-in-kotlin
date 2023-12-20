fun main() {

    fun parseRow(input: String): Pair<String, List<Int>> = input.split(" ").run {
        first() to last().split(",").map { it.toInt() }
    }

    fun parseRowUnfolded(input: String): Pair<String, List<Int>> = input.split(" ").run {

        val springs = first()

        var springsUnfolded = springs
        repeat(4) {
            springsUnfolded = "${springsUnfolded}?${springs}"
        }

        val damage = last().split(",").map { it.toInt() }

        var damageUnfolded = damage

        repeat(4) {
            damageUnfolded = damageUnfolded + damage
        }

        springsUnfolded to damageUnfolded
    }

    //at each step springs will be reduced and at some steps damage will be reduced
    fun arrangements(springs: String, damage: List<Int>, cache: MutableMap<Pair<String, List<Int>>, Long>): Long {
//        println("arrangements call: spr=$springs, ${damage.joinToString(",")}")
        //if damage is empty and pattern do not have any wildcards -> we can increase number of Ways

        val cacheHit = cache[springs to damage]
        if (cacheHit != null) return cacheHit

        if (springs.isEmpty()) {
            return if (damage.isEmpty()) 1 else 0
        }

        val count =  when (springs.first()) {
            '.' -> arrangements(springs.dropWhile { it == '.' }, damage, cache)
            '?' -> arrangements(springs.substring(1), damage, cache) + arrangements("#${springs.substring(1)}", damage, cache)
            '#' -> {
                if (damage.isEmpty()) return 0
                //take current damage and try to apply it if possible
                val contiguousSharpCount = damage.first()
                //we should try to build contiguous sharps, if not possible -> return 0
                if (springs.length < contiguousSharpCount) return 0

                //what would be eligible?
                //it should be contiguousSharpCount sharps, or wildcard, no way around it
                var count = 0
                while (count < contiguousSharpCount && (springs[count] == '?' || springs[count] == '#')) count++
                //we can't build contiguous sharp group
                if (count != contiguousSharpCount) return 0

                //next element after contiguous group  should be end of string or .
                if (count < springs.length && springs[count] == '#') return 0

                //we should force next element to be . (if it's wildcard)
                if (count < springs.length && springs[count] == '?') {
                    arrangements(springs.substring(contiguousSharpCount+1), damage.subList(1, damage.size), cache)
                } else {
                    arrangements(springs.substring(contiguousSharpCount), damage.subList(1, damage.size), cache)
                }
            }
            else -> throw IllegalStateException("Something went wrong, unknown symbol = ${springs.first()}")
        }

        cache[springs to damage] = count
        return count
    }

    fun part1(input: List<String>): Long {
        return input
            .map { row -> parseRow(row) }
            .sumOf { (springs: String, damage: List<Int>) ->
                val count = arrangements(springs, damage, mutableMapOf())
//                println("$springs ${damage.joinToString(",")} :count=$count")
                count
            }
    }

    fun part2(input: List<String>): Long {

        return input
            .map { row -> parseRowUnfolded(row) }
            .sumOf { (springs: String, damage: List<Int>) ->
                val count = arrangements(springs, damage, mutableMapOf())
//                println("$springs ${damage.joinToString(",")} :count=$count")
                count
            }
    }

    val testInput = readInput("Day12", true, 1)
    println("Part1 test = ${part1(testInput)}")
    check(part1(testInput) == 21L)

    check(part2(testInput) == 525152L)

    val input = readInput("Day12", false)
    part1(input).println()
    part2(input).println()
}