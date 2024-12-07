package advent

class Day04 : Puzzle {
    override fun partOne(input: String): String {
        val grid = input.lines()
        var total = 0

        for (row in grid.indices) {
            for (col in 0..grid.size - 4) {
                val token = "${grid[row][col]}${grid[row][col + 1]}${grid[row][col + 2]}${grid[row][col + 3]}"
                if (token == "XMAS" || token == "SAMX") {
                    total++
                }
            }
        }
        for (col in grid.indices) {
            for (row in 0..grid.size - 4) {
                val token = "${grid[row][col]}${grid[row + 1][col]}${grid[row + 2][col]}${grid[row + 3][col]}"
                if (token == "XMAS" || token == "SAMX") {
                    total++
                }
            }
        }
        for (row in 0..grid.size - 4) {
            for (col in 0..grid.size - 4) {
                val token1 = "${grid[row][col]}${grid[row + 1][col + 1]}${grid[row + 2][col + 2]}${grid[row + 3][col + 3]}"
                val token2 = "${grid[row + 3][col]}${grid[row + 2][col + 1]}${grid[row + 1][col + 2]}${grid[row][col + 3]}"
                if (token1 == "XMAS" || token1 == "SAMX") {
                    total++
                }
                if (token2 == "XMAS" || token2 == "SAMX") {
                    total++
                }
            }
        }
        return total.toString()
    }

    override fun partTwo(input: String): String {
        val grid = input.lines()
        var total = 0

        for (row in 0..grid.size - 3) {
            for (col in 0..grid.size - 3) {
                val token1 = "${grid[row][col]}${grid[row + 1][col + 1]}${grid[row + 2][col + 2]}"
                val token2 = "${grid[row + 2][col]}${grid[row + 1][col + 1]}${grid[row][col + 2]}"

                if ((token1 == "MAS" || token1 == "SAM") && (token2 == "MAS" || token2 == "SAM")) {
                    total++
                }
            }
        }
        return total.toString()
    }
}
