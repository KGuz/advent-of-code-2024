package advent

interface Puzzle {
    fun partOne(input: String): String = "0"

    fun partTwo(input: String): String = "0"
}

fun dispatch(day: Int): Puzzle? =
    when (day) {
        1 -> Day01()
        else -> null
    }
