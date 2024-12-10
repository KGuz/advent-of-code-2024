package advent

class Day10 : Puzzle {
    override fun partOne(input: String): String {
        val grid = input.lines().map(String::toList)
        return grid.indices
            .flatMap { y -> grid.indices.map { x -> y to x } }
            .filter { (y, x) -> grid[y][x] == '0' }
            .sumOf { (y, x) -> score(P2(x, y), grid, true) }
            .toString()
    }

    override fun partTwo(input: String): String {
        val grid = input.lines().map(String::toList)
        return grid.indices
            .flatMap { y -> grid.indices.map { x -> y to x } }
            .filter { (y, x) -> grid[y][x] == '0' }
            .sumOf { (y, x) -> score(P2(x, y), grid, false) }
            .toString()
    }

    fun score(
        trail: P2<Int>,
        grid: List<List<Char>>,
        unique: Boolean = true,
    ): Int {
        val queue = ArrayDeque(listOf(trail))
        val visited = hashSetOf<P2<Int>>()
        var trailheads = 0

        while (queue.isNotEmpty()) {
            val pos = queue.removeFirst()
            if (pos in visited) continue

            if (unique) visited.add(pos)
            if (grid[pos.y][pos.x] == '9') trailheads++

            for (dir in listOf(P2(1, 0), P2(0, 1), P2(-1, 0), P2(0, -1))) {
                val next = P2(pos.x + dir.x, pos.y + dir.y)
                if (next.x !in 0..<grid.size || next.y !in 0..<grid.size) continue

                if (grid[next.y][next.x] == grid[pos.y][pos.x] + 1) {
                    queue.add(next)
                }
            }
        }
        return trailheads
    }
}
