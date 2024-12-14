package advent

class Day14 : Puzzle {
    override fun partOne(input: String): String {
        var quadrants = arrayOf(0, 0, 0, 0)
        for ((p, v) in parse(input)) {
            val index = quadrant(solve(p, v, 100))
            if (index != null) quadrants[index]++
        }
        return quadrants.reduce { acc, value -> acc * value }.toString()
    }

    override fun partTwo(input: String): String {
        var robots = parse(input)
        var answer = -1

        var buffer = Array(bounds.y) { "" }
        for (t in 0..10000) {
            var new = robots.map { (p, v) -> solve(p, v, t) }.toSet()
            for (y in 0..<bounds.y) {
                val line = (0..<bounds.x).map { x -> if (P2(x, y) in new) '#' else '.' }
                buffer[y] = line.joinToString("")
            }

            if (buffer.any { it.contains("#".repeat(10)) }) {
                answer = t
                buffer.forEach { println(it) }
                break
            }
        }
        return answer.toString()
    }

    private fun parse(input: String): List<Pair<P2<Int>, P2<Int>>> =
        input.lines().map {
            it
                .split(' ')
                .map { it.substring(2).split(',') }
                .map { P2(it.first().toInt(), it.last().toInt()) }
                .let { it[0] to it[1] }
        }

    private fun solve(p: P2<Int>, v: P2<Int>, t: Int): P2<Int> {
        val wrap = { a: Int, b: Int -> (a - (a / b) * b).let { if (it < 0) b + it else it } }
        return (p + (v * t)).let { P2(wrap(it.x, bounds.x), wrap(it.y, bounds.y)) }
    }

    private fun quadrant(pos: P2<Int>): Int? =
        if (pos.x in 0..<bounds.x / 2 && pos.y in 0..<bounds.y / 2) {
            0
        } else if (pos.x in bounds.x / 2 + 1..<bounds.x && pos.y in 0..<bounds.y / 2) {
            1
        } else if (pos.x in 0..<bounds.x / 2 && pos.y in bounds.y / 2 + 1..<bounds.y) {
            2
        } else if (pos.x in bounds.x / 2 + 1..<bounds.x && pos.y in bounds.y / 2 + 1..<bounds.y) {
            3
        } else {
            null
        }

    val bounds = P2(101, 103)
}
