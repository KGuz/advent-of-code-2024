@file:OptIn(ExperimentalForeignApi::class)

package resources

import kotlinx.cinterop.*
import platform.posix.*

fun load(day: Int, example: Boolean): String? {
    val file = fopen(path(day, example), "r") ?: return null
    fseek(file, 0, SEEK_END)
    val size = ftell(file)
    val buffer = ByteArray(size.toInt())

    fseek(file, 0, SEEK_SET)
    fread(buffer.refTo(0), size.toULong(), 1u, file)
    fclose(file)
    return buffer.decodeToString().trimEnd()
}

fun path(day: Int, example: Boolean): String {
    val folder = if (example) "examples" else "inputs"
    val number = day.toString().padStart(2, '0')
    return "assets/$folder/day$number.txt"
}
