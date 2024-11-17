package parser

fun parse(args: Array<String>): Pair<Int, Boolean> {
    var (day, example) = Pair(1, true)
    for (arg in args) {
        if (arg.contains("day")) {
            day = arg.split("=")[1].toInt()
        }
        if (arg.contains("example")) {
            example = arg.split("=")[1].toBoolean()
        }
    }
    return Pair(day, example)
}
