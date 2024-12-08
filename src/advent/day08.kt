package advent

class Day08 : Puzzle {
    override fun partOne(input: String): String {
        val (map, size) = parse(input)
        val global = hashSetOf<P2<Int>>()

        for ((_, nodes) in map) {
            global += calculateAntinodes(nodes, size)
        }
        return global.size.toString()
    }

    override fun partTwo(input: String): String {
        val (map, size) = parse(input)
        val global = hashSetOf<P2<Int>>()

        for ((_, nodes) in map) {
            global += calculateResonantAntinodes(nodes, size)
        }
        return global.size.toString()
    }

    private fun parse(input: String): Pair<HashMap<Char, MutableList<P2<Int>>>, Int> {
        val lines = input.lines()
        val map = hashMapOf<Char, MutableList<P2<Int>>>()

        for (y in lines.indices) {
            for (x in lines[y].indices) {
                if (lines[y][x] == '.') continue
                map[lines[y][x]]?.add(P2(x, y)) ?: map.put(lines[y][x], mutableListOf(P2(x, y)))
            }
        }
        return Pair(map, lines.size)
    }

    private fun calculateAntinodes(nodes: List<P2<Int>>, size: Int): HashSet<P2<Int>> {
        val antinodes = hashSetOf<P2<Int>>()
        for (i in 0..<nodes.lastIndex) {
            for (j in 1 + i..nodes.lastIndex) {
                val dist = P2(nodes[j].x - nodes[i].x, nodes[j].y - nodes[i].y)

                var antinode = P2(nodes[j].x + dist.x, nodes[j].y + dist.y)
                if (antinode.x in 0..<size && antinode.y in 0..<size) {
                    antinodes.add(antinode)
                }

                antinode = P2(nodes[i].x - dist.x, nodes[i].y - dist.y)
                if (antinode.x in 0..<size && antinode.y in 0..<size) {
                    antinodes.add(antinode)
                }
            }
        }
        return antinodes
    }

    private fun calculateResonantAntinodes(nodes: List<P2<Int>>, size: Int): HashSet<P2<Int>> {
        val antinodes = hashSetOf<P2<Int>>()
        for (i in 0..<nodes.lastIndex) {
            for (j in 1 + i..nodes.lastIndex) {
                val dist = P2(nodes[j].x - nodes[i].x, nodes[j].y - nodes[i].y)

                var antinode = P2(nodes[i].x, nodes[i].y)
                while (antinode.x in 0..<size && antinode.y in 0..<size) {
                    antinodes.add(antinode)
                    antinode = P2(antinode.x + dist.x, antinode.y + dist.y)
                }

                antinode = P2(nodes[j].x, nodes[j].y)
                while (antinode.x in 0..<size && antinode.y in 0..<size) {
                    antinodes.add(antinode)
                    antinode = P2(antinode.x - dist.x, antinode.y - dist.y)
                }
            }
        }
        return antinodes
    }
}
