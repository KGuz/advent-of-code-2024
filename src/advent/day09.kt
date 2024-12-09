package advent

data class Block(
    var id: Int,
    var size: Int = 1,
)

const val FREE = -1

class Day09 : Puzzle {
    override fun partOne(input: String): String {
        val disk = parse(input)

        var lo = 0
        var hi = disk.size - 1
        while (lo < hi) {
            if (disk[lo].id == FREE) {
                while (disk[hi].id == FREE) hi--

                if (disk[lo].size == disk[hi].size) {
                    disk[lo].id = disk[hi].id
                    disk[hi].id = FREE
                } else if (disk[lo].size < disk[hi].size) {
                    disk[hi + 1].size += disk[lo].size
                    disk[hi].size -= disk[lo].size
                    disk[lo].id = disk[hi].id
                } else {
                    if (disk[lo + 1].id == FREE) {
                        disk[lo + 1].size += disk[lo].size - disk[hi].size
                    } else {
                        disk.add(lo + 1, Block(FREE, disk[lo].size - disk[hi].size))
                        hi++
                    }
                    disk[lo] = disk[hi].copy()
                    disk[hi].id = FREE
                }
            }
            lo++
        }
        return checksum(disk).toString()
    }

    override fun partTwo(input: String): String {
        val disk = parse(input)
        val seen = hashSetOf<Int>()

        var ptr = disk.size - 1
        while (true) {
            while (ptr > 0 && disk[ptr].id == FREE) ptr--
            if (ptr == 0) break
            if (seen.contains(disk[ptr].id)) {
                ptr--
                continue
            }
            seen.add(disk[ptr].id)

            val free = disk.indexOfFirst { it.id == FREE && it.size >= disk[ptr].size }
            if (free == -1 || free > ptr) {
                ptr--
                continue
            }

            if (disk[free].size == disk[ptr].size) {
                disk[free].id = disk[ptr].id
                disk[ptr].id = FREE
            } else {
                if (disk[free + 1].id == FREE) {
                    disk[free + 1].size += disk[free].size - disk[ptr].size
                } else {
                    disk.add(free + 1, Block(FREE, disk[free].size - disk[ptr].size))
                    ptr++
                }
                disk[free] = disk[ptr].copy()
                disk[ptr].id = FREE
            }

            if (ptr + 1 < disk.size && disk[ptr + 1].id == FREE) {
                disk[ptr].size += disk[ptr + 1].size
                disk[ptr + 1].size = 0
            }
            if (disk[ptr - 1].id == FREE) {
                disk[ptr - 1].size += disk[ptr].size
                disk[ptr].size = 0
            }
        }
        return checksum(disk).toString()
    }

    private fun parse(input: String): MutableList<Block> {
        val disk = mutableListOf<Block>()
        for ((id, string) in input.chunked(2).withIndex()) {
            var digit = string.first().digitToInt()
            if (digit != 0) disk.add(Block(id, digit))

            digit = string.lastOrNull()?.digitToInt() ?: 0
            if (digit != 0) disk.add(Block(FREE, digit))
        }
        return disk
    }

    private fun checksum(disk: List<Block>): Long {
        var index = 0L
        var sum = 0L

        for (block in disk) {
            for (i in 1..block.size) {
                if (block.id == FREE) {
                    index++
                    continue
                }
                sum += block.id * index++
            }
        }
        return sum
    }
}
