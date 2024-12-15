package advent

class Day15 : Puzzle {
    override fun partOne(input: String): String {
        val (grid, sequence) = parseOne(input)
        var robot =
            grid.indices
                .flatMap { y -> grid[y].indices.map { x -> arrayOf(x, y) } }
                .first { (x, y) -> grid[y][x] == '@' }

        for (dir in sequence) {
            if (tryMove(grid, robot, dir)) {
                robot = arrayOf(robot.x + dir.x, robot.y + dir.y)
            }
        }

        return grid.indices
            .flatMap { y -> grid[y].indices.map { x -> x to y } }
            .filter { (x, y) -> grid[y][x] == 'O' }
            .sumOf { (x, y) -> 100 * y + x }
            .toString()
    }

    override fun partTwo(input: String): String {
        val (grid, sequence) = parseTwo(input)
        var robot =
            grid.indices
                .flatMap { y -> grid[y].indices.map { x -> arrayOf(x, y) } }
                .first { (x, y) -> grid[y][x] == '@' }

        for (dir in sequence) {
            if (canMove(grid, robot, dir)) {
                move(grid, robot, dir)
                robot = arrayOf(robot.x + dir.x, robot.y + dir.y)
            }
        }

        return grid.indices
            .flatMap { y -> grid[y].indices.map { x -> x to y } }
            .filter { (x, y) -> grid[y][x] == '[' }
            .sumOf { (x, y) -> 100 * y + x }
            .toString()
    }

    private fun parseOne(input: String): Pair<MutableList<MutableList<Char>>, List<Array<Int>>> {
        val lines = input.lines()
        val grid = lines.takeWhile { it.isNotEmpty() }.map { it.toMutableList() }.toMutableList()
        val sequence =
            lines.subList(grid.size + 1, lines.size).flatMap(String::toList).map {
                when (it) {
                    '<' -> arrayOf(-1, 0)
                    '^' -> arrayOf(0, -1)
                    '>' -> arrayOf(1, 0)
                    else -> arrayOf(0, 1)
                }
            }
        return grid to sequence
    }

    private fun parseTwo(input: String): Pair<MutableList<MutableList<Char>>, List<Array<Int>>> {
        val (grid, sequence) = parseOne(input)
        var double = { c: Char ->
            when (c) {
                '#' -> listOf('#', '#')
                'O' -> listOf('[', ']')
                '@' -> listOf('@', '.')
                else -> listOf('.', '.')
            }
        }
        for (y in grid.indices) grid[y] = grid[y].flatMap(double).toMutableList()
        return grid to sequence
    }

    private fun swap(grid: MutableList<MutableList<Char>>, a: Array<Int>, b: Array<Int>) {
        val tile = grid[a.y][a.x]
        grid[a.y][a.x] = grid[b.y][b.x]
        grid[b.y][b.x] = tile
    }

    private fun tryMove(
        grid: MutableList<MutableList<Char>>,
        pos: Array<Int>,
        dir: Array<Int>,
    ): Boolean {
        val next = arrayOf(pos.x + dir.x, pos.y + dir.y)
        when (grid[next.y][next.x]) {
            'O' -> {
                if (tryMove(grid, next, dir)) {
                    swap(grid, pos, next)
                    return true
                } else {
                    return false
                }
            }
            '.' -> {
                swap(grid, pos, next)
                return true
            }
            else -> return false
        }
    }

    private fun canMove(
        grid: MutableList<MutableList<Char>>,
        pos: Array<Int>,
        dir: Array<Int>,
    ): Boolean {
        val next = arrayOf(pos.x + dir.x, pos.y + dir.y)
        when (grid[next.y][next.x]) {
            '[', ']' -> {
                if (dir.y == 0) {
                    return canMove(grid, next, dir)
                } else {
                    val offset = if (grid[next.y][next.x] == '[') 1 else -1
                    val additional = arrayOf(next.x + offset, next.y)
                    return canMove(grid, next, dir) && canMove(grid, additional, dir)
                }
            }
            '.' -> return true
            else -> return false
        }
    }

    private fun move(grid: MutableList<MutableList<Char>>, pos: Array<Int>, dir: Array<Int>) {
        val next = arrayOf(pos.x + dir.x, pos.y + dir.y)
        when (grid[next.y][next.x]) {
            '[', ']' -> {
                if (dir.y == 0) {
                    move(grid, next, dir)
                    swap(grid, pos, next)
                } else {
                    val offset = if (grid[next.y][next.x] == '[') 1 else -1
                    val additional = arrayOf(next.x + offset, next.y)
                    move(grid, next, dir)
                    move(grid, additional, dir)
                    swap(grid, pos, next)
                }
            }
            '.' -> swap(grid, pos, next)
            else -> return
        }
    }
}
