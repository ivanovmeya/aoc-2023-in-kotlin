fun main() {

    fun parseInstructions(input: List<String>): List<Char> {
        return input[0].toCharArray().toList()
    }

    fun parse(input: List<String>): Map<String, Pair<String, String>> {
        return input.drop(2).associate { line ->
            line.split('=').run {
                val key = first().trim()
                val (left, right) = last().split(", ").run {
                    first().trim().removePrefix("(") to last().trim().removeSuffix(")")
                }
                key to (left to right)
            }
        }
    }

    fun gcd(num1: Long, num2: Long): Long {
        var n1 = num1
        var n2 = num2

        while (n2 != 0L) {
            val tmp = n2
            n2 = n1 % n2
            n1 = tmp
        }


        return n1
    }

    fun lcm(nums: IntArray): Long {
        var lcm = nums.first().toLong()

        for (i in 1 until nums.size) {
            val num2 = nums[i].toLong()
            lcm = (lcm * num2) / gcd(lcm, num2)
        }
        return lcm
    }

    fun part1(input: List<String>): Int {
        val inst = parseInstructions(input)
        val map = parse(input)

        var key = "AAA"
        var instIndex = 0
        var steps = 0

        while (key != "ZZZ") {
            //support looping through instructions
            if (instIndex == inst.size) instIndex = 0
            key = if (inst[instIndex] == 'L') {
                map[key]!!.first
            } else {
                map[key]!!.second
            }
            steps++
            instIndex++
        }
        return steps
    }

    fun part2(input: List<String>): Long {
        val inst = parseInstructions(input)
        val map = parse(input)

        //find all starting points
        val starts = map.keys.filter { it.last() == 'A' }

        val steps = starts.map { key ->
            var instIndex = 0
            var count = 0

            var currentKey = key

            while (currentKey.last() != 'Z') {
                //support looping through instructions
                if (instIndex == inst.size) instIndex = 0

                currentKey = if (inst[instIndex] == 'L') {
                    map[currentKey]!!.first
                } else {
                    map[currentKey]!!.second
                }

                count++
                instIndex++
            }
            count
        }.toIntArray()

        //Loops are without offsets so their all will meet at lcm of each steps
        return lcm(steps)

    }

    val testInput = readInput("Day08", true, 1)
    check(part1(testInput) == 2)

    val testInput2 = readInput("Day08", true, 2)
    check(part1(testInput2) == 6)

    val testInput3 = readInput("Day08", true, 3)
    check(part2(testInput3) == 6L)

    val input = readInput("Day08", false)
    part1(input).println()
    part2(input).println()
}