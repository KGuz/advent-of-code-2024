package advent

class Day12 : Puzzle {
    override fun partOne(input: String): String {
        val garden = parse(input)
        var regions = mutableSetOf<P2<Int>>()

        var price = 0
        for (key in garden.keys) {
            if (key in regions) continue
            val region = isolate(garden, key)

            price += region.size * edges(region)
            regions.addAll(region)
        }
        return price.toString()
    }

    override fun partTwo(input: String): String {
        val garden = parse(input)
        var regions = mutableSetOf<P2<Int>>()

        var price = 0
        for (key in garden.keys) {
            if (key in regions) continue
            val region = isolate(garden, key)

            price += region.size * corners(region)
            regions.addAll(region)
        }
        return price.toString()
    }

    private fun parse(input: String): Map<P2<Int>, Char> {
        val grid = input.lines()
        return grid.indices
            .flatMap { y -> grid.indices.map { x -> y to x } }
            .associate { (y, x) -> P2(x, y) to grid[y][x] }
    }

    private fun isolate(garden: Map<P2<Int>, Char>, seed: P2<Int>): Set<P2<Int>> {
        var queue = ArrayDeque(listOf(seed))
        var region = mutableSetOf<P2<Int>>()

        while (queue.isNotEmpty()) {
            val point = queue.removeFirst()
            if (point in region) continue
            region.add(point)

            for (dir in listOf(P2(1, 0), P2(0, 1), P2(-1, 0), P2(0, -1))) {
                val next = point + dir
                if (next !in garden) continue
                if (garden[next] == garden[point]) {
                    queue.add(next)
                }
            }
        }
        return region
    }

    private fun edges(region: Set<P2<Int>>): Int =
        region.sumOf { point ->
            listOf(P2(0, -1), P2(1, 0), P2(0, 1), P2(-1, 0))
                .map { dir -> point + dir }
                .count { neighbor -> neighbor in region }
                .let { 4 - it }
        }

    private fun corners(region: Set<P2<Int>>): Int {
        val u = P2(0, -1)
        val r = P2(1, 0)
        val d = P2(0, 1)
        val l = P2(-1, 0)

        return region.sumOf { p ->
            arrayOf(
                p + u !in region && p + l !in region,
                p + u !in region && p + r !in region,
                p + d !in region && p + l !in region,
                p + d !in region && p + r !in region,
                p + u in region && p + l in region && p + l + u !in region,
                p + u in region && p + r in region && p + r + u !in region,
                p + d in region && p + l in region && p + l + d !in region,
                p + d in region && p + r in region && p + r + d !in region,
            ).count { it }
        }
    }
}
