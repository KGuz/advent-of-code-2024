package advent

class Day03 : Puzzle {
    override fun partOne(input: String): String {
        val regex = Regex("""mul\((\d+),(\d+)\)""")
        return regex
            .findAll(input)
            .sumOf { result ->
                val a = result.groups[1]!!.value.toInt()
                val b = result.groups[2]!!.value.toInt()
                a * b
            }.toString()
    }

    override fun partTwo(input: String): String {
        val multiplications = Regex("""mul\((\d+),(\d+)\)""")
        val conditions = Regex("""(do\(\)|don't\(\))""")
        var ranges = mutableListOf<IntRange>(0..input.length)

        var enable = true
        for (result in conditions.findAll(input)) {
            if (enable == (result.value == "do()")) {
                continue
            }
            enable = !enable

            ranges[ranges.lastIndex] = ranges.last().first..result.range.first
            ranges.add(result.range.last..input.length)
        }
        ranges = ranges.filterIndexed { n, _ -> n % 2 == 0 }.toMutableList()

        return multiplications
            .findAll(input)
            .sumOf { result ->
                val position = result.groups[0]!!.range.first
                val a = result.groups[1]!!.value.toInt()
                val b = result.groups[2]!!.value.toInt()
                if (ranges.any { range -> position in range }) a * b else 0
            }.toString()
    }
}
