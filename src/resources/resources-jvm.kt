package resources

import java.io.File

fun load(day: Int, example: Boolean): String? {
    val file = File(path(day, example))
    return if (file.exists()) file.readText() else null
}

fun path(day: Int, example: Boolean): String {
    val folder = if (example) "examples" else "inputs"
    val number = day.toString().padStart(2, '0')
    return "assets/$folder/day$number.txt"
}
