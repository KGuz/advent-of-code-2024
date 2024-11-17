import advent.*
import parser.*
import resources.*

fun main(args: Array<String>) {
    val (day, example) = parse(args)

    val puzzle = dispatch(day)
    if (puzzle == null) {
        return println("ERR: Puzzle number out of range")
    }

    val input = load(day, example)
    if (input == null) {
        return println("ERR: Unable to load '${path(day, example)}'")
    }

    println(header(" Advent of Code 2024 - Day $day "))
    println(answer("Part one",  puzzle.partOne(input)))
    println(answer("Part two",  puzzle.partTwo(input)))
}

fun header(title: String): String =
    title.padStart(30 + title.length/2, '*').padEnd(60, '*')

fun answer(part: String, answer: String): String =
    "$part ${answer.padStart(51)}"