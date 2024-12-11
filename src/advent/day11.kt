package advent

class Day11 : Puzzle {
    override fun partOne(input: String): String =
        input
            .split(' ')
            .map { string -> string.toLong() }
            .sumOf { stone -> blink(stone, 25) }
            .toString()

    override fun partTwo(input: String): String =
        input
            .split(' ')
            .map { string -> string.toLong() }
            .sumOf { stone -> blink(stone, 75) }
            .toString()

    private fun blink(stone: Long, times: Int): Long {
        if (times == 0) return 1

        var value = cache[Pair(stone, times)]
        if (value != null) return value

        value =
            when {
                stone == 0L -> blink(1, times - 1)
                length(stone) % 2 == 0 -> split(stone).sumOf { blink(it, times - 1) }
                else -> blink(stone * 2024, times - 1)
            }
        cache[Pair(stone, times)] = value
        return value
    }

    private fun length(stone: Long): Int = stone.toString().length

    private fun split(stone: Long): Array<Long> {
        val string = stone.toString()
        return arrayOf(
            string.substring(0, string.length / 2).toLong(),
            string.substring(string.length / 2, string.length).toLong(),
        )
    }

    private val cache = mutableMapOf<Pair<Long, Int>, Long>()
}
