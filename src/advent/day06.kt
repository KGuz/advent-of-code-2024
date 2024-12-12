package advent

class Day06 : Puzzle {
    override fun partOne(input: String): String {
        val lines = input.lines()
        val size = lines.size

        val (position, direction, obstacles) = parse(lines)
        val path = guardPath(position, direction, size, obstacles)

        return path.distinctBy { it.first }.count().toString()
    }

    override fun partTwo(input: String): String {
        val lines = input.lines()
        val size = lines.size

        val (position, direction, obstacles) = parse(lines)
        val path = guardPath(position, direction, size, obstacles)

        return "0"
    }

    private fun parse(lines: List<String>): Triple<P2<Int>, P2<Int>, HashSet<P2<Int>>> {
        val obstacles = hashSetOf<P2<Int>>()
        var position: P2<Int>? = null
        var direction: P2<Int>? = null

        for (y in 0..lines.lastIndex) {
            for (x in 0..lines.lastIndex) {
                if (lines[y][x] == '#') {
                    obstacles.add(P2(x, y))
                } else if (lines[y][x] == '^') {
                    position = P2(x, y)
                    direction = P2(0, -1)
                }
            }
        }
        return Triple(position!!, direction!!, obstacles)
    }

    private fun guardPath(
        startingPosition: P2<Int>,
        startingDirection: P2<Int>,
        size: Int,
        obstacles: HashSet<P2<Int>>,
    ): List<Pair<P2<Int>, P2<Int>>> {
        val path = mutableListOf(Pair(startingPosition, startingDirection))
        while (true) {
            var (position, direction) = path.last()
            val newPosition = P2(position.x + direction.x, position.y + direction.y)
            if (newPosition.x !in 0..<size || newPosition.y !in 0..<size) {
                break // out of bounds
            }
            if (newPosition in obstacles) {
                direction = turnRight(direction)
                continue
            }
            position = newPosition
            path.add(Pair(position, direction))
        }
        return path
    }

    private fun turnRight(direction: P2<Int>): P2<Int> =
        when (direction) {
            P2(0, -1) -> P2(1, 0)
            P2(1, 0) -> P2(0, 1)
            P2(0, 1) -> P2(-1, 0)
            P2(-1, 0) -> P2(0, -1)
            else -> throw Exception("Invalid direction: $direction")
        }
}
