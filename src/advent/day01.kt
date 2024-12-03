package advent

import kotlin.math.abs

class Day01 : Puzzle {
    override fun partOne(input: String): String {
        val left: MutableList<Int> = mutableListOf()
        val right: MutableList<Int> = mutableListOf()

        input.lines().forEach { line ->
            val seq = line.split(' ')
            left.add(seq.first().toInt())
            right.add(seq.last().toInt())
        }

        left.sort()
        right.sort()

        return left.zip(right) { a, b -> abs(a - b) }.sum().toString()
    }

    override fun partTwo(input: String): String {
        val left: MutableList<Int> = mutableListOf()
        val right: MutableMap<Int, Int> = mutableMapOf()

        input.lines().forEach { line ->
            val seq = line.split(' ')
            left.add(seq.first().toInt())

            val key = seq.last().toInt()
            right[key] = 1 + (right[key] ?: 0)
        }

        return left.sumOf { it * (right[it] ?: 0) }.toString()
    }
}
