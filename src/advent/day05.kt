package advent

class Day05 : Puzzle {
    override fun partOne(input: String): String {
        val (rules, pages) = parse(input)
        return pages.filter { isValid(rules, it) }.sumOf { it[it.size / 2] }.toString()
    }

    override fun partTwo(input: String): String {
        val (rules, pages) = parse(input)
        val invalidPages = pages.filter { !isValid(rules, it) }.map { it.toMutableList() }
        return "0"
    }

    private fun parse(input: String): Pair<HashMap<Int, HashSet<Int>>, List<List<Int>>> {
        val lines = input.lines()
        val rules = hashMapOf<Int, HashSet<Int>>()
        var pages = listOf<List<Int>>()

        for ((index, value) in lines.withIndex()) {
            if (value.isBlank()) {
                pages =
                    lines.slice(index + 1..lines.lastIndex).map { line ->
                        line.split(',').map { it.toInt() }
                    }
                break
            }

            val nums = value.split('|').map { it.toInt() }
            val set = rules[nums[0]]
            if (set == null) {
                rules[nums[0]] = hashSetOf(nums[1])
            } else {
                set.add(nums[1])
            }
        }
        return Pair(rules, pages)
    }

    private fun isValid(rules: HashMap<Int, HashSet<Int>>, page: Collection<Int>): Boolean {
        val seen = hashSetOf<Int>()
        for (number in page) {
            if (rules[number]?.any { seen.contains(it) } == true) {
                return false
            }
            seen.add(number)
        }
        return true
    }
}
