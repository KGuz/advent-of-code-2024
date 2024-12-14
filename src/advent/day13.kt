package advent

class Day13 : Puzzle {
    override fun partOne(input: String): String =
        parse(input)
            .mapNotNull { (f, g) -> solve(f, g) }
            .sumOf { (a, b) -> 3 * a + b }
            .toString()

    override fun partTwo(input: String): String {
        val correct = { a: Array<Long> -> arrayOf(a[0], a[1], a[2] + 10000000000000) }
        return parse(input)
            .map { (f, g) -> correct(f) to correct(g) }
            .mapNotNull { (f, g) -> solve(f, g) }
            .sumOf { (a, b) -> 3 * a + b }
            .toString()
    }

    private fun parse(input: String): List<Pair<Array<Long>, Array<Long>>> {
        val button = Regex("""Button [A|B]: X(.*), Y(.*)""")
        val prize = Regex("""Prize: X=(.*), Y=(.*)""")
        val capture = { r: Regex, s: String ->
            r.find(s)!!.groups.let { it[1]!!.value.toLong() to it[2]!!.value.toLong() }
        }
        return input.lines().chunked(4).map {
            val a = capture(button, it[0])
            val b = capture(button, it[1])
            val p = capture(prize, it[2])
            arrayOf(a.first, b.first, p.first) to arrayOf(a.second, b.second, p.second)
        }
    }

    private fun solve(f: Array<Long>, g: Array<Long>): Pair<Long, Long>? {
        val h = f.map { g[0] * it }.zip(g.map { f[0] * it }).map { it.first - it.second }

        val b = h[2] / h[1]
        val a = (f[2] - b * f[1]) / f[0]

        val valid = a >= 0 && b >= 0 && a * g[0] + b * g[1] == g[2]
        return if (valid) a to b else null
    }
}
