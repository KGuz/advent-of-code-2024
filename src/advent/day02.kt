package advent

import kotlin.math.*

class Day02 : Puzzle {
    override fun partOne(input: String): String =
        input
            .lines()
            .sumOf { line ->
                val report = line.split(' ').map { it.toInt() }
                isSafe(report).compareTo(false)
            }.toString()

    override fun partTwo(input: String): String =
        input
            .lines()
            .sumOf { line ->
                val report = line.split(' ').map { it.toInt() }
                val isTolerable: (Int) -> Boolean = {
                    isSafe(report.filterIndexed { i, _ -> i != it })
                }
                report.indices.any(isTolerable).compareTo(false)
            }.toString()

    private fun isSafe(numbers: Collection<Int>): Boolean {
        val diff = numbers.windowed(2).map { it.last() - it.first() }
        val tendency = calculateTendency(diff)
        return diff.withIndex().all { checkLevels(it.value, tendency) }
    }

    private fun calculateTendency(list: Collection<Int>): Int {
        var (inc, dec) = Pair(0, 0)
        for (item in list) {
            inc += (item > 0).compareTo(false)
            dec += (item < 0).compareTo(false)
        }
        return if (inc > dec) 1 else -1
    }

    private fun checkLevels(diff: Int, tendency: Int): Boolean =
        diff.sign == tendency && (abs(diff) in 1..3)
}
