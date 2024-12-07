package advent

private fun Long.concat(other: Long): Long = "$this$other".toLong()

class Day07 : Puzzle {
    override fun partOne(input: String): String {
        val operators: Array<(Long, Long) -> Long> = arrayOf(Long::plus, Long::times)
        return parse(input)
            .filter { equation -> canBeSolved(equation, operators) }
            .sumOf { equation -> equation.result }
            .toString()
    }

    override fun partTwo(input: String): String {
        val operators: Array<(Long, Long) -> Long> = arrayOf(Long::plus, Long::times, Long::concat)
        return parse(input)
            .filter { equation -> canBeSolved(equation, operators) }
            .sumOf { equation -> equation.result }
            .toString()
    }

    data class Equation(
        val result: Long,
        val operands: List<Long>,
    )

    private fun parse(input: String): List<Equation> =
        input.lines().map { line ->
            val parts = line.split(' ', ':').filter { it.isNotBlank() }.map { it.toLong() }
            Equation(parts[0], parts.slice(1..<parts.size))
        }

    private fun canBeSolved(e: Equation, operators: Array<(Long, Long) -> Long>): Boolean {
        if (e.operands.size == 1) return e.operands.first() == e.result
        if (e.operands[1] > e.result) return false // operators can only increase value

        return operators.any { operator ->
            val output = operator(e.operands[0], e.operands[1])
            val collapsed = listOf(output) + e.operands.slice(2..<e.operands.size)
            canBeSolved(Equation(e.result, collapsed), operators)
        }
    }
}
